package CacheSmartCatalog.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

@Configuration
public class AppConfig {


    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public HandlerExceptionResolver myExceptionResolver() {
        // Your custom HandlerExceptionResolver implementation
        return new SimpleMappingExceptionResolver();
    }

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
