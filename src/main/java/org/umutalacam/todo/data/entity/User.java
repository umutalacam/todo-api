package org.umutalacam.todo.data.entity;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.UUID;

@Data
@Document
public class User implements Identifiable {
    @Id
    private String userId;
    @Field
    @NotNull
    private String username;
    @Field
    @NotNull
    private String firstName;
    @Field
    @NotNull
    private String lastName;
    @Field
    @NotNull
    private String email;
    @Field
    @NotNull
    private String encodedPassword;

    public User(String username, String firstName, String lastName, String email) {
        this.userId = generateId();
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.encodedPassword = "";
    }

    public User(String username, String firstName, String lastName, String email, String encodedPassword) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.encodedPassword = encodedPassword;
    }

    @Override
    public String generateId() {
        return "user::"+ UUID.randomUUID();
    }
}
