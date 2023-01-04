package net.ivanzykov.craftworkstasktracker;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents Task entity for external clients and converts time to client's or db's time zone.
 */
public class TaskDto {

    private Long id;
    private String createdAt;
    private String updatedAt;
    private String dueDate;
    private String resolvedAt;
    private String title;
    private String description;
    private String priority;
    private String status;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm zz");
    private ZoneId timezone;

    public TaskDto() {
        // Required by Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Converts all time-related fields to the time zone of the client.
     *
     * @param createdAt     offsetDateTime object when the task was created
     * @param updatedAt     offsetDateTime object when the task was last updated
     * @param dueDate       offsetDateTime object when the task is expected to be done
     * @param resolvedAt    offsetDateTime object when the task was done
     * @param timezone      zoneId object of the client's timezone
     */
    public void setAllDatesConverted(OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime dueDate,
                                     OffsetDateTime resolvedAt, ZoneId timezone) {
        this.timezone = timezone;
        setCreatedAtConverted(createdAt);
        setUpdatedAtConverted(updatedAt);
        setDueDateConverted(dueDate);
        setResolvedAtConverted(resolvedAt);
    }

    /**
     * Converts the date when the task was created to the client's time zone, formats it to string and updates the
     * corresponding field of this object.
     *
     * @param createdAt offsetDateTime object the task was created
     */
    public void setCreatedAtConverted(OffsetDateTime createdAt) {
        this.createdAt = convertAndFormatDate(createdAt);
    }

    /**
     * Converts the date when the task was last updated to the client's time zone, formats it to string and updates the
     * corresponding field of this object.
     *
     * @param updatedAt offsetDateTime object the task was last updated
     */
    public void setUpdatedAtConverted(OffsetDateTime updatedAt) {
        this.updatedAt = convertAndFormatDate(updatedAt);
    }

    /**
     * Converts the date when the task is expected to be done to the client's time zone, formats it to string and
     * updates the corresponding field of this object
     *
     * @param dueDate   offsetDateTime object when the task is expected to be done
     */
    public void setDueDateConverted(OffsetDateTime dueDate) {
        this.dueDate = convertAndFormatDate(dueDate);
    }

    /**
     * Converts the date when the task was done to the client's time zone, formats it to string and updates the
     * corresponding field of this object.
     *
     * @param resolvedAt    offsetDateTime object when the task was done
     */
    public void setResolvedAtConverted(OffsetDateTime resolvedAt) {
        // resolvedAt allowed to be null
        if (resolvedAt != null) {
            this.resolvedAt = convertAndFormatDate(resolvedAt);
        }
    }

    private String convertAndFormatDate(OffsetDateTime date) {
        return date.atZoneSameInstant(timezone)
                .format(dateTimeFormatter);
    }

    // TODO: add methods returning dates converted to DB's time zone
}
