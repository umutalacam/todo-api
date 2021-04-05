package org.umutalacam.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.umutalacam.todo.data.entity.Todo;
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
        Todo todo1 = new Todo("user::c485c5bb-99a4-49c9-83df-4d32f1aed420", "Todo Task 3", "Todo task descrip", Collections.emptyList());
        todoService.saveTodo(todo1);

        List<Todo> todos = todoService.getTodosForUser("user::c485c5bb-99a4-49c9-83df-4d32f1aed420");

        todos.forEach(System.out::println);
    }

}
