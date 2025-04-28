package org.example.recaptodobackend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.recaptodobackend.Enums.TodoStatus;
import org.example.recaptodobackend.Models.Dtos.TodoDto;
import org.example.recaptodobackend.Models.Todo;
import org.example.recaptodobackend.Reprository.TodoRepro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepro mockTodoRepro;

    Todo todo1 = new Todo("1", "test1", TodoStatus.OPEN);
    Todo todo2 = new Todo("2", "test2", TodoStatus.IN_PROGRESS);
    TodoDto todoDto1 = new TodoDto(todo1.description(), todo1.status());

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllTodos_shouldHandleUnknownError() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/trigger-error"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
    }
    @Test
    void test_getAllTodos_shouldReturnEmptyList_Initialy() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
    @Test
    void test_getAllTodos_shouldReturnListOfTwo() throws Exception{
        mockTodoRepro.save(todo1);
        mockTodoRepro.save(todo2);
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
         """
                        [
                            {
                                "description":"test1",
                                "status":"OPEN"
                            },
                            {
                                "description":"test2",
                                "status":"IN_PROGRESS"
                            }
                        ]
                    """
                ));
    }
    @Test
    void test_getTodoById_shouldReturnTodoDto_ForId1() throws Exception{
        // Given
        mockTodoRepro.save(todo1);
        mockTodoRepro.save(todo2);
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/"+todo1.id()))
        // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(todoDto1)));
    }
    @Test
    void test_getTodoById_shouldReturnTodoNotFoundException_ForIdNotExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/IdNotExist"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Todo with id IdNotExist could not be found!"));
    }
    @Test
    void test_addTodo_shouldReturnTrue() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoDto1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(todoDto1)));
    }
    @Test
    void test_updateTodo_shouldReturnTodoNotFoundException_ForIdNotExist() throws Exception{
        // Given
        TodoDto updateTodoData = new TodoDto(todo1.description(), TodoStatus.CANCELLED);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/IdNotExist", updateTodoData)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTodoData)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Todo with id IdNotExist could not be found!"));
    }
    @Test
    void test_deleteTodo_shouldReturnEmptyList_afterDeletingTodo() throws Exception{
        // Given
        mockTodoRepro.save(todo1);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                       [
                           {
                               "description":"test1",
                               "status":"OPEN"
                           }
                       ]
                   """));
        // When
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/"+todo1.id()))
        // Then
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));

    }
}
