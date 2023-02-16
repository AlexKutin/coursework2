package dailyplanner;

public enum TaskType {
    PERSONAL("Личная"), WORK("Рабочая");

    private final String text;

    TaskType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
