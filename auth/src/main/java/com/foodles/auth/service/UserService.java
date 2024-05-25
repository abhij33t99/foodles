package com.foodles.auth.service;

import com.foodles.auth.exception.UsernameNotFoundException;
import com.foodles.auth.model.User;
import com.foodles.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User loadUserEntity(String username) {
        return userRepository.findByEmail(username).orElse(null);
    }

}
