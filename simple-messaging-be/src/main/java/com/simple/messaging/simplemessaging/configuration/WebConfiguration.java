package com.simple.messaging.simplemessaging.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@Configuration
public class WebConfiguration {

  @Bean
  public CorsWebFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod(RequestMethod.OPTIONS.name());
    config.addAllowedMethod(RequestMethod.HEAD.name());
    config.addAllowedMethod(RequestMethod.GET.name());
    config.addAllowedMethod(RequestMethod.PUT.name());
    config.addAllowedMethod(RequestMethod.POST.name());
    config.addAllowedMethod(RequestMethod.DELETE.name());
    config.addAllowedMethod(RequestMethod.PATCH.name());
    source.registerCorsConfiguration("/**", config);

    return new CorsWebFilter(source);
  }
}
