package com.integrations.orderprocessing.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDTO {

    @NotBlank(message = "username should not be null")
    private String username;

    @NotBlank(message = "password should not be null")
    private String password;
}
