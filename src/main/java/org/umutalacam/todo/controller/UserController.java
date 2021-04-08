package org.umutalacam.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.security.UserDataValidator;
import org.umutalacam.todo.security.UserDetail;
import org.umutalacam.todo.service.UserService;

import javax.management.InvalidAttributeValueException;
import java.util.HashMap;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user/{userName}")
    public ResponseEntity<Object> getUser(@PathVariable String userName){
        User user = userService.getUserByUsername(userName);

        ResponseEntity<Object> responseEntity;
        if (user == null) {
            // User not found
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "User not found.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        } else {
            // User found
            responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
        }
        return responseEntity;
    }

    /**
     * Update user
     * @param user User object that contains updated fields. Only fields that is going to be updated is needed to sent.
     * @param principal Authenticated user
     * @param userName Username of the user that is going to be updated.
     * @return Updated user
     */
    @PutMapping(path="/user/{userName}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @AuthenticationPrincipal UserDetail principal, @PathVariable String userName) {
        User authenticatedUser = principal.getPrincipalUser();
        User oldUser = userService.getUserByUsername(userName);

        ResponseEntity<Object> responseEntity;
        if (oldUser == null) {
            // User not found
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "User not found.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        else if (!authenticatedUser.getUserId().equals(oldUser.getUserId())) {
            // Not authorized
            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Not authorized.");
            responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
            return responseEntity;
        } else {
            // Update user fields safely
            try {
                // Check if password is being updated
                boolean encodePassword = true;

                if (user.getPassword() == null) {
                    // Password data is not sent, keep old password without re encoding the existing data
                    encodePassword = false;
                    user.setPassword(oldUser.getPassword());
                }

                // Recover null fields TODO: Can null control moved into User service?
                user.setUserId(oldUser.getUserId());
                user.setUsername(oldUser.getUsername());
                if (user.getEmail() == null) user.setEmail(oldUser.getEmail());
                if (user.getFirstName() == null) user.setFirstName(oldUser.getFirstName());
                if (user.getLastName() == null) user.setLastName(oldUser.getLastName());

                // Validate user data for upsert operation
                User validatedUser = UserDataValidator.validateUserForInsertion(user, encodePassword);

                // Update user
                userService.updateUser(validatedUser);
                return new ResponseEntity<>(validatedUser, HttpStatus.OK);

            } catch (InvalidAttributeValueException e) {
                HashMap<String, String> errorMessage = new HashMap<>();
                errorMessage.put("error", e.getMessage());
                responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
                return responseEntity;
            }

        }
    }

}
