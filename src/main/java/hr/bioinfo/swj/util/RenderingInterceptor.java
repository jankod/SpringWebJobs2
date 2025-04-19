package hr.bioinfo.swj.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class RenderingInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {

       // log.debug("Handler: {}", handler);
        if (modelAndView != null && handler instanceof HandlerMethod hm) {
            // puni paketsko ime ili samo simpleName
            String controllerClass = hm.getBeanType().getSimpleName();
            String methodName = hm.getMethod().getName();

            // Dohvati informacije o klasi i metodi
            String className = hm.getBeanType().getName();

            String sourceFile = controllerClass + ".java";

            ControllerInfo info = new ControllerInfo(className, methodName, sourceFile, getLineNumber(hm));
            modelAndView.addObject("controllerInfo", info);
        }
    }

    private int getLineNumber(HandlerMethod hm) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement el : stack) {
            if (el.getClassName().equals(hm.getBeanType().getName())
                    && el.getMethodName().equals(hm.getMethod().getName())) {

                log.debug("StackTraceElement: {}", el);
                return el.getLineNumber();
            }
        }
        return -1;
    }
}
