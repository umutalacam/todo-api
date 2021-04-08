package org.umutalacam.todo.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.data.repository.UserRepository;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewUser(User user){
        String username = user.getUsername();
        User u = getUserByUsername(username);
        if (u != null) {
            // Username already exists
            throw new DataIntegrityViolationException("User already exists.");
        } else {
            userRepository.save(user);
        }
    }

    public void updateUser(User user) {
        String username = user.getUsername();
        User oldUser = getUserById(user.getUserId());
        // Ensure data integrity
        if (oldUser == null) {
            throw new DataIntegrityViolationException("User does not exist.");
        } else {
            userRepository.save(user);
        }
    }

    public void deleteUserById(String userId){
        userRepository.deleteById(userId);
    }

    public User getUserById(String userId){
        return userRepository.getUserByUserId(userId);
    }

    public User getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

}
