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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;
    @Field
    private String username;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private String email;
    @Field
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias("password")
    private String password;

    @Override
    public String generateId() {
        return "user::"+ this.username;
    }

}
