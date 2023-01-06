package net.ivanzykov.craftworkstasktracker.exceptions;

public class TaskFieldsMissingException extends RuntimeException {

    public TaskFieldsMissingException(String message) {
        super("One or more mandatory fields of the task are missing in your request's body. See the following error " +
                "message for details." + System.lineSeparator() + message);
    }
}
