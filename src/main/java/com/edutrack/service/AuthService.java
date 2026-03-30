package com.edutrack.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutrack.dto.request.LoginRequest;
import com.edutrack.dto.request.RegisterRequest;
import com.edutrack.dto.response.AuthResponse;
import com.edutrack.entity.Role;
import com.edutrack.entity.User;
import com.edutrack.exception.UserAlreadyExistsException;
import com.edutrack.repository.RoleRepository;
import com.edutrack.repository.UserRepository;
import com.edutrack.security.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired private UserRepository      userRepository;
    @Autowired private RoleRepository      roleRepository;
    @Autowired private PasswordEncoder     passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider    jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {

        // check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // decide role — default is STUDENT
        String roleName = "INSTRUCTOR".equalsIgnoreCase(request.getRole())
                ? "ROLE_INSTRUCTOR" : "ROLE_STUDENT";

        // find role from DB or create it
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        // create user
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        // generate JWT token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AuthResponse(token, user.getId(),
                user.getEmail(), user.getFullName(), roleNames);
    }

    public AuthResponse login(LoginRequest request) {

        // authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        // generate JWT token
        String token = jwtTokenProvider.generateToken(authentication);

        // get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AuthResponse(token, user.getId(),
                user.getEmail(), user.getFullName(), roleNames);
    }
}