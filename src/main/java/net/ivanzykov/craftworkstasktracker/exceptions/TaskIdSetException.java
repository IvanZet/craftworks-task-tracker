package net.ivanzykov.craftworkstasktracker.exceptions;

public class TaskIdSetException extends RuntimeException {

    public TaskIdSetException() {
        super("Providing an ID when creating a new task is not supported. Remove ID from the body of your " +
                "request");
    }
}
