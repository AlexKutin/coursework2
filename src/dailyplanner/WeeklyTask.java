package dailyplanner;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeeklyTask extends Task {

    public WeeklyTask(String title, TaskType taskType, String description, LocalDateTime localDateTime) {
        super(title, taskType, description, localDateTime);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        LocalDate dateTask = getDateTime().toLocalDate();
        if (localDate.isBefore(dateTask)) {
            return false;
        }
        return localDate.getDayOfWeek() == dateTask.getDayOfWeek();
    }

    @Override
    public String toString() {
        return "Еженедельная задача " + super.toString();
    }
}
