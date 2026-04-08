package com.edutrack.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutrack.dto.request.ProfileRequest;
import com.edutrack.dto.response.ProfileResponse;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.repository.UserRepository;

@Service
@Transactional
public class ProfileService {

    @Autowired private UserRepository userRepository;

    // GET own profile
    public ProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", email));
        return mapToResponse(user);
    }

    // GET profile by id — instructor/admin
    public ProfileResponse getProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "id", id));
        return mapToResponse(user);
    }

    // UPDATE own profile
    public ProfileResponse updateProfile(String email,
                                          ProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", email));

        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setPrnNumber(request.getPrnNumber());
        user.setMothersName(request.getMothersName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setCategory(request.getCategory());
        user.setCaste(request.getCaste());
        user.setFatherAnnualIncome(request.getFatherAnnualIncome());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());

        // update full name from first + middle + last
        String fullName = buildFullName(
            request.getFirstName(),
            request.getMiddleName(),
            request.getLastName());
        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName);
        }

        return mapToResponse(userRepository.save(user));
    }

    private String buildFullName(String first,
                                  String middle,
                                  String last) {
        StringBuilder sb = new StringBuilder();
        if (first != null && !first.isBlank())
            sb.append(first.trim());
        if (middle != null && !middle.isBlank())
            sb.append(" ").append(middle.trim());
        if (last != null && !last.isBlank())
            sb.append(" ").append(last.trim());
        return sb.toString();
    }

    public ProfileResponse mapToResponse(User user) {
        ProfileResponse res = new ProfileResponse();
        res.setId(user.getId());
        res.setFullName(user.getFullName());
        res.setFirstName(user.getFirstName());
        res.setMiddleName(user.getMiddleName());
        res.setLastName(user.getLastName());
        res.setEmail(user.getEmail());
        res.setPrnNumber(user.getPrnNumber());
        res.setMothersName(user.getMothersName());
        res.setDateOfBirth(user.getDateOfBirth());
        res.setCategory(user.getCategory());
        res.setCaste(user.getCaste());
        res.setFatherAnnualIncome(user.getFatherAnnualIncome());
        res.setPhoneNumber(user.getPhoneNumber());
        res.setAddress(user.getAddress());
        res.setEnabled(user.isEnabled());
        res.setCreatedAt(user.getCreatedAt());
        res.setRoles(user.getRoles().stream()
                .map(r -> r.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet()));
        return res;
    }
}