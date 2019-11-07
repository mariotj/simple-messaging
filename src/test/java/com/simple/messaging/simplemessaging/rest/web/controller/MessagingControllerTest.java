package com.simple.messaging.simplemessaging.rest.web.controller;

import com.simple.messaging.simplemessaging.entity.common.BaseResponse;
import com.simple.messaging.simplemessaging.entity.dao.MessageData;
import com.simple.messaging.simplemessaging.entity.web.request.MessageDataRequest;
import com.simple.messaging.simplemessaging.rest.web.constant.ApiPath;
import com.simple.messaging.simplemessaging.service.api.MessagingService;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
import reactor.test.StepVerifier;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessagingControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private MessagingService messagingService;

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

  @Test
  public void testCollectMessage() {
    String test1 = "test collect 1";
    String test2 = "test collect 2";
    StepVerifier.create(messagingService.submitMessage(new MessageData(test1)))
        .assertNext(data -> {
          Assert.assertNotNull(data);
          Assert.assertEquals(test1, data.getMessage());

          StepVerifier.create(messagingService.submitMessage(new MessageData(test2)))
              .assertNext(data2 -> {
                Assert.assertNotNull(data2);
                Assert.assertEquals(test2, data2.getMessage());

                EntityExchangeResult<BaseResponse<List<MessageData>>> entityExchangeResult =
                    webTestClient.get()
                        .uri(ApiPath.MESSAGES + ApiPath.COLLECT)
                        .accept(MediaType.ALL)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectBody(
                            new ParameterizedTypeReference<BaseResponse<List<MessageData>>>() {
                            }).returnResult();

                Assert.assertNotNull(entityExchangeResult);
                Assert.assertNotNull(entityExchangeResult.getResponseBody());

                BaseResponse<List<MessageData>> baseResponse = entityExchangeResult.getResponseBody();
                Assert.assertNotNull(baseResponse.getData());

                List<MessageData> dataList = baseResponse.getData();

                AtomicBoolean checkTest1 = new AtomicBoolean(false);
                for (MessageData m: dataList) {
                  if (m.getMessage().equals(test1)) {
                    checkTest1.set(true);
                    break;
                  }
                }

                AtomicBoolean checkTest2 = new AtomicBoolean(false);
                for (MessageData m: dataList) {
                  if (m.getMessage().equals(test2)) {
                    checkTest2.set(true);
                    break;
                  }
                }

                Assert.assertTrue(checkTest1.get());
                Assert.assertTrue(checkTest2.get());

              }).expectComplete().verify();
        }).expectComplete().verify();
  }
}
