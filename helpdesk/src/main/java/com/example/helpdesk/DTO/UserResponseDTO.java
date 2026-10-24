package com.example.helpdesk.DTO;

import com.example.helpdesk.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
}
