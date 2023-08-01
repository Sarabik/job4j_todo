package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;

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
        model.addAttribute("priorities", priorityService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, @RequestParam List<Integer> categoryList, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        task.setCreated(LocalDateTime.now());
        task.setUser(user);
        for (int categoryId : categoryList) {
            task.getCategories().add(categoryService.getById(categoryId).get());
        }
        taskService.save(task);
        return "redirect:/";
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

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id, Model model) {
        boolean isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Task is not deleted!");
            return "errors/404";
        }
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable int id, Model model) {
        Optional<Task> optionalTask = taskService.getById(id);
        if (optionalTask.isEmpty()) {
            model.addAttribute("message", "Task is not found!");
            return "errors/404";
        }
        model.addAttribute("priorities", priorityService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("selectedTask", optionalTask.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task, @RequestParam List<Integer> categoryList, Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        for (int categoryId : categoryList) {
            task.getCategories().add(categoryService.getById(categoryId).get());
        }
        task.setUser(user);
        boolean isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Task is not changed!");
            return "errors/404";
        }
        return "redirect:/";
    }

    @GetMapping("/statusToDone/{id}")
    public String statusToDone(@PathVariable int id, Model model) {
        boolean isUpdated = taskService.updateTaskStatus(id);
        if (!isUpdated) {
            model.addAttribute("message", "Task status is not changed!");
            return "errors/404";
        }
        return "redirect:/tasks/" + id;
    }

}
