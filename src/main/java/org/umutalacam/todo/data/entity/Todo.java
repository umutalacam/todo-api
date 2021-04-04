package org.umutalacam.todo.data.entity;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Document
public class Todo implements Identifiable{
    @Id
    private String documentId;

    @NotNull
    @Field
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
    private Timestamp created;

    @Field
    private Timestamp modified;

    @Field
    private Timestamp completed;

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

}
