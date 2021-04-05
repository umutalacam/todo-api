package org.umutalacam.todo.security;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.umutalacam.todo.data.entity.User;
import org.umutalacam.todo.service.UserService;

@Component
@Scope("singleton")
public class UserDetailService implements UserDetailsService {

    UserService userService;

    public UserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(s);
        if (user == null) throw new UsernameNotFoundException("User not found with the given username");

        return new UserDetail(user);
    }
}
