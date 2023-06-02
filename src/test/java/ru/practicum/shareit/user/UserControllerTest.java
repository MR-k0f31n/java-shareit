package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author MR.k0F31n
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    private final UserDto user1 = new UserDto(1L, "user1", "user1@mail.com");
    private final UserDto user2 = new UserDto(2L, "user2", "user2@mail.com");
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void testGetAllUsers_empty() throws Exception {
        when(userService.findAllUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1))
                .findAllUser();
    }

    @Test
    void testGetAllUserExpectedLength2_correct() throws Exception {
        when(userService.findAllUser()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].email", is(user1.getEmail())))
                .andExpect(jsonPath("$[1].id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(user2.getName())))
                .andExpect(jsonPath("$[1].email", is(user2.getEmail())));

        verify(userService, times(1))
                .findAllUser();
    }

    @Test
    void testCreateUser_correct() throws Exception {
        when(userService.createNewUser(any())).thenReturn((user1));

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));

        verify(userService, times(1))
                .createNewUser(any());
    }

    @Test
    void testCreateUserWithEmailWrong_expectedError() throws Exception {
        UserDto userWrongEmail = new UserDto(1L, "name", "name,com.ru");
        UserDto userEmailNull = new UserDto(1L, "name", null);
        UserDto userEmailEmpty = new UserDto(1L, "name", "");
        UserDto userEmailIsBlank = new UserDto(1L, "name", " ");
        UserDto userEmailWithBlank = new UserDto(1L, "name", "name @com.ru");
        when(userService.createNewUser(any())).thenThrow(new ValidationException());

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userWrongEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userEmailNull))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userEmailEmpty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userEmailIsBlank))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userEmailWithBlank))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(userService, times(0))
                .createNewUser(any());
    }

    @Test
    void testGetUserById_correct() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(user1);

        mockMvc.perform(get("/users/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));

        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    void testGetUserById_error() throws Exception {
        when(userService.findUserById(anyLong())).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/99"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    void testUpdateUser() throws Exception {
        final UserDto userUpdate = new UserDto(1L, "name1", "email1@email.ru");
        userUpdate.setName("updateName");

        when(userService.updateUser(any(), anyLong())).thenReturn(userUpdate);

        mockMvc.perform(patch("/users/{id}", userUpdate.getId())
                        .content(mapper.writeValueAsString(userUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userUpdate.getName())))
                .andExpect(jsonPath("$.email", is(userUpdate.getEmail())));

        userUpdate.setEmail("emailUpdate@mail.ru");

        mockMvc.perform(patch("/users/{id}", userUpdate.getId())
                        .content(mapper.writeValueAsString(userUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userUpdate.getName())))
                .andExpect(jsonPath("$.email", is(userUpdate.getEmail())));

        verify(userService, times(2))
                .updateUser(any(), anyLong());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/users/{id}", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1))
                .deleteUserById(anyLong());
    }
}
