package org.umutalacam.todo.data.repository;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.umutalacam.todo.data.entity.User;

public interface UserRepository extends CouchbaseRepository<User, String> {
    User getUserByUserId(String userId);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
}
