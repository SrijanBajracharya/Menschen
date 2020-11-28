package com.achiever.menschenfahren;

import org.junit.jupiter.api.Test;

import com.achiever.menschenfahren.entities.response.UserCreateDto;
import com.achiever.menschenfahren.entities.response.UserDto;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.mapper.UserMapper;

public class UserMapperTest {

    @Test
    public void test() {
        final UserMapper mapper = new UserMapper();
        final UserCreateDto dto = new UserCreateDto();
        dto.setFirstName("abc");
        dto.setLastName("abc");
        dto.setEmail("abc@gmail.com");
        dto.setPassword("abc");
        final User user = mapper.map(dto, User.class);
        final UserDto userDto = mapper.map(user, UserDto.class);
    }

}
