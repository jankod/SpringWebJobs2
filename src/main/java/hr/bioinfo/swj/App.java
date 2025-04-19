package hr.bioinfo.swj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "hr.bioinfo.swj")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
