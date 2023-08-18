package org.aibles.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor(staticName = "of")
@Data
@NoArgsConstructor
public class Response<T> {
  private int status;
  private String statusMessage;
  private T data;
  private String timestamp;

  public static <T> Response<T> of(int status, String statusMessage, T data) {
    return Response.of(status, statusMessage, data, String.valueOf(LocalDateTime.now()));
  }

  public static <T> Response<T> of(int status, String statusMessage) {
    return Response.of(status, statusMessage, null, String.valueOf(LocalDateTime.now()));
  }
}