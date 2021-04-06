package org.umutalacam.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.umutalacam.todo.data.entity.Todo;
import org.umutalacam.todo.security.UserDetail;
import org.umutalacam.todo.service.TodoService;

import javax.xml.ws.Response;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

/**
 * Controller for To Do service endpoints.
 * Created by Umut Can Alaçam
 */
@RestController
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(path = "/todo", produces = "application/json")
    public List<Todo> getAllTodosForUser(@AuthenticationPrincipal UserDetail principal){
        // Todo: Error handling
        // Todo: Filtered requests
        String principalUserId = principal.getPrincipalUser().getUserId();
        return this.todoService.getTodosForUser(principalUserId);
    }

    @GetMapping(path = "/todo/{id}", produces = "application/json")
    public ResponseEntity<Object> getTodo(@PathVariable  String id, @AuthenticationPrincipal UserDetail principal) {
        // Get current user Id.
        String principalUserId = principal.getPrincipalUser().getUserId();

        // Get to do object
        String todoId = "todo::"+id;
        Todo todo = todoService.getTodoById(todoId);

        // Build response
        ResponseEntity<Object> responseEntity;

        if (todo == null){
            // Resource does not exist
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Todo with the given id is not found.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
        else if (!todo.getOwnerId().equals(principalUserId)) {
            // Unauthorized access ???? Is this fine
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Not authorized.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }
        else {
            // Return resource
            responseEntity = new ResponseEntity<>(todo, HttpStatus.OK);
        }

        return responseEntity;
    }

    @PostMapping(path = "/todo", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createNewTodo(@RequestBody Todo todo,
                              @AuthenticationPrincipal UserDetail principal) {
        // TODO: Test for bad requests
        // Get current user
        String principalUserId = principal.getPrincipalUser().getUserId();
        // Set owner of task
        todo.setOwnerId(principalUserId);
        todo.setDocumentId(todo.generateId());
        // Set created date
        todo.setCreated(Instant.now().toEpochMilli());
        this.todoService.saveTodo(todo);
        return todo;
    }

    @PutMapping(path = "/todo/{id}")
    public ResponseEntity<Object> updateTodo(@RequestBody Todo todo,
                                             @PathVariable String id,
                                             @AuthenticationPrincipal UserDetail principal) {
        // Get current user
        String principalUserId = principal.getPrincipalUser().getUserId();
        // Get old to do object
        String todoId = "todo::"+id;
        Todo oldTodo = todoService.getTodoById(todoId);

        ResponseEntity<Object> responseEntity;
        if (oldTodo == null) {
            // To do not found
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Todo with the given id is not found.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);

        } else if (!oldTodo.getOwnerId().equals(principalUserId)){
            // User does not have this to do
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Not authorized");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);

        } else {
            // Update to do
            todo.setCreated(oldTodo.getCreated());
            todo.setModified(Instant.now().toEpochMilli());
            todo.setDocumentId(oldTodo.getDocumentId());
            todoService.updateTodo(todo);
            responseEntity = new ResponseEntity<>(todo, HttpStatus.OK);
        }

        return responseEntity;
    }


    @DeleteMapping(path = "/todo/{id}")
    public ResponseEntity<Object> deleteTodo(@PathVariable String id){
        String todoId = "todo::"+id;
        // Check if to do object with the given id is exists.
        Todo oldTodo = todoService.getTodoById(todoId);

        ResponseEntity<Object> responseEntity;

        if (oldTodo == null) {
            // To do not found
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Todo with the given id is not found.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        } else {
            // Delete to do
            todoService.deleteTodoById(todoId);
            HashMap<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Todo deleted successfully.");
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        return responseEntity;
    }

}
