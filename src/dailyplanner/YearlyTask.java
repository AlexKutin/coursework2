package dailyplanner;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class YearlyTask extends Task {

    public YearlyTask(String title, TaskType taskType, String description, LocalDateTime localDateTime) {
        super(title, taskType, description, localDateTime);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        LocalDate dateTask = getDateTime().toLocalDate();
        if (localDate.isBefore(dateTask)) {
            return false;
        }
        return localDate.getDayOfMonth() == dateTask.getDayOfMonth() &&
               localDate.getMonth() == dateTask.getMonth();
    }

    @Override
    public String toString() {
        return "Ежегодная задача " + super.toString();
    }
}
