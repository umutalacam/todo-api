package org.umutalacam.todo.service;

import org.springframework.stereotype.Service;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.data.repository.UserRepository;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user){
        // TODO: Error handling
        userRepository.save(user);
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
