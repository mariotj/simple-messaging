package com.simple.messaging.simplemessaging.configuration;

import static springfox.documentation.builders.PathSelectors.regex;

import com.simple.messaging.simplemessaging.Main;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfiguration {

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Simple Messaging Rest APIs")
        .description("This page lists all the rest apis Simple Messaging")
        .version(Main.class.getPackage().getImplementationVersion())
        .build();
  }

  @Bean
  public Docket init() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors
            .basePackage("com.simple.messaging.simplemessaging.rest.web.controller"))
        .paths(regex("/.*"))
        .build();
  }
}
