package com.simple.messaging.simplemessaging.entity.common;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Mario T Juzar
 * @since : 07/11/19
 */
@Data
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

  private String code;
  private String status;
  private transient T data;
  private String error;
  private Date time;

  public static <T> BaseResponse<T> construct(String code, String status, T data, String error) {
    BaseResponse<T> response = new BaseResponse<>();
    response.setCode(code);
    response.setData(data);
    response.setStatus(status);
    response.setError(error);
    response.setTime(new Date());
    return response;
  }
}
