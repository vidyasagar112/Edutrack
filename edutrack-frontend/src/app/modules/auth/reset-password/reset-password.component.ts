import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit {

  token = '';
  newPassword = '';
  confirmPassword = '';
  isLoading = false;
  successMessage = '';
  errorMessage = '';
  isSuccess = false;
  showPassword = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.token =
      this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) {
      this.errorMessage =
        'Invalid reset link! Please request a new one.';
    }
  }

  submit(): void {
    if (!this.newPassword || !this.confirmPassword) {
      this.errorMessage = 'Please fill in all fields!';
      return;
    }

    if (this.newPassword.length < 6) {
      this.errorMessage =
        'Password must be at least 6 characters!';
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match!';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.resetPassword(
      this.token, this.newPassword).subscribe({
      next: () => {
        this.isLoading = false;
        this.isSuccess = true;
        this.successMessage =
          'Password reset successfully!';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage =
          err.error?.message ||
          'Failed to reset password!';
      }
    });
  }
}