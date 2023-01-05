package net.ivanzykov.craftworkstasktracker.exceptions;

import java.time.format.DateTimeFormatter;

public class TaskDateTimeParseException extends RuntimeException{

    public TaskDateTimeParseException(String message, DateTimeFormatter formatter) {
        super(message + ". Please supply dates in the following format: " + formatter.toString());
    }
}
