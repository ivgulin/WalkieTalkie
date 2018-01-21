package com.mokujin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@ComponentScan
@Slf4j
public class Application extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(System.getProperty("user.dir") + "src/main/resources/public/**").addResourceLocations(System.getProperty("user.dir") + "src/main/resources/public/");
    }

    public static void main(String[] args) {
        log.debug("'main() is running'");
        SpringApplication.run(Application.class, args);
    }
}
