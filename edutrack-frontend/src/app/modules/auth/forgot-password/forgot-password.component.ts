import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {

  email = '';
  isLoading = false;
  successMessage = '';
  errorMessage = '';
  emailSent = false;

  constructor(private authService: AuthService) {}

  submit(): void {
    if (!this.email) {
      this.errorMessage = 'Please enter your email!';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.forgotPassword(this.email)
      .subscribe({
        next: () => {
          this.isLoading = false;
          this.emailSent = true;
          this.successMessage =
            `Reset link sent to ${this.email}!
             Check your inbox.`;
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage =
            err.error?.message ||
            'Email not found!';
        }
      });
  }
}