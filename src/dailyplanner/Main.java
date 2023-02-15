package dailyplanner;

import dailyplanner.exceptions.TaskNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;

import static dailyplanner.utils.TaskUtils.getAllEnumNamesForDialog;
import static dailyplanner.utils.TaskUtils.getEditActionById;

public class Main {

    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DATE_FORMAT_WITH_DAY = "dd.MM.yyyy (E)";
    private static final String TIME_FORMAT = "HH:mm";

    private static final int START_CHOICE_VALUE = 1;
    private static final String ENTER_DIALOG_ACTION = "Выберите действие (введите число): ";
    private static final String[] DIALOG_ACTIONS = getAllEnumNamesForDialog(MainMenuAction.class);

    private static final String ENTER_TASK_TITLE = "Введите заголовок задачи: ";
    private static final String ENTER_TASK_NEW_TITLE = "Введите новый заголовок задачи: ";
    private static final String ERROR_TASK_TITLE = "Ошибка: заголовок задачи не должен быть пустым!";
    private static final String ENTER_TASK_DESC = "Введите описание задачи: ";

    private static final String ENTER_TASK_NEW_DESC = "Введите новое описание задачи: ";
    private static final String ERROR_TASK_DESC = "Ошибка: описание задачи не должно быть пустым!";

    private static final String ENTER_NEW_TASK_DATE =
            String.format("Введите дату начала задачи в формате %s: ", DATE_FORMAT);
    private static final String ENTER_CHECK_TASK_DATE =
            String.format("Введите дату для поиска задач в формате %s: ", DATE_FORMAT);
    private static final String ENTER_END_TASK_DATE =
            String.format("Введите конечную дату для поиска всех задач в формате %s: ", DATE_FORMAT);
    private static final String ERROR_TASK_DATE_PAST = "Ошибка: дата задачи не должна быть в прошлом!";
    private static final String ERROR_TASK_DATE_EMPTY = "Ошибка: дата задачи не должна быть пустой!";

    private static final String ENTER_TASK_TIME =
            String.format("Введите время начала задачи в формате %s: ", TIME_FORMAT);
    private static final String ERROR_TASK_TIME_PAST = "Ошибка: время задачи не должно быть в прошлом!";
    private static final String ERROR_TASK_TIME_EMPTY = "Ошибка: время задачи не должно быть пустой!";

    private static final String ENTER_TASK_TYPE = "Введите тип задачи: ";
    private static final String[] TASK_TYPES = getAllEnumNamesForDialog(TaskType.class);

    private static final String ENTER_TASK_FREQ = "Введите повторяемость задачи: ";
    private static final String[] TASK_FREQ = getAllEnumNamesForDialog(Repeatability.class);

    private static final String SELECT_TASK_PART = "Выберите параметр задачи для редактирования:";
    private static final String[] DIALOG_SELECT_TASK_PART_EDIT = getAllEnumNamesForDialog(EditAction.class);

    private static final String YOUR_CHOICE = "Ваш выбор: ";
    private static final String ACTIVE_LIST_EMPTY_MESSAGE = "Список активных задач пуст!";
    private static final String REMOVED_LIST_EMPTY_MESSAGE = "Список удаленных задач пуст!";
    private static final String CHECK_DATE_LIST_EMPTY_MESSAGE = "Список задач на указанный день %s - пуст";

    private static final String ACTIVE_LIST_READY_MESSAGE = "Список всех активных задач:";
    private static final String REMOVED_LIST_READY_MESSAGE = "Список всех удаленных задач:";
    private static final String CHECK_DATE_LIST_READY_MESSAGE = "Список всех задач на указанный день %s:";

    private static final String ENTER_ID_TASK_FOR_EDIT_MESSAGE = "Введите id задачи для редактирования: ";

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Ежедневник!");
        Scanner scanner = new Scanner(System.in);
        TaskService taskService = new TaskService();

        boolean isContinue = true;
        while (isContinue) {
            MainMenuAction dialogAction = readMainDialogAction(scanner);

            switch (dialogAction) {
                case ADD_TASK_ACTION:
                    Task newTask = createTaskByDialog(scanner);
                    System.out.println("Создана задача: " + newTask);
                    taskService.add(newTask);
                    break;
                case GET_TASKS_BY_DATE:
                    LocalDate dateCheck = readTaskDate(scanner, ENTER_CHECK_TASK_DATE);
                    String s = dateCheck.format(DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_DAY));
                    printAllTasksFromCollection(taskService.getAllByDate(dateCheck),
                            String.format(CHECK_DATE_LIST_EMPTY_MESSAGE, s),
                            String.format(CHECK_DATE_LIST_READY_MESSAGE, s));
                    break;
                case GET_ALL_GROUP_TASK_BEFORE_DATE:
                    LocalDate endDate = readTaskDate(scanner, ENTER_END_TASK_DATE);
                    taskService.getAllGroupByDate(endDate).forEach((date, tasks) -> {
                        System.out.println(date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_DAY)));
                        tasks.forEach((task) -> System.out.println("\t" + task));
                    });
                    break;
                case GET_ALL_ACTIVE_TASKS:
                    printAllTasksFromCollection(taskService.getAllActiveTasks(),
                            ACTIVE_LIST_EMPTY_MESSAGE, ACTIVE_LIST_READY_MESSAGE);
                    break;
                case GET_ALL_REMOVED_TASKS:
                    printAllTasksFromCollection(taskService.getRemovedTasks(),
                            REMOVED_LIST_EMPTY_MESSAGE, REMOVED_LIST_READY_MESSAGE);
                    break;
                case REMOVE_TASK_BY_ID:
                    removeTaskById(scanner, taskService);
                    break;
                case EDIT_TASK_BY_ID:
                    editTaskById(scanner, taskService);
                    break;
                case ACTION_EXIT:
                    isContinue = false;
                    System.out.println("Завершаем работу...");
                    break;
            }
        }
        scanner.close();
    }

    private static Task createTaskByDialog(Scanner scanner) {
        System.out.println();
        System.out.println("Добавление новой задачи...");

        String taskTitle = readString(scanner, ENTER_TASK_TITLE, ERROR_TASK_TITLE);
        String taskDescription = readString(scanner, ENTER_TASK_DESC, ERROR_TASK_DESC);
        TaskType taskType = readTaskType(scanner);

        LocalDate taskDate = readTaskDate(scanner, ENTER_NEW_TASK_DATE);
        LocalTime taskTime = readTaskTime(scanner, taskDate);
        LocalDateTime taskDateTime = LocalDateTime.of(taskDate, taskTime);

        Repeatability taskRepeatability = readTaskRepeatability(scanner);

        switch (taskRepeatability) {
            case ONE_TIME:
                return new OneTimeTask(taskTitle, taskType, taskDescription, taskDateTime);
            case DAILY:
                return new DailyTask(taskTitle, taskType, taskDescription, taskDateTime);
            case WEEKLY:
                return new WeeklyTask(taskTitle, taskType, taskDescription, taskDateTime);
            case MONTHLY:
                return new MonthlyTask(taskTitle, taskType, taskDescription, taskDateTime);
            case YEARLY:
                return new YearlyTask(taskTitle, taskType, taskDescription, taskDateTime);
            default:
                return null;
        }
    }

    private static void printAllTasksFromCollection(Collection<Task> tasks,
                                                    String emptyListMessage, String readyListMessage) {
        System.out.println();
        if (tasks.isEmpty()) {
            System.out.println(emptyListMessage);
        } else {
            System.out.println(readyListMessage);
            tasks.forEach(System.out::println);
        }
    }

    private static void removeTaskById(Scanner scanner, TaskService taskService) {
        System.out.println();
        System.out.print("Введите id задачи для удаления: ");
        int removeId = 0;
        String answer = scanner.nextLine();
        try {
            removeId = Integer.parseInt(answer);
            Task removedTask = taskService.remove(removeId);
            System.out.println("Успешно удалена: " + removedTask);
        } catch (NumberFormatException e) {
            System.out.printf("Указанное значение: %s - не является числом!\n", answer);
        } catch (TaskNotFoundException e) {
            System.out.printf("Задача с указанным id = %d не найдена!\n", removeId);
        }
    }

    private static void editTaskById(Scanner scanner, TaskService taskService) {
        Task editTask = getTaskByDialog(scanner, taskService);
        System.out.println("Задача найдена...");
        System.out.println(editTask);

        EditAction editAction = readEditTaskActions(scanner);
        switch (editAction) {
            case EDIT_TASK_TITLE:
                System.out.println("Имеющийся заголовок задачи: " + editTask.getTitle());
                String newTitle = readString(scanner, ENTER_TASK_NEW_TITLE, ERROR_TASK_TITLE);
                editTask.setTitle(newTitle);
                System.out.println(editTask);
                System.out.println("Заголовок задачи успешно изменен");
                break;
            case EDIT_TASK_DESCRIPTION:
                System.out.println("Актуальное описание задачи: " + editTask.getDescription());
                String newDescription = readString(scanner, ENTER_TASK_NEW_DESC, ERROR_TASK_DESC);
                editTask.setDescription(newDescription);
                System.out.println("Описание задачи успешно изменено");
                System.out.println(editTask);
                break;
        }
    }

    private static String readString(Scanner scanner, String text, String errorMessage) {
        String stringPart;
        boolean isEmpty;
        do {
            System.out.print(text);
            stringPart = scanner.nextLine();
            isEmpty = stringPart == null || stringPart.isEmpty() || stringPart.isBlank();
            if (isEmpty) {
                System.out.println(errorMessage);
            }
        } while (isEmpty);
        return stringPart;
    }

    private static LocalDate readTaskDate(Scanner scanner, String message) {
        String stringDate;
        LocalDate localDate = null;
        boolean isNeedRead;
        do {
            System.out.print(message);
            stringDate = scanner.nextLine();
            isNeedRead = stringDate == null || stringDate.isEmpty() || stringDate.isBlank();
            if (isNeedRead) {
                System.out.println(ERROR_TASK_DATE_EMPTY);
            } else {
                try {
                    localDate = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
                    if (localDate.isBefore(LocalDate.now())) {
                        System.out.println(ERROR_TASK_DATE_PAST);
                        isNeedRead = true;
                    }
                } catch (DateTimeParseException e) {
                    System.out.printf("Указанное значение: %s - не является датой!\n", stringDate);
                    isNeedRead = true;
                }
            }
        } while (isNeedRead);
        return localDate;
    }

    private static LocalTime readTaskTime(Scanner scanner, LocalDate taskDate) {
        String stringTime;
        LocalTime taskTime = null;
        boolean isNeedRead;
        do {
            System.out.print(ENTER_TASK_TIME);
            stringTime = scanner.nextLine();
            isNeedRead = stringTime == null || stringTime.isEmpty() || stringTime.isBlank();
            if (isNeedRead) {
                System.out.println(ERROR_TASK_TIME_EMPTY);
            } else {
                try {
                    taskTime = LocalTime.parse(stringTime, DateTimeFormatter.ofPattern(TIME_FORMAT));
                    LocalDateTime taskDateTime = LocalDateTime.of(taskDate, taskTime);
                    if (taskDateTime.isBefore(LocalDateTime.now())) {
                        System.out.println(ERROR_TASK_TIME_PAST);
                        isNeedRead = true;
                    }
                } catch (DateTimeParseException e) {
                    System.out.printf("Указанное значение: %s - не является временем!\n", stringTime);
                    isNeedRead = true;
                }
            }
        } while (isNeedRead);
        return taskTime;
    }

    private static TaskType readTaskType(Scanner scanner) {
        String message = combineTextMessageVariants(ENTER_TASK_TYPE, TASK_TYPES);
        int idTaskType = readIntByDialog(scanner, message, TASK_TYPES.length);
        return getEditActionById(TaskType.class, idTaskType);
    }

    private static Repeatability readTaskRepeatability(Scanner scanner) {
        String message = combineTextMessageVariants(ENTER_TASK_FREQ, TASK_FREQ);
        int idRepeatability = readIntByDialog(scanner, message, TASK_FREQ.length);
        return getEditActionById(Repeatability.class, idRepeatability);
    }

    private static EditAction readEditTaskActions(Scanner scanner) {
        String message = combineTextMessageVariants(SELECT_TASK_PART, DIALOG_SELECT_TASK_PART_EDIT);
        int idEditAction = readIntByDialog(scanner, message, DIALOG_SELECT_TASK_PART_EDIT.length);
        return getEditActionById(EditAction.class, idEditAction);
    }

    private static MainMenuAction readMainDialogAction(Scanner scanner) {
        String message = combineTextMessageVariants(ENTER_DIALOG_ACTION, DIALOG_ACTIONS);
        int idDialogAction = readIntByDialog(scanner, message, DIALOG_ACTIONS.length);
        return getEditActionById(MainMenuAction.class, idDialogAction);
    }

    private static int readIntByDialog(Scanner scanner, String choiceMessage, int maxValue) {
        boolean isCorrect = false;
        int intResult = 0;
        do {
            System.out.println();
            System.out.print(choiceMessage);
            String answer = scanner.nextLine();
            try {
                intResult = Integer.parseInt(answer);
                isCorrect = intResult >= START_CHOICE_VALUE && intResult <= maxValue;
                if (!isCorrect) {
                    System.out.printf("Неверный вариант выбора: %d, должно быть значение от %d до %d\n",
                            intResult, START_CHOICE_VALUE, maxValue);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Указанное значение: %s - не является числом!\n", answer);
            }
        } while (!isCorrect);
        return intResult;
    }

    private static String combineTextMessageVariants(String message, String[] variants) {
        StringBuilder sb = new StringBuilder(message);
        for (String variant : variants) {
            sb.append("\n").append(variant);
        }
        sb.append("\n").append(YOUR_CHOICE);
        return sb.toString();
    }

    private static Task getTaskByDialog(Scanner scanner, TaskService taskService) {
        boolean isCorrect = false;
        Task task = null;
        do {
            System.out.println();
            System.out.print(ENTER_ID_TASK_FOR_EDIT_MESSAGE);
            int taskId = 0;
            String answer = scanner.nextLine();
            try {
                taskId = Integer.parseInt(answer);
                task = taskService.getTaskById(taskId);
                isCorrect = true;
            } catch (NumberFormatException e) {
                System.out.printf("Указанное значение: %s - не является числом!\n", answer);
            } catch (TaskNotFoundException e) {
                System.out.printf("Задача с указанным id = %d не найдена!\n", taskId);
            }
        } while (!isCorrect);
        return task;
    }
}
