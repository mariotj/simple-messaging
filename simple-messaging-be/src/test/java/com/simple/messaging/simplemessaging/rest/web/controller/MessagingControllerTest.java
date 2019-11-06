package com.simple.messaging.simplemessaging.rest.web.controller;

import com.simple.messaging.simplemessaging.entity.common.BaseResponse;
import com.simple.messaging.simplemessaging.entity.dao.MessageData;
import com.simple.messaging.simplemessaging.entity.web.request.MessageDataRequest;
import com.simple.messaging.simplemessaging.rest.web.constant.ApiPath;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessagingControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testSubmitMessage() {
    MessageDataRequest request = new MessageDataRequest("test");

    EntityExchangeResult<BaseResponse<MessageData>> entityExchangeResult =
        webTestClient.post()
            .uri(ApiPath.MESSAGES)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.ALL)
            .body(Mono.just(request), MessageDataRequest.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(new ParameterizedTypeReference<BaseResponse<MessageData>>() {
            }).returnResult();

    Assert.assertNotNull(entityExchangeResult);
    Assert.assertNotNull(entityExchangeResult.getResponseBody());

    BaseResponse<MessageData> baseResponse = entityExchangeResult.getResponseBody();
    Assert.assertNotNull(baseResponse.getData());

    MessageData messageData = baseResponse.getData();
    Assert.assertEquals(request.getMessage(), messageData.getMessage());
  }
}
