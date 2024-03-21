package com.payme.buddy.service.impl;

import com.payme.buddy.dto.UserDto;
import com.payme.buddy.entity.User;
import com.payme.buddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("user@test.com");

        assertTrue(result.isPresent());
        assertEquals("user@test.com", result.get().getEmail());
    }
    @Test
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("unknown@test.com");

        assertFalse(result.isPresent());
    }
    @Test
    void testGetUserDtoByEmail() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        UserDto result = userService.getUserDtoByEmail("user@test.com");

        assertNotNull(result);
        assertEquals("user@test.com", result.getEmail());
    }

    @Test
    void testGetUserDtoByEmailNotFound() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        UserDto result = userService.getUserDtoByEmail("unknown@test.com");

        assertNull(result);
    }

    @Test
    void testUpdateUser() {
        User user = new User();

        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }
    @Test
    void testAddToBalance() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setBalance(new BigDecimal("100.00"));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        userService.addToBalance("user@test.com", new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), user.getBalance());
    }

    @Test
    void testAddToBalanceUserNotFound() {
        BigDecimal amount = new BigDecimal("50.00");

        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.addToBalance("unknown@test.com",amount));
    }
    @Test
    void testExceedBalance() {
        User user = new User();
        user.setBalance(new BigDecimal("100.00"));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        boolean result = userService.exceedBalance("user@test.com", new BigDecimal("150.00"));

        assertTrue(result);
    }

    @Test
    void testExceedBalanceNotExceeded() {
        User user = new User();
        user.setBalance(new BigDecimal("200.00"));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        boolean result = userService.exceedBalance("user@test.com", new BigDecimal("150.00"));

        assertFalse(result);
    }

    @Test
    void testExceedBalanceUserNotFound() {
        BigDecimal amount = new BigDecimal("50.00");

        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.exceedBalance("unknown@test.com", amount));
    }


}
