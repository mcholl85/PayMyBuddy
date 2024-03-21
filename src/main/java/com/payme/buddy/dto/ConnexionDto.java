package com.payme.buddy.dto;

import com.payme.buddy.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnexionDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String email;

    @NotNull
    private String lastname;

    @NotNull
    private String firstname;

    public static ConnexionDto fromEntity(User user) {
        return new ConnexionDto(user.getEmail(), user.getLastname(), user.getFirstname());
    }
}
