package com.fl.service.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class FootballException extends RuntimeException{
    private static final Logger LOGGER = LoggerFactory.getLogger(FootballException.class);

    public FootballException(String errorMessage) {
        super(errorMessage);
    }

    public FootballException(String errorMessage, Object... arguments) {
        super(buildDefaultMessage(errorMessage, arguments));
    }

    private static String buildDefaultMessage(String errorMessage, Object... arguments) {
        String error = MessageFormat.format(errorMessage, arguments);
        LOGGER.error("Exception message : {}", error);
        return error;
    }
}
