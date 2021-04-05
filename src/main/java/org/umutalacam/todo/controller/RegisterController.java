package org.umutalacam.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.security.UserDataValidator;
import org.umutalacam.todo.service.UserService;

import javax.naming.directory.InvalidAttributeValueException;
import javax.xml.ws.Response;
import java.util.HashMap;

@RestController
public class RegisterController {

    UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<HashMap<String, Object>> registerNewUser(@RequestBody User user) {
        // Validate user object
        HashMap<String, Object> responseBody = new HashMap<>();

        try {
            user = UserDataValidator.validateUserForInsertion(user);
            // TODO: DB insertion validation
            userService.createNewUser(user);
            responseBody.put("message", "User registered successfully");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } catch (InvalidAttributeValueException exception) {
            System.err.println("Bad request on register!");
            responseBody.put("error", exception.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }
        catch (Exception exception) {
            responseBody.put("error", "Internal server error");
            exception.printStackTrace();
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
