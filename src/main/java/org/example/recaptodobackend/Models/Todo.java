package org.example.recaptodobackend.Models;

import org.example.recaptodobackend.Enums.TodoStatus;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Todos")
public record Todo(String id, String description, TodoStatus status) {
}
