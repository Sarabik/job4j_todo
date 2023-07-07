package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping({"/", "/tasks"})
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String getAllTaskPage(Model model) {
        model.addAttribute("tasks", taskService.getAll());
        return "tasks/list";
    }

    @GetMapping("/notDone")
    public String getNotDoneTaskPage(Model model) {
        model.addAttribute("tasks", taskService.getByStatus(false));
        return "tasks/notDone";
    }

    @GetMapping("/done")
    public String getDoneTaskPage(Model model) {
        model.addAttribute("tasks", taskService.getByStatus(true));
        return "tasks/done";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, Model model) {
        try {
            task.setCreated(LocalDateTime.now());
            taskService.save(task);
            return "tasks/list";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{taskId}")
    public String getSelectedTaskPage(@PathVariable int taskId, Model model) {
        Optional<Task> optionalTask = taskService.getById(taskId);
        if (optionalTask.isEmpty()) {
            model.addAttribute("message", "Task is not found");
            return "errors/404";
        }
        model.addAttribute("selectedTask", optionalTask.get());
        return "tasks/one";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task, Model model) {
        try {
            System.out.println("**************");
            System.out.println(task);
            taskService.update(task);
            return "tasks/list";
        } catch (Exception e) {
            model.addAttribute("message", "Task update failed");
            return "errors/404";
        }
    }

    @PostMapping("/delete")
    public String deleteTask(Model model, @ModelAttribute Task task) {
        try {
            System.out.println("**************");
            System.out.println(task);
            taskService.deleteById(task.getId());
            return "tasks/list";
        } catch (Exception e) {
            model.addAttribute("message", "Task delete failed");
            return "errors/404";
        }
    }
}
