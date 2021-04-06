package org.umutalacam.todo.data.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Document
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo implements Identifiable{
    @Id
    private String documentId;

    @NotNull
    @Field
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String ownerId;

    @NotNull
    @Field
    private String title;

    @NotNull
    @Field
    private String description;

    @NotNull
    @Field
    private List<String> tags;

    @Field
    private long created;

    @Field
    private long modified;

    @Field
    private long completed;

    public Todo(String ownerId, String title, String description, List<String> tags) {
        this.documentId = this.generateId();
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public String generateId() {
        return "todo::" + UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Todo {" +
                "documentId='" + documentId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", created=" + created +
                ", modified=" + modified +
                ", completed=" + completed +
                '}';
    }
}
