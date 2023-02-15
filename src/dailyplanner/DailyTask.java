package dailyplanner;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyTask extends Task {

    public DailyTask(String title, TaskType taskType, String description, LocalDateTime localDateTime) {
        super(title, taskType, description, localDateTime);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        return !localDate.isBefore(getDateTime().toLocalDate());
    }

    @Override
    public String toString() {
        return "Ежедневная задача " + super.toString();
    }
}
