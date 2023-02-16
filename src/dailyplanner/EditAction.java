package dailyplanner;

public enum EditAction {
    EDIT_TASK_TITLE("Редактировать заголовок задачи"),
    EDIT_TASK_DESCRIPTION("Редактировать описание задачи");

    private final String name;

    EditAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
