package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author MR.k0F31n
 */

@JsonTest
public class UserJsonTest {
    @Autowired
    private JacksonTester<UserDto> jsonUserDto;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(1L, "name1", "email@mail.ru");

        JsonContent<UserDto> result = jsonUserDto.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name1");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("email@mail.ru");
    }
}
