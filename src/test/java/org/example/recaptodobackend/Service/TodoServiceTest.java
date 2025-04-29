package org.example.recaptodobackend.Service;

import org.example.recaptodobackend.Enums.TodoStatus;
import org.example.recaptodobackend.Models.Dtos.TodoDto;
import org.example.recaptodobackend.Models.Todo;
import org.example.recaptodobackend.Reprository.TodoRepro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    IdService mockIdService;
    TodoService mockTodoService;
    TodoRepro mockTodoRepro;

    Todo todo1;
    Todo todo2;
    TodoDto todoDto1;
    TodoDto todoDto2;

    @BeforeEach
    void setUp() {
        todo1 = new Todo("1", "test1", TodoStatus.OPEN);
        todo2 = new Todo("2", "test2", TodoStatus.OPEN);
        todoDto1 = new TodoDto(todo1.description(), todo1.status());
        todoDto2 = new TodoDto(todo2.description(), todo2.status());

        mockIdService = mock(IdService.class);
        mockTodoRepro = mock(TodoRepro.class);

        mockTodoService = new TodoService(mockTodoRepro, mockIdService);
    }
    @Test
    void getAllTodos_shouldReturnEmptyList_Initialy() {
        assertEquals(List.of(),  mockTodoService.getAllTodos());
    }
    @Test
    void getAllTodos_shouldReturnListOfTwo(){
        // Given
        mockTodoService = new TodoService(mockTodoRepro, mockIdService);
        when(mockTodoRepro.findAll()).thenReturn(List.of(todo1, todo2));
        List<TodoDto> expected = List.of(todoDto1, todoDto2);
        // When
        List<TodoDto> actual = mockTodoService.getAllTodos();
        // Then
        assertEquals(expected, actual);
        verify(mockTodoRepro, times(1)).findAll();
    }
    @Test
    void getTodoById_shouldReturnTodoDtoOptional_forTodoWithId1() {
        // Given
        mockTodoService = new TodoService(mockTodoRepro, mockIdService);
        Optional<Todo> expected = Optional.of(todo1);
        when(mockTodoRepro.findById(todo1.id())).thenReturn(expected);
        // When
        Optional<Todo> actual = mockTodoService.getTodoById(todo1.id());
        // Then
        assertEquals(expected, actual);
        verify(mockTodoRepro, times(1)).findById(todo1.id());
    }
    @Test
    void getTodoById_shouldReturnEmptyOptional_forInvalidId(){
        mockTodoService = new TodoService(mockTodoRepro, mockIdService);
        // Then
        assertEquals(Optional.empty(), mockTodoService.getTodoById("idExistNot"));
        verify(mockTodoRepro, times(1)).findById("idExistNot");
    }
    @Test
    void addTodo_shouldReturnTodoDto_forValidData(){
        // Given
        TodoDto expected = new TodoDto(todo1.description(), todo1.status());
        when(mockIdService.createId()).thenReturn("1");
        // When
        TodoDto actual = mockTodoService.addTodo(expected);
        // Then
        assertEquals(expected, actual);
        verify(mockTodoRepro, times(1)).save(todo1);
    }
    @Test
    void updateTodo_shouldReturnEmptyOptional_forInvalidId() {
        // Given
        TodoDto updateTodoData = new TodoDto(todo1.description(), TodoStatus.CANCELLED);
        // When
        Optional<TodoDto> actual = mockTodoService.updateTodo("InvalidId", updateTodoData);
        // Then
        assertEquals(Optional.empty(), actual);
    }
    @Test
    void updateTodo_shouldReturnOptionalTodoDto_forValidId() {
        // Given
        TodoDto updateTodoData = new TodoDto(todo1.description(), TodoStatus.CANCELLED);
        when(mockTodoRepro.findById(todo1.id())).thenReturn(Optional.of(todo1));
        Optional<TodoDto> expected = Optional.of(updateTodoData);
        // When
        Optional<TodoDto> actual = mockTodoService.updateTodo(todo1.id(), updateTodoData);
        // Then
        assertEquals(expected, actual);
        verify(mockTodoRepro, times(1))
                .save(new Todo(todo1.id(),
                        updateTodoData.description(),
                        updateTodoData.status()));
    }
    @Test
    void deleteTodo_shouldReturnTrue_AfterDeletingATodo(){
        mockTodoService.deleteTodo(todo1.id());
        verify(mockTodoRepro, times(1)).deleteById(todo1.id());
    }
}