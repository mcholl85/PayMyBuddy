package com.payme.buddy.service.impl;

import com.payme.buddy.dto.UserDto;
import com.payme.buddy.entity.User;
import com.payme.buddy.repository.UserRepository;
import com.payme.buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public UserDto getUserDtoByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.map(UserDto::fromEntity).orElse(null);
    }
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }
    @Override
    public void addToBalance(String email, BigDecimal amount) {
        User userToUpdate = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        userToUpdate.setBalance(userToUpdate.getBalance().add(amount));

        userRepository.save(userToUpdate);
    }

    @Override
    public boolean exceedBalance(String email, BigDecimal amount) {
        if(amount == null) return false;

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getBalance().compareTo(amount) < 0;
    }
}
