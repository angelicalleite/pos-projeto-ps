package br.com.posgraduacao.programacaoservidor.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MessageError {

    private int code;
    private int status;
    private String path;
    private String message;
    private List<String> errors;

}
