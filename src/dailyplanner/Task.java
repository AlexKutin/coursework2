package dailyplanner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class Task {
    private static int idGenerator = 0;
    private final int id;
    private String title;
    private final TaskType taskType;
    private final LocalDateTime dateTime;
    private String description;

    public Task(String title, TaskType taskType, String description, LocalDateTime localDateTime) {
        this.id = ++idGenerator;
        this.title = title;
        this.taskType = taskType;
        this.dateTime = localDateTime;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskType getType() {
        return taskType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract boolean appearsIn(LocalDate localDate);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && taskType == task.taskType &&
                Objects.equals(dateTime, task.dateTime) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, taskType, dateTime, description);
    }

    @Override
    public String toString() {
        return String.format("{id = %d, Заголовок = '%s', Описание ='%s', Тип = '%s', Дата и время старта = '%s'}",
                id, title, description, taskType, dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy (E) HH:mm:ss")));
    }
}
