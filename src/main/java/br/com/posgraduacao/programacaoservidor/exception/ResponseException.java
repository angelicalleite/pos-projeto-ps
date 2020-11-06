package br.com.posgraduacao.programacaoservidor.exception;

import java.util.Arrays;
import java.util.List;

public interface ResponseException extends RestResponse {

    default MessageError message(int status, int code, String path, String message, List<String> errors) {
        return MessageError.builder()
                .code(code)
                .path(path)
                .message(message)
                .status(status)
                .errors(errors)
                .build();
    }

    default MessageError message(int status, int code, String path, String message, String... errors) {
        return MessageError.builder()
                .code(code)
                .path(path)
                .message(message)
                .status(status)
                .errors(Arrays.asList(errors))
                .build();
    }
}
