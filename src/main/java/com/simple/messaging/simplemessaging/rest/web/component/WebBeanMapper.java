package com.simple.messaging.simplemessaging.rest.web.component;

import com.simple.messaging.simplemessaging.entity.dao.MessageData;
import com.simple.messaging.simplemessaging.entity.web.request.MessageDataRequest;
import org.mapstruct.Mapper;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@Mapper
public interface WebBeanMapper {

  MessageData map(MessageDataRequest request);
}
