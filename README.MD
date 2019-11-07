# Simple Messaging

Technologies:
 * Spring Boot 2 (Using <b>spring-boot-starter-webflux</b>, <b>spring-boot-starter-websocket</b>)

Dependencies:
 * Java 8 (required)
 
## Install using default profile
```
mvn -U clean install
```

## Run application
Run java application with specific version from project directory
```
mvn spring-boot:run
```

Run java application with specific version from jar
```
java -jar target/simple-messaging-<version>.jar
```

# API Documentation
There are 3 API in this project, 2 REST API and 1 Websocket endpoint.

## 1. Submit Message API
  Endpoint for submit message with method ```POST```
  ```
  /simple-messaging/v1/messages
  ```
  This endpoint must include request in body for the message like
  ```
  { "message" : "your desire message}
  ```
  Expected response
  ```
  {
    "code": "OK",
    "status": "OK",
    "data": {
      "message": "some message"
    },
    "error": null,
    "time": "2019-11-07T03:14:38.024+0000"
  }
  ```
  Message that have been submitted will send to subscription ```/topic/message``` to be consumed by websocket

## 2. Collect Message API
  Endpoint for collect message that has been submitted, using method ```GET```
  ```
  /simple-messaging/v1/messages/collect
  ```
  Expected response
  ```
  {
    "code": "OK",
    "status": "OK",
    "data": [
      {
        "message": "some message"
      },
      {
        "message": "from another one"
      }
    ],
    "error": null,
    "time": "2019-11-07T04:09:03.938+0000"
  }
  ```
  You will received all message that have been submitted.
  
 ## 3. Message Data Websocket API
   Connect websocket with this url
   ```
   ws://{host}:{port}/message-ws
   ```
   Create subscription for 
   ```
   /topic/message
   ```
   Message that will be received only message that submit after subscription created.