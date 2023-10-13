package com.multipolar.bootcamp.gatewayexercise.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLog {
    private String requestMethod;
    private String requestUri;
    private Integer responseStatusCode;
    private LocalDateTime timeStamp;
    private String content;
}
