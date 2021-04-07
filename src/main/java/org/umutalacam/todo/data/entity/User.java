package org.umutalacam.todo.data.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias("password")
    private String password;

    public User(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = "";
        this.userId = generateId();
    }

    public User(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userId = generateId();
    }

    @Override
    public String generateId() {
        return "user::"+ this.username;
    }
}
