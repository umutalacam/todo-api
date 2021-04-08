package org.umutalacam.todo.security;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.service.UserService;

import javax.management.InvalidAttributeValueException;


/**
 * Helper class for User data validation
 */
@Component
@Scope("singleton")
public class UserDataValidator {

    static UserService userService;
    private static BCryptPasswordEncoder passwordEncoder;

    public UserDataValidator(UserService service) {
        userService = service;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Validate given user object's data and encode password, then return the object itself if validation is correct.
     * @param user User that is going to be validated.
     * @return The same user object
     * @throws InvalidAttributeValueException | Exception
     */
    public static User validateUserForInsertion(User user, boolean encodePassword) throws InvalidAttributeValueException {
        String username = user.getUsername();
        String password = user.getPassword();

        // Validate username and password
        validateUsername(username);
        if (encodePassword) validatePassword(password);

        // Validate other required fields
        if (user.getUserId() == null) user.setUserId(user.generateId());
        if (user.getFirstName() == null) throw new InvalidAttributeValueException("Firstname field can't be empty");
        if (user.getLastName() == null) throw new InvalidAttributeValueException("Lastname field can't be empty");
        if (user.getEmail() == null) throw new InvalidAttributeValueException("Email field can't be empty");

        // Encode password
        if (encodePassword) {
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }

        return user;
    }

    public static String validateUsername(String username) throws InvalidAttributeValueException {
        if (username.isEmpty())
            throw new InvalidAttributeValueException("Username can't be empty.");

        if (username.length() < 5)
            throw new InvalidAttributeValueException("Username must be longer than 5 characters.");

        if (!username.matches("[A-z0-9]+"))
            throw new InvalidAttributeValueException("Username can't include special characters.");

        return username;
    }

    public static String validatePassword(String password) throws InvalidAttributeValueException {
        if (password.isEmpty())
            throw new InvalidAttributeValueException("Password can't be empty.");

        if (password.length() < 6)
            throw new InvalidAttributeValueException("Password must be longer than 6 characters.");

        if (!password.matches("[A-z0-9]+"))
            throw new InvalidAttributeValueException("Password can't include special characters.");

        return password;
    }
}
