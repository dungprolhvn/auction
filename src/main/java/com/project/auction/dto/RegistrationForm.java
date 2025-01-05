package com.project.auction.dto;

import com.project.auction.exception.PasswordMismatchException;
import com.project.auction.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {
    @Size(min = 8, max = 25)
    private String username;

    @Size(min = 8, max = 20)
    private String password;

    @Size(min = 8, max = 20)
    private String confirmation;

    @Email
    private String email;

    public User toUser(PasswordEncoder encoder) {
        if (password.equals(confirmation)) {
            return new User(username, encoder.encode(password), email);
        }
        throw new PasswordMismatchException("Password do not match");
    }

}
