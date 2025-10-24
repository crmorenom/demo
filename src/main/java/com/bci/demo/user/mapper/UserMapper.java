package com.bci.demo.user.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bci.demo.user.dto.LoginResponseDTO;
import com.bci.demo.user.dto.PhoneResponseDTO;
import com.bci.demo.user.dto.UserResponseDTO;
import com.bci.demo.user.entity.Phone;
import com.bci.demo.user.entity.User;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                user.getToken(),
                user.getIsActive(),
                user.getPhones().stream().map(this::toPhoneResponseDTO).collect(Collectors.toList())
        );
    }

    private PhoneResponseDTO toPhoneResponseDTO(Phone phone) {
        return new PhoneResponseDTO(
                phone.getId(),
                phone.getNumber(),
                phone.getCitycode(),
                phone.getCountrycode()
        );
    }

    public LoginResponseDTO toLoginResponseDTO(User user) {
        return new LoginResponseDTO(
                user.getId(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                user.getToken(),
                user.getIsActive()
        );
    }
}
