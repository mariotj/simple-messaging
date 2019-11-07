package com.simple.messaging.simplemessaging.rest.web.controller;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.simple.messaging.simplemessaging.entity.common.BaseResponse;
import com.simple.messaging.simplemessaging.entity.dao.MessageData;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketEndpointIT {

  @Value("${local.server.port}")
  private int port;

  private String url;

  private CompletableFuture<BaseResponse<MessageData>> completableFuture;

  @Autowired
  private SimpMessagingTemplate template;

  private static final String SUBSCRIBE_MESSAGE = "/topic/message";

  @Before
  public void setup() {
    completableFuture = new CompletableFuture<>();
    url = "ws://localhost:" + port + "/message-ws";
  }

  private BaseResponse<MessageData> construct(MessageData message) {
    return BaseResponse.construct(
        HttpStatus.OK.name(),
        HttpStatus.OK.getReasonPhrase(),
        message,
        null
    );
  }

  @Test
  public void testSubscribeMessage() throws InterruptedException, ExecutionException, TimeoutException {
    WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    StompSession stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
    }).get(1, SECONDS);

    stompSession.subscribe(SUBSCRIBE_MESSAGE, new CreateMessageFrameHandler());

    String message = "test subscribe message via ws";
    MessageData messageData = new MessageData(message);
    template.convertAndSend(SUBSCRIBE_MESSAGE, construct(messageData));

    BaseResponse<MessageData> baseResponse = completableFuture.get(5, SECONDS);

    Assert.assertNotNull(baseResponse);
    Assert.assertNotNull(baseResponse.getData());
  }

  private List<Transport> createTransportClient() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    return transports;
  }

  private class CreateMessageFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      return BaseResponse.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      completableFuture.complete((BaseResponse<MessageData>) o);
    }
  }
}
