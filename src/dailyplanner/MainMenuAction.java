package dailyplanner;

public enum MainMenuAction {
    ADD_TASK_ACTION("Добавить новую задачу"),
    GET_TASKS_BY_DATE("Получить задачи на день"),
    GET_ALL_GROUP_TASK_BEFORE_DATE("Получить все задачи, сгруппированные по датам"),
    GET_ALL_ACTIVE_TASKS("Получить все активные задачи"),
    GET_ALL_REMOVED_TASKS("Получить все удаленные задачи"),
    REMOVE_TASK_BY_ID("Удалить задачу по id"),
    EDIT_TASK_BY_ID("Редактировать задачу по id"),
    ACTION_EXIT("Выход");

    private final String name;

    MainMenuAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
