package net.ivanzykov.craftworkstasktracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TaskDateTimeParseAdvice {

    @ResponseBody
    @ExceptionHandler(TaskDateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String taskDateTimeParseAdvice(TaskDateTimeParseException ex) {
        return ex.getMessage();
    }

}
