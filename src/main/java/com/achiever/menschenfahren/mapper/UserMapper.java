package com.achiever.menschenfahren.mapper;

import com.achiever.menschenfahren.entities.response.UserCreateDto;
import com.achiever.menschenfahren.entities.response.UserDto;
import com.achiever.menschenfahren.entities.response.UserEditDto;
import com.achiever.menschenfahren.entities.users.User;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class UserMapper extends ConfigurableMapper {

    @Override
    public void configure(final MapperFactory factory) {

        factory.classMap(User.class, UserDto.class).byDefault().register();
        factory.classMap(User.class, UserCreateDto.class).byDefault().register();
        factory.classMap(User.class, UserEditDto.class).mapNullsInReverse(false).byDefault().register();
    }

    // public User convertUserCreateDtoToUser(@Nonnull UserCreateDto userCreateDto) {
    // final User user = new User();
    // user.setFirstName(userCreateDto.getFirstName());
    // user.setLastName(userCreateDto.getLastName());
    // user.setPassword(userCreateDto.getPassword());
    // user.setEmail(userCreateDto.getEmail());
    // user.setVoided(false);
    // user.setAuthenticationType(AuthProviderType.OTHER);
    // user.setActive(true);
    // return user;
    // }
    //
    // public UserDto convertUserToUserDto(@Nonnull User user) {
    // final UserDto userDto = new UserDto();
    // userDto.setEmail(user.getEmail());
    // userDto.setId(user.getId());
    // userDto.setFirstName(user.getFirstName());
    // userDto.setLastName(user.getLastName());
    // userDto.setVoided(user.isVoided());
    // return userDto;
    // }

}
