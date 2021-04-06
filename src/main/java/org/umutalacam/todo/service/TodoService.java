package org.umutalacam.todo.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.umutalacam.todo.data.entity.Todo;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.data.repository.TodoRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class TodoService {
    TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void saveTodoForUser(User user, Todo todo) {
        todo.setOwnerId(user.getUserId());
        saveTodo(todo);
    }

    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public void updateTodo(Todo todo) throws DataIntegrityViolationException{
        String todoId = todo.getDocumentId();
        Todo oldTodo = getTodoById(todoId);

        if (oldTodo == null)
            throw new DataIntegrityViolationException("Todo object with the given id does not exist.");

        todoRepository.save(oldTodo);
    }


    public void deleteTodoById(String todoId) {
        todoRepository.deleteById(todoId);
    }

    public Todo getTodoById(String todoId) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        return optionalTodo.orElse(null);
    }

    public List<Todo> getTodosForUser(User user){
        String ownerId = user.getUserId();
        return getTodosForUser(ownerId);
    }

    public List<Todo> getTodosForUser(String userId) {
        return todoRepository.findAllByOwnerId(userId);
    }
}
