package com.edutrack.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class ProfileResponse {

    private Long id;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String prnNumber;
    private String mothersName;
    private LocalDate dateOfBirth;
    private String category;
    private String caste;
    private Double fatherAnnualIncome;
    private String phoneNumber;
    private String address;
    private boolean enabled;
    private Set<String> roles;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPrnNumber() { return prnNumber; }
    public void setPrnNumber(String prnNumber) { this.prnNumber = prnNumber; }

    public String getMothersName() { return mothersName; }
    public void setMothersName(String mothersName) { this.mothersName = mothersName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCaste() { return caste; }
    public void setCaste(String caste) { this.caste = caste; }

    public Double getFatherAnnualIncome() { return fatherAnnualIncome; }
    public void setFatherAnnualIncome(Double fatherAnnualIncome) { this.fatherAnnualIncome = fatherAnnualIncome; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}