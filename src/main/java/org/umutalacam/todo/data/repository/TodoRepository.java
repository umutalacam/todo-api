package org.umutalacam.todo.data.repository;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.umutalacam.todo.data.entity.Todo;

public interface TodoRepository extends CouchbaseRepository<Todo, String> {


}
