package com.payme.buddy.dto;

import com.payme.buddy.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String email;

    @NotNull
    private String lastname;

    @NotNull
    private String firstname;

    private BigDecimal balance;

    public static UserDto fromEntity(User user) {
        return new UserDto(user.getEmail(),
                user.getLastname(),
                user.getFirstname(),
                user.getBalance());
    }
}
