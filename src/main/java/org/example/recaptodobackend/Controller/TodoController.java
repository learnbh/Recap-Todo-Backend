package org.example.recaptodobackend.Controller;

import org.example.recaptodobackend.Exception.TodoNotFoundException;
import org.example.recaptodobackend.Models.Dtos.TodoDto;
import org.example.recaptodobackend.Models.Todo;
import org.example.recaptodobackend.Service.ChatGPTService;
import org.example.recaptodobackend.Service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> getTodoById(@PathVariable String id){
        try{
            Todo todo = todoService.getTodoById(id)
                    .orElseThrow(()->new TodoNotFoundException(id));
            return ResponseEntity.ok(new TodoDto(todo.description(), todo.status()));
        } catch (TodoNotFoundException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @PostMapping
    public TodoDto addTodo(@RequestBody TodoDto todoDto){
        return todoService.addTodo(todoDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTodo(@PathVariable String id, @RequestBody TodoDto todoDto){
        try{
            TodoDto updatedTodoDto = todoService.updateTodo(id, todoDto)
                    .orElseThrow(()->new TodoNotFoundException(id));
            return ResponseEntity.ok(updatedTodoDto);
        } catch (TodoNotFoundException e){
            Map<String, String> responseError = new HashMap<>();
            responseError.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
        }
    }
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id){
        todoService.deleteTodo(id);
    }
}
