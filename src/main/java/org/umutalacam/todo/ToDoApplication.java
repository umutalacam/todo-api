package org.umutalacam.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.umutalacam.todo.service.TodoService;
import org.umutalacam.todo.service.UserService;

@SpringBootApplication
@EnableWebSecurity
public class ToDoApplication {
    TodoService todoService;
    UserService userService;

    public ToDoApplication(TodoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ToDoApplication.class, args);
    }

}
