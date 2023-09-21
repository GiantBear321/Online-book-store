package com.example.onlinebookstore.dto.user;

import com.example.onlinebookstore.validation.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @Email
    private String email;
    @NotNull
    @Size(min = 5, max = 50)
    private String password;
}
