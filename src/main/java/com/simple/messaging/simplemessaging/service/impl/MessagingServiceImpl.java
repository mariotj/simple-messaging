package com.simple.messaging.simplemessaging.service.impl;

import com.simple.messaging.simplemessaging.entity.dao.MessageData;
import com.simple.messaging.simplemessaging.service.api.MessagingService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@Service
@Slf4j
public class MessagingServiceImpl implements MessagingService {

  private List<MessageData> messageData = new ArrayList<>();

  @Override
  public Mono<MessageData> submitMessage(MessageData message) {
    log.info("submit message data {}", message);
    return Mono.just(messageData.add(message))
        .map(r -> message);
  }

  @Override
  public Mono<List<MessageData>> collect() {
    return Mono.just(messageData);
  }
}
