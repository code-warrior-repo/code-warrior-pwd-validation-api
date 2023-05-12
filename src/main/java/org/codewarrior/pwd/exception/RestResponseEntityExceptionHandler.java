package org.codewarrior.pwd.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This is exception handler class which catches and handles exceptions occurred in application
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
   

    /**
     * This method handles RuntimeException
     * @param ex
     * @param request
     * @return ResponseEntity - Returns status code 500 for RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
