package dailyplanner;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OneTimeTask extends Task {

    public OneTimeTask(String title, TaskType taskType, String description, LocalDateTime localDateTime) {
        super(title, taskType, description, localDateTime);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        return localDate.isEqual(getDateTime().toLocalDate());
    }

    @Override
    public String toString() {
        return "Однократная задача " + super.toString();
    }
}
