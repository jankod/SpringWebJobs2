package hr.bioinfo.swj.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
public class TemplatePathHelper {

    private final ResourceLoader resourceLoader;
    private final String prefix;
    private final String suffix;

    public TemplatePathHelper(ResourceLoader resourceLoader,
                              @Value ("${spring.thymeleaf.prefix:classpath:/templates/}") String prefix,
                              @Value ("${spring.thymeleaf.suffix:.html}") String suffix) {
        this.resourceLoader = resourceLoader;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getTemplatePath(String templateName) {
        try {
            String resourcePath = prefix + templateName + suffix;
            Resource resource = resourceLoader.getResource(resourcePath);

            if (resource.exists()) {
                try {
                    return resource.getFile().getAbsolutePath();
                } catch (IOException e) {
                    return resourcePath + " (unutar JAR-a ili WAR-a)";
                }
            } else {
                return resourcePath + " (nije pronađen)";
            }
        } catch (Exception e) {
            return "Greška pri traženju putanje: " + e.getMessage();
        }
    }

    public String getProjectRoot() {
        try {
            // Pokušaj pronaći korijen projekta
            Resource resource = resourceLoader.getResource("classpath:");
            return resource.getFile().getParentFile().getParentFile().getParentFile().getAbsolutePath();
        } catch (Exception e) {
            // Fallback - koristite putanju konfiguracije
            return System.getProperty("user.dir");
        }
    }

    public String getControllerIdeLink(ControllerInfo info) {
        return "idea://open?file=" + getSrcPath() + "/main/java/"
                + info.getClassName().replace('.', '/') + ".java" + "&line=" + info.getLineNumber();
    }

    public String getControllerIdeLink(String className, int lineNumber) {
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        return "idea://open?file=" + getProjectRoot() + "/src/main/java/"
                + className.replace('.', '/') + ".java&line=" + lineNumber;
    }

    public String getSrcPath() {
        try {
            // 1. Pokušaj s ClassLoader-om
            URL resource = getClass().getProtectionDomain().getCodeSource().getLocation();
            File classPathRoot = new File(resource.toURI());

            // Heuristika: Ako završava s /target/classes, popnimo se dva nivoa i dodajmo /src
            if (classPathRoot.getAbsolutePath().endsWith("/target/classes")) {
                File projectRoot = classPathRoot.getParentFile().getParentFile();
                File srcDir = new File(projectRoot, "src");

                if (srcDir.exists() && srcDir.isDirectory()) {
                    return srcDir.getAbsolutePath();
                }
            }

            // 2. Pokušaj s ApplicationHome
            ApplicationHome home = new ApplicationHome(getClass());
            File projectRoot = home.getDir();

            if (projectRoot.getName().equals("target")) {
                projectRoot = projectRoot.getParentFile();
            }

            File srcDir = new File(projectRoot, "src");
            if (srcDir.exists() && srcDir.isDirectory()) {
                return srcDir.getAbsolutePath();
            }

            // 3. Pokušaj s user.dir
            srcDir = new File(System.getProperty("user.dir"), "src");
            if (srcDir.exists() && srcDir.isDirectory()) {
                return srcDir.getAbsolutePath();
            }
        } catch (Exception e) {
            // Log greške
        }

        // Fallback
        return System.getProperty("user.dir") + "/src";
    }


}