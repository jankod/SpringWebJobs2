package hr.bioinfo.swj.util;

import aj.org.objectweb.asm.*;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.MethodInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.LineNumberTable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Aspect
@Component
public class ControllerTrackingAspect {

    @Around ("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object trackController(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //  Class<?> originalClass = method.getDeclaringClass();


        Class<?> originalClass = AopUtils.getTargetClass(joinPoint.getTarget());

        // Dohvati trenutni model
        Object[] args = joinPoint.getArgs();
        Model model = null;
        for (Object arg : args) {
            if (arg instanceof Model) {
                model = (Model) arg;
                break;
            }
        }

        // Dohvati informacije o klasi i metodi
        String className = originalClass.getName();
        String sourceFile = originalClass.getSimpleName() + ".java";
        String methodName = method.getName();


//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        int lineNumber = -1;
//        for (StackTraceElement element : stackTrace) {
//            String elementClassName = element.getClassName();
//            if (elementClassName.startsWith(className) && (elementClassName.equals(className) || elementClassName.contains("$$"))
//                    && element.getMethodName().equals(methodName)) {
//                lineNumber = element.getLineNumber();
//                log.debug("Element " + element.getClassName() + " Linija: " + lineNumber);
//                break;
//            }
//        }

        int lineNumber = getStartLine(method);

        // Spremi podatke u model
        if (model != null) {
            ControllerInfo controllerInfo = new ControllerInfo(
                    className,
                    methodName,
                    sourceFile,
                    lineNumber
            );
            model.addAttribute("controllerInfo", controllerInfo);
        }

        // Nastavi s izvršavanjem metode kontrolera
        return joinPoint.proceed();
    }

    private int getMethodLineNumber(final Class<?> clazz, final String methodName) {
        final AtomicInteger lineNumber = new AtomicInteger(-1);

        try {
            ClassReader classReader = new ClassReader(clazz.getName());
            classReader.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                 String signature, String[] exceptions) {
                    if (!name.equals(methodName)) {
                        return null;
                    }

                    return new MethodVisitor(Opcodes.ASM9) {
                        @Override
                        public void visitLineNumber(int line, Label label) {
                            if (lineNumber.get() == -1) {
                                lineNumber.set(line);
                            }
                        }
                    };
                }
            }, 0);
        } catch (Exception e) {
            log.warn("Failed to get line number: {}", e.getMessage());
        }

        return lineNumber.get();
    }


    /**
     * Vrati početni broj linije (source line) za danu java.lang.reflect.Method
     */
    public static int getStartLine(Method m) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        // učitaj CtClass verziju tvoje klase
        CtClass cc = pool.get(m.getDeclaringClass().getName());

        // izvuči prvi CtMethod s tim imenom (brže, ali pazi na overloadove!)
        CtMethod cm = cc.getDeclaredMethod(m.getName());

        // dohvati LineNumberTable
        LineNumberAttribute lna = (LineNumberAttribute)
                cm.getMethodInfo()
                        .getCodeAttribute()
                        .getAttribute(LineNumberAttribute.tag);

        // prva entry ima početnu liniju
        return lna.lineNumber(0);
    }

    public static String getControllerTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : stackTrace) {
            // Filter out Spring i JDK klase, ostavi samo tvoje kontrolere
            if (element.getClassName().startsWith("hr.bioinfo.swj")) {
                return String.format("Kontroler: %s, Metoda: %s, Linija: %d",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getLineNumber());
            }
        }
        return "Nepoznat izvor";
    }


//        public String getIdeLink() {
//            return "idea://open?file=/putanja/do/projekta/src/main/java/"
//                    + className.replace('.', '/') + ".java&line=" + lineNumber;
//        }
}