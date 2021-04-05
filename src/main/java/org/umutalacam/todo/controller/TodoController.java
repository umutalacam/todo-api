package org.umutalacam.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.umutalacam.todo.data.entity.Todo;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.service.TodoService;

import java.util.List;

/**
 * Controller for To Do service endpoints.
 * Created by Umut Can Alaçam
 */
@RestController
public class TodoController {

    private TodoService todoService;
    private String currentUserId = "user::bgursoy";

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(path = "/todo", produces = "application/json")
    public List<Todo> getAllTodosForUser(){
        // Todo: Error handling
        // Todo: Filtered requests
        return this.todoService.getTodosForUser(currentUserId);
    }

    @PostMapping(path = "/todo", consumes = "application/json", produces = "application/json")
    public Todo createNewTodo(@RequestBody Todo todo) {
        return todo;
    }

}
