package com.example.onlinebookstore.dto.user;

import com.example.onlinebookstore.validation.Email;
import com.example.onlinebookstore.validation.FieldMatch;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(field = "password",
        fieldMatch = "repeatPassword",
        message = "Password and repeat password shouldn't be empty and should be equal")
public class UserRegistrationRequestDto {
    @Email
    private String email;
    @NotNull
    @Size(min = 5, max = 50)
    private String password;
    @NotNull
    @Size(min = 5, max = 50)
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String shippingAddress;
}
