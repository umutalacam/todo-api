package org.umutalacam.todo.service;

import org.springframework.stereotype.Service;
import org.umutalacam.todo.data.entity.Todo;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.data.repository.TodoRepository;

import java.util.List;

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

    public void deleteTodoById(String todoId) {
        todoRepository.deleteById(todoId);
    }

    public List<Todo> getTodosForUser(User user){
        String ownerId = user.getUserId();
        return getTodosForUser(ownerId);
    }

    public List<Todo> getTodosForUser(String userId) {
        return todoRepository.findAllByOwnerId(userId);
    }
}
