package com.simple.messaging.simplemessaging.rest.web.component;

import org.mapstruct.factory.Mappers;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
public class WebMapper {

  public static final WebBeanMapper BEAN_MAPPER = Mappers.getMapper(WebBeanMapper.class);

  private WebMapper() {
    // hide
  }
}
