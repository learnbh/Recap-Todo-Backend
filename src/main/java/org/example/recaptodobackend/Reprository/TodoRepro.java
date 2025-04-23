package org.example.recaptodobackend.Reprository;

import org.example.recaptodobackend.Models.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepro extends MongoRepository<Todo, String> {
}
