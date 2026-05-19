package com.edutrack.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "email_verification_tokens")
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false)
    private boolean used = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public EmailVerificationToken() {}

    public EmailVerificationToken(String token,
                                   User user,
                                   int expiryHours) {
        this.token = token;
        this.user = user;
        this.expiryTime = LocalDateTime.now()
                .plusHours(expiryHours);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryTime);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}