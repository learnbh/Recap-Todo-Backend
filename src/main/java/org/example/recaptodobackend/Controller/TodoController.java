package org.example.recaptodobackend.Controller;

import org.example.recaptodobackend.Exception.IdNotFoundException;
import org.example.recaptodobackend.Models.Dtos.TodoDto;
import org.example.recaptodobackend.Models.Todo;
import org.example.recaptodobackend.Service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @GetMapping
    public List<TodoDto> getAllTodos(){
        return todoService.getAllTodos();
    }
    @GetMapping("/{id}")
    public TodoDto getTodoById(@PathVariable String id){
        Todo todo = todoService.getTodoById(id).orElseThrow(()->new IdNotFoundException(id));
        return new TodoDto(todo.description(), todo.status());
    }
    @PostMapping
    public TodoDto addTodo(@RequestBody TodoDto todoDto){
        return todoService.addTodo(todoDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTodo(@PathVariable String id, @RequestBody TodoDto todoDto){
        TodoDto updatedTodoDto = todoService.updateTodo(id, todoDto)
                .orElseThrow(()->new IdNotFoundException(id));
        return ResponseEntity.ok(updatedTodoDto);
    }
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id){
        todoService.deleteTodo(id);
    }
}
