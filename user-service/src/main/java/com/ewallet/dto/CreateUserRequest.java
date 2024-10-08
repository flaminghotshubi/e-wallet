package com.ewallet.dto;

import com.ewallet.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private String name;

    @NotBlank
    @Size(min = 3, message = "Username should be minimum of 3 characters")
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password should be minimum of 6 characters")
    private String password;

//    @NotBlank
//    @Size(max = 10, min = 10, message = "Mobile number has to be of 10 digits")
//    private String mobile;

//    @NotBlank
    @Email
    private String email;

    public User to() {
        return User.builder()
                .email(email)
                .username(username)
                .password(password)
                .name(name)
                .build();
    }

}
