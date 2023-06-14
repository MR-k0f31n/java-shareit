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
    void getAllUsers_returnEmpty_() throws Exception {
        when(userService.findAllUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1))
                .findAllUser();
    }

    @Test
    void getAllUser_return2Entries_exist2Users() throws Exception {
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
    void createNewUser_returnDto() throws Exception {
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
    void getUserById_returnDto() throws Exception {
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
    void getUserById_expectedThrow_code404() throws Exception {
        when(userService.findUserById(anyLong())).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/99"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    void updateUserName_returnDto() throws Exception {
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

        verify(userService, times(1))
                .updateUser(any(), anyLong());
    }

    @Test
    void updateUserEmail_returnDto() throws Exception {
        final UserDto userUpdate = new UserDto(1L, "name1", "email1@email.ru");
        userUpdate.setEmail("emailUpdate@mail.ru");

        when(userService.updateUser(any(), anyLong())).thenReturn(userUpdate);

        mockMvc.perform(patch("/users/{id}", userUpdate.getId())
                        .content(mapper.writeValueAsString(userUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userUpdate.getName())))
                .andExpect(jsonPath("$.email", is(userUpdate.getEmail())));

        verify(userService, times(1))
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
