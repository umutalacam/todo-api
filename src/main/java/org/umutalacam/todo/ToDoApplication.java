package org.umutalacam.todo;

import com.sun.tools.javac.comp.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.data.repository.TodoRepository;
import org.umutalacam.todo.data.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.swing.*;

@SpringBootApplication
public class ToDoApplication {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TodoRepository todoRepository;

    public static void main(String[] args) {
        SpringApplication.run(ToDoApplication.class, args);
    }

    @PostConstruct
    public void initDatabase(){
        User user = new User("umutcanalacam", "Umut Can", "Alaçam");
        userRepository.save(user);
    }

}
