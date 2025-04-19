package hr.bioinfo.swj.config;

import hr.bioinfo.swj.util.RenderingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // za sve controllere

//        registry.addInterceptor(new RenderingInterceptor())
//                .addPathPatterns("/**");
    }
}
