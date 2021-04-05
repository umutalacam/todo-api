package org.umutalacam.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.umutalacam.todo.data.entity.Todo;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.service.TodoService;
import org.umutalacam.todo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class ToDoApplication {


    @Autowired
    TodoService todoService;
    @Autowired
    UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(ToDoApplication.class, args);
    }

    @PostConstruct
    public void initDatabase(){
        User u1 = new User("john.doe", "John", "Doe", "bahar@gmail.com");
        try {
            userService.createNewUser(u1);
        } catch (Exception exception) {
            System.err.println("User already exists.");
        }

        List<Todo> todos = todoService.getTodosForUser("user::c485c5bb-99a4-49c9-83df-4d32f1aed420");
        todos.forEach(System.out::println);
    }

}
