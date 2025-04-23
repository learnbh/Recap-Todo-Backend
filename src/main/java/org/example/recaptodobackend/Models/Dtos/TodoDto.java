package org.example.recaptodobackend.Models.Dtos;

import org.example.recaptodobackend.Enums.TodoStatus;

public record TodoDto(String description, TodoStatus status) {
}
