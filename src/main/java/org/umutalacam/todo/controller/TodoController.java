package org.umutalacam.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.umutalacam.todo.data.entity.Todo;
import org.umutalacam.todo.security.UserDetail;
import org.umutalacam.todo.service.TodoService;

import javax.management.InvalidAttributeValueException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Get todos that the user has.
     * @param principal Authenticated user
     * @param filterBy Filter by to do tags
     * @return List of todos
     */
    @GetMapping(path = "/todo", produces = "application/json")
    public List<Todo> getTodos(@AuthenticationPrincipal UserDetail principal, @RequestParam(required = false) String filterBy) {
        // Get current user
        String principalUserId = principal.getPrincipalUser().getUserId();

        List<Todo> todos = todoService.getTodosForUser(principalUserId);
        if (filterBy != null) {
            // Filter to do objects
            List<Todo> filteredTodos = todos.stream().filter(todo -> {
                List<String> todoTags = todo.getTags();
                return todoTags != null && todoTags.contains(filterBy);
            }).collect(Collectors.toList());
            return filteredTodos;
        }

        return todos;
    }

    /**
     * Get a to do with the given id
     * @param id To do id.
     * @param principal Authenticated user
     * @return To do object
     */
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

    /**
     * Create a new to do object
     * @param todo To do object
     * @param principal Authenticated user
     * @return Created To do object
     */
    @PostMapping(path = "/todo", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createNewTodo(@RequestBody Todo todo,
                              @AuthenticationPrincipal UserDetail principal) {
        // Get current user
        String principalUserId = principal.getPrincipalUser().getUserId();
        // Set owner of task
        todo.setOwnerId(principalUserId);
        todo.setDocumentId(todo.generateId());
        // Set created date
        todo.setCreated(Instant.now().toEpochMilli());
        try {
            this.todoService.saveTodo(todo);
        } catch (InvalidAttributeValueException e) {
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    /**
     * Update a to do object
     * @param todo To do object that contains the updated data. Only updated fields are enough.
     * @param id To do document id
     * @param principal Authenticated user
     * @return Updated to do object
     */
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
            if (todo.getCompleted() != 0) todo.setCompleted(Instant.now().toEpochMilli());

            todo.setDocumentId(oldTodo.getDocumentId());
            todoService.updateTodo(todo);
            responseEntity = new ResponseEntity<>(todo, HttpStatus.OK);
        }

        return responseEntity;
    }


    /**
     * Delete a to do object with the given id
     * @param id To do object id which is going to be deleted
     * @return Message for operation
     */
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
