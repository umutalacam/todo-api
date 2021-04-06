package org.umutalacam.todo.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.umutalacam.todo.data.entity.Todo;
import org.umutalacam.todo.service.TodoService;

import java.time.Instant;
import java.util.HashMap;
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

    @GetMapping(path = "/todo/{id}", produces = "application/json")
    public ResponseEntity<Object> getTodo(@PathVariable  String id) {
        String todoId = "todo::"+id;
        Todo todo = todoService.getTodoById(todoId);
        ResponseEntity<Object> responseEntity;
        if (todo != null) {
            responseEntity = new ResponseEntity<>(todo, HttpStatus.OK);
        }
        else {
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Todo with the given id is not found.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @PostMapping(path = "/todo", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createNewTodo(@RequestBody Todo todo) {
        // TODO: Test bad requests
        // Set owner of task
        todo.setOwnerId(currentUserId);
        todo.setDocumentId(todo.generateId());
        // Set created date
        todo.setCreated(Instant.now().toEpochMilli());
        this.todoService.saveTodo(todo);
        return todo;
    }

    @PutMapping(path = "/todo/{id}")
    public ResponseEntity<Object> updateTodo(@RequestBody Todo todo, @PathVariable String id) {
        ResponseEntity<Object> responseEntity;
        try {
            todo.setModified(Instant.now().toEpochMilli());
            todo.setDocumentId("todo::"+id);

            todoService.updateTodo(todo);
            responseEntity = new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (DataIntegrityViolationException exception){
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Todo with the given id is not found.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

}
