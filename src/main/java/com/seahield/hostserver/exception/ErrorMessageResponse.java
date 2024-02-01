package com.seahield.hostserver.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorMessageResponse {
    private String message;
}
