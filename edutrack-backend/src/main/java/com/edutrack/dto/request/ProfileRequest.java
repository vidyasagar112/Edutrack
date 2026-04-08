package com.edutrack.dto.request;

import java.time.LocalDate;

public class ProfileRequest {

    private String firstName;
    private String middleName;
    private String lastName;
    private String prnNumber;
    private String mothersName;
    private LocalDate dateOfBirth;
    private String category;
    private String caste;
    private Double fatherAnnualIncome;
    private String phoneNumber;
    private String address;

    // Getters and Setters
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
}