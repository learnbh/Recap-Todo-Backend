package org.example.recaptodobackend.Service;

import org.example.recaptodobackend.Models.Dtos.TodoDto;
import org.example.recaptodobackend.Models.Todo;
import org.example.recaptodobackend.Reprository.TodoRepro;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepro todoRepro;
    private final IdService idService;

    public TodoService(TodoRepro todoRepro, IdService idService) {
        this.todoRepro = todoRepro;
        this.idService = idService;
    }

    public List<TodoDto> getAllTodos() {

        return todoRepro.findAll().stream()
                .map(t->new TodoDto(t.description(), t.status()))
                .toList();
    }

    public Optional<Todo> getTodoById(String id) {
        return todoRepro.findById(id);
    }

    public TodoDto addTodo(TodoDto todoDto) {
        todoRepro.save( new Todo(
                idService.createId(),
                todoDto.description(),
                todoDto.status()));
        return todoDto;
    }

    public Optional<TodoDto> updateTodo(String id, TodoDto todoDto) {
        Optional<Todo> optionalTodo = this.getTodoById(id);
        if(optionalTodo.isEmpty())
            return Optional.empty();
        todoRepro.save(new Todo(id, todoDto.description(), todoDto.status()));
        return Optional.of(todoDto);
    }

    public void deleteTodo(String id) {
        todoRepro.deleteById(id);
    }
}
