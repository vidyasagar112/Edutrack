import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProfileService } from '../../../core/services/profile.service';

@Component({
  selector: 'app-my-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.css'
})
export class MyProfileComponent implements OnInit {

  profile: any = null;
  isLoading = true;
  isEditing = false;
  isSaving = false;
  successMessage = '';
  errorMessage = '';

  // form fields
  firstName = '';
  middleName = '';
  lastName = '';
  prnNumber = '';
  mothersName = '';
  dateOfBirth = '';
  category = '';
  caste = '';
  fatherAnnualIncome: number | null = null;
  phoneNumber = '';
  address = '';

  categories = [
    'General', 'OBC', 'SC', 'ST',
    'NT-A', 'NT-B', 'NT-C', 'NT-D',
    'SBC', 'VJNT', 'EWS'
  ];

  constructor(private profileService: ProfileService) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.profileService.getMyProfile().subscribe({
      next: (res) => {
        this.profile = res.data;
        this.fillForm();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  fillForm(): void {
    this.firstName = this.profile.firstName || '';
    this.middleName = this.profile.middleName || '';
    this.lastName = this.profile.lastName || '';
    this.prnNumber = this.profile.prnNumber || '';
    this.mothersName = this.profile.mothersName || '';
    this.dateOfBirth = this.profile.dateOfBirth || '';
    this.category = this.profile.category || '';
    this.caste = this.profile.caste || '';
    this.fatherAnnualIncome = this.profile.fatherAnnualIncome || null;
    this.phoneNumber = this.profile.phoneNumber || '';
    this.address = this.profile.address || '';
  }

  saveProfile(): void {
    this.isSaving = true;
    this.successMessage = '';
    this.errorMessage = '';

    // PRN Number Validation
    if (this.prnNumber) {
      // Check if PRN contains only digits
      if (!/^\d+$/.test(this.prnNumber)) {
        this.isSaving = false;
        this.errorMessage = 'PRN Number must contain only digits!';
        return;
      }

      // Check if PRN is exactly 14 digits
      if (this.prnNumber.length !== 14) {
        this.isSaving = false;
        this.errorMessage = 'PRN Number must be exactly 14 digits!';
        return;
      }

      // Check if PRN doesn't start with 0
      if (this.prnNumber.startsWith('0')) {
        this.isSaving = false;
        this.errorMessage = 'PRN Number cannot start with 0!';
        return;
      }
    }

    this.profileService.updateProfile({
      firstName: this.firstName,
      middleName: this.middleName,
      lastName: this.lastName,
      prnNumber: this.prnNumber,
      mothersName: this.mothersName,
      dateOfBirth: this.dateOfBirth || null,
      category: this.category,
      caste: this.caste,
      fatherAnnualIncome: this.fatherAnnualIncome,
      phoneNumber: this.phoneNumber,
      address: this.address
    }).subscribe({
      next: (res) => {
        this.isSaving = false;
        this.profile = res.data;
        this.isEditing = false;
        this.successMessage = 'Profile updated successfully!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err.error?.message || 'Failed to update!';
      }
    });
  }

  isProfileComplete(): boolean {
    return !!(this.profile?.prnNumber &&
              this.profile?.firstName &&
              this.profile?.dateOfBirth);
  }
}