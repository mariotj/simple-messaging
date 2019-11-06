package com.simple.messaging.simplemessaging.service.api;

import com.simple.messaging.simplemessaging.entity.dao.MessageData;
import reactor.core.publisher.Mono;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
public interface MessagingService {

  Mono<MessageData> submitMessage(MessageData message);
}
