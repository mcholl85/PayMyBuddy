package com.payme.buddy.service;

import com.payme.buddy.dto.UserDto;
import com.payme.buddy.entity.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByEmail(String email);
    UserDto getUserDtoByEmail(String email);
    void updateUser(User user);
    void addToBalance(String email, BigDecimal amount);
    boolean exceedBalance(String email, BigDecimal amount);
}
