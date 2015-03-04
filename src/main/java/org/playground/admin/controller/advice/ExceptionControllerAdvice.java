package org.playground.admin.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by wsantasiero on 9/11/14.
 */
@ControllerAdvice(annotations=RestController.class)
public class ExceptionControllerAdvice {

    Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> getSQLError(Exception exception){
        exception.printStackTrace();
        logger.debug("An error occurred ", exception);
        HttpHeaders headers = new HttpHeaders();
        headers.set("HeaderKey","HeaderDetails");
        return new ResponseEntity<String>("An unexpected error has occurred.",headers,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
