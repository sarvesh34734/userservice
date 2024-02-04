package dev.sarvesh.userservice.populators;

import dev.sarvesh.userservice.dtos.UserDto;
import dev.sarvesh.userservice.models.User;

public class UserPopulator {

    public static UserDto toUserDto(User user){
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }

    public static User toUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setEncpass(userDto.getEncpass());
        return user;
    }

}
