package org.umutalacam.todo.data.repository;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.umutalacam.todo.data.entity.Todo;

import java.util.List;

public interface TodoRepository extends CouchbaseRepository<Todo, String> {

    List<Todo> findAllByOwnerId(String ownerId);

}
