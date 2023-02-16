package dailyplanner;

import dailyplanner.exceptions.TaskNotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private final Map<Integer, Task> taskMap;
    private final Collection<Task> removedTasks;

    public TaskService() {
        taskMap = new TreeMap<>();
        removedTasks = new ArrayList<>();
    }

    public void add(Task task) {
        taskMap.put(task.getId(), task);
    }

    public Task remove(int id) throws TaskNotFoundException {
        if (taskMap.containsKey(id)) {
            Task removedTask = taskMap.remove(id);
            removedTasks.add(removedTask);
            return removedTask;
        } else {
            throw new TaskNotFoundException(String.format("Task with %d not found", id));
        }
    }

    public Task updateTitle(int id, String newTitle) throws TaskNotFoundException {
        Task editTask = getTaskById(id);
        editTask.setTitle(newTitle);
        return editTask;
    }

    public Task updateDescription(int id, String newDescription) throws TaskNotFoundException {
        Task editTask = getTaskById(id);
        editTask.setDescription(newDescription);
        return editTask;
    }

    public Map<LocalDate, Collection<Task>> getAllGroupByDate(LocalDate endDate) {
        Map<LocalDate, Collection<Task>> resultMap = new TreeMap<>();
        LocalDate localDate = LocalDate.now();
        while (!localDate.isAfter(endDate)) {
            Collection<Task> tasks = getAllByDate(localDate);
            if (!tasks.isEmpty()) {
                resultMap.put(localDate, tasks);
            }
            localDate = localDate.plusDays(1);
        }
        return resultMap;
    }

    public Collection<Task> getRemovedTasks() {
        return Collections.unmodifiableCollection(removedTasks);
    }

    public Collection<Task> getAllActiveTasks() {
        return Collections.unmodifiableCollection(taskMap.values());
    }

    public Collection<Task> getAllByDate(LocalDate localDate) {
        return taskMap.values().stream().filter((Task task) ->
                task.appearsIn(localDate)).
                sorted(Comparator.comparing(o -> o.getDateTime().toLocalTime())).
                collect(Collectors.toList());
    }

    protected Task getTaskById(int id) throws TaskNotFoundException {
        if (taskMap.containsKey(id)) {
            return taskMap.get(id);
        } else {
            throw new TaskNotFoundException(String.format("Task with %d not found", id));
        }
    }
}
