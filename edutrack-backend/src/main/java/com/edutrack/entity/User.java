package com.edutrack.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min=2,max=60)
    @Column(name="full_name", nullable=false)
    private String fullName;

    // ── NEW PROFILE FIELDS ──────────────────────────
    @Column(name="first_name")
    private String firstName;

    @Column(name="middle_name")
    private String middleName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="prn_number", unique=true)
    private String prnNumber;

    @Column(name="mothers_name")
    private String mothersName;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name="category")
    private String category;

    @Column(name="caste")
    private String caste;

    @Column(name="father_annual_income")
    private Double fatherAnnualIncome;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="address", columnDefinition="TEXT")
    private String address;
    // ────────────────────────────────────────────────

    @NotBlank @Email
    @Column(nullable=false, unique=true)
    private String email;

    @NotBlank @Size(min=6)
    @Column(nullable=false)
    private String password;

    @Column(name="profile_picture")
    private String profilePicture;

    @Column(name="is_enabled", nullable=false)
    private boolean enabled = true;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_roles",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // ── Getters and Setters ─────────────────────────
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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}