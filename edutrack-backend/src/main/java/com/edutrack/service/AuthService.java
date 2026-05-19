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
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.exception.UserAlreadyExistsException;
import com.edutrack.repository.EmailVerificationTokenRepository;
import com.edutrack.repository.PasswordResetTokenRepository;
import com.edutrack.repository.RoleRepository;
import com.edutrack.repository.UserRepository;
import com.edutrack.security.JwtTokenProvider;

import com.edutrack.entity.PasswordResetToken;
import com.edutrack.entity.EmailVerificationToken;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired private UserRepository        userRepository;
    @Autowired private RoleRepository        roleRepository;
    @Autowired private PasswordEncoder       passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider      jwtTokenProvider;
    @Autowired private EmailService          emailService;
    @Autowired private PasswordResetTokenRepository
    resetTokenRepository;
@Autowired private EmailVerificationTokenRepository
    verifyTokenRepository;


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
        
        sendVerificationEmail(user.getEmail());

        // send welcome email
        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

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
    
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User", "email", email));

        // delete old tokens
        resetTokenRepository.deleteByUserId(user.getId());

        // create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken =
                new PasswordResetToken(token, user, 30);
        resetTokenRepository.save(resetToken);

        // send email
        emailService.sendForgotPasswordEmail(
                user.getEmail(),
                user.getFullName(),
                token);
    }

    // ── RESET PASSWORD ──────────────────────────────────
    public void resetPassword(String token,
                               String newPassword) {
        PasswordResetToken resetToken =
                resetTokenRepository.findByToken(token)
                .orElseThrow(() ->
                    new RuntimeException(
                            "Invalid or expired token!"));

        if (resetToken.isExpired()) {
            throw new RuntimeException(
                    "Token has expired! Request a new one.");
        }

        if (resetToken.isUsed()) {
            throw new RuntimeException(
                    "Token already used!");
        }

        User user = resetToken.getUser();
        user.setPassword(
                passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }

    // ── SEND EMAIL VERIFICATION ─────────────────────────
    public void sendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User", "email", email));

        // delete old tokens
        verifyTokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verifyToken =
                new EmailVerificationToken(token, user, 24);
        verifyTokenRepository.save(verifyToken);

        emailService.sendEmailVerificationEmail(
                user.getEmail(),
                user.getFullName(),
                token);
    }

    // ── VERIFY EMAIL ────────────────────────────────────
    public void verifyEmail(String token) {
        EmailVerificationToken verifyToken =
                verifyTokenRepository.findByToken(token)
                .orElseThrow(() ->
                    new RuntimeException(
                            "Invalid verification link!"));

        if (verifyToken.isExpired()) {
            throw new RuntimeException(
                    "Link expired! Request a new one.");
        }

        if (verifyToken.isUsed()) {
            throw new RuntimeException(
                    "Already verified!");
        }

        User user = verifyToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verifyToken.setUsed(true);
        verifyTokenRepository.save(verifyToken);
    }
}