package net.ivanzykov.craftworkstasktracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TaskFieldsMissingAdvice {

    @ResponseBody
    @ExceptionHandler(TaskFieldsMissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String taskFieldMissingAdvice(TaskFieldsMissingException ex) {
        return ex.getMessage();
    }
}
