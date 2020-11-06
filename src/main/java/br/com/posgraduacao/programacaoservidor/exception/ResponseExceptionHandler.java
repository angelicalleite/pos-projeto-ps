package br.com.posgraduacao.programacaoservidor.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Order(value = 1)
@ControllerAdvice
public class ResponseExceptionHandler implements ResponseException {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest http) {
        return badRequest(message(400, 1001, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, final HttpServletRequest http) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));

        ex.getBindingResult()
                .getGlobalErrors()
                .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        return badRequest(message(400, 1002, http.getRequestURI(), ex.getMessage(), errors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest http) {
        return badRequest(message(400, 1003, http.getRequestURI(), ex.getMessage(), ex.getParameterName() + " param not informed"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest http) {
        return methodNotAllowed(message(405, 1004, http.getRequestURI(), ex.getMessage(), ex.getMethod() + " method not supported for this request"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest http) {
        StringBuilder error = new StringBuilder()
                .append(ex.getContentType()).append(" type media not support. Media support is: ");

        ex.getSupportedMediaTypes()
                .forEach(e -> error.append(e).append(", "));

        return unsupportedMediaType(message(415, 1005, http.getRequestURI(), ex.getMessage(), error.substring(0, error.length() - 2)));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex, HttpServletRequest http) {
        return notFound(message(406, 1009, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest http) {
        String error = ex.getName() + " must be the type " + ex.getRequiredType().getName();

        return badRequest(message(400, 1007, http.getRequestURI(), ex.getMessage(), error));
    }

    @ExceptionHandler(ExceptionInInitializerError.class)
    public ResponseEntity handleExceptionInternal(ExceptionInInitializerError ex, HttpServletRequest http) {
        return internalServerError(message(500, 1008, http.getRequestURI(), ex.getMessage()));
    }
}