package com.fl.service.exception;

import com.fl.service.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class FootballLeagueExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String CORRELATION_MDC_PROPERTY = "correlation";

    @ExceptionHandler(FootballException.class)
    public final ResponseEntity<ErrorDto> handle(FootballException ex) {
        ErrorDto errorDto = new ErrorDto(MDC.get(CORRELATION_MDC_PROPERTY), "ERROR", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Class<?> type = ex.getRequiredType();
        String typeName = "";
        if (type != null) {
            typeName = type.getTypeName();
        }
        ErrorDto errorDto = new ErrorDto(MDC.get(CORRELATION_MDC_PROPERTY), "TYPE_ERROR", ex.getName() + " should be of type " + typeName);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getRejectedValue() + " is invalid value for " + error.getField());
        }
        ErrorDto errorDto = new ErrorDto(MDC.get(CORRELATION_MDC_PROPERTY), "TYPE_ERROR",  String.join(",", errors));
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(MDC.get(CORRELATION_MDC_PROPERTY), "Invalid MediaType", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
                                                                          HttpStatus status, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(MDC.get(CORRELATION_MDC_PROPERTY), "MISSING_PARAMETER",
                ex.getParameterName() + " parameter is missing");
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        ErrorDto errorDto = new ErrorDto(MDC.get(CORRELATION_MDC_PROPERTY), "DENIED", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDto> handleAllExceptions(Exception ex) {
        // an exceptionHandler defined for ElasticsearchStatusException is not triggered, so it has to be handled her
        ErrorDto errorDto = new ErrorDto(MDC.get(CORRELATION_MDC_PROPERTY), "GENERIC_EXCEPTION",
                ex.getLocalizedMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
