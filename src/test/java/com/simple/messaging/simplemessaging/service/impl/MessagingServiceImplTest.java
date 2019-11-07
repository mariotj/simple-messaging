package com.simple.messaging.simplemessaging.service.impl;

import com.simple.messaging.simplemessaging.entity.dao.MessageData;
import com.simple.messaging.simplemessaging.service.api.MessagingService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessagingServiceImplTest {

  @Autowired
  private MessagingService messagingService;

  @Test
  public void testSubmitMessage() {
    MessageData messageData = new MessageData("test service");

    StepVerifier.create(messagingService.submitMessage(messageData))
        .assertNext(data -> {
          Assert.assertNotNull(data);
          Assert.assertEquals(messageData.getMessage(), data.getMessage());
        }).expectComplete().verify();
  }

  @Test
  public void testCollectMessage() {
    MessageData messageData = new MessageData("test service collect");

    StepVerifier.create(messagingService.submitMessage(messageData))
        .assertNext(data -> {
          Assert.assertNotNull(data);
          Assert.assertEquals(messageData.getMessage(), data.getMessage());

          StepVerifier.create(messagingService.collect())
              .assertNext(dataList -> {
                Assert.assertNotNull(dataList);

                AtomicBoolean checkTest1 = new AtomicBoolean(false);
                for (MessageData m: dataList) {
                  if (m.getMessage().equals(messageData.getMessage())) {
                    checkTest1.set(true);
                    break;
                  }
                }

                Assert.assertTrue(checkTest1.get());
              }).expectComplete().verify();
        }).expectComplete().verify();
  }
}
