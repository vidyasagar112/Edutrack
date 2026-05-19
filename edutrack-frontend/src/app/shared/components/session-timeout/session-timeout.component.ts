import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-session-timeout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './session-timeout.component.html',
  styleUrl: './session-timeout.component.css'
})
export class SessionTimeoutComponent
    implements OnInit, OnDestroy {

  showWarning = false;
  countdown = 60;
  private warningTimer: any;
  private countdownTimer: any;
  private readonly TIMEOUT_MS = 25 * 60 * 1000; // 25 min
  private readonly WARNING_MS = 24 * 60 * 1000; // warn at 24min

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.startTimer();
      this.setupActivityListeners();
    }
  }

  startTimer(): void {
    clearTimeout(this.warningTimer);
    this.showWarning = false;

    this.warningTimer = setTimeout(() => {
      this.showWarning = true;
      this.countdown = 60;
      this.startCountdown();
    }, this.WARNING_MS);
  }

  startCountdown(): void {
    clearInterval(this.countdownTimer);
    this.countdownTimer = setInterval(() => {
      this.countdown--;
      if (this.countdown <= 0) {
        this.logout();
      }
    }, 1000);
  }

  setupActivityListeners(): void {
    const events = ['click', 'keypress',
                    'scroll', 'mousemove'];
    events.forEach(event => {
      document.addEventListener(event, () => {
        if (!this.showWarning) {
          this.startTimer();
        }
      });
    });
  }

  stayLoggedIn(): void {
    this.showWarning = false;
    clearInterval(this.countdownTimer);
    this.startTimer();
  }

  logout(): void {
    clearTimeout(this.warningTimer);
    clearInterval(this.countdownTimer);
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    clearTimeout(this.warningTimer);
    clearInterval(this.countdownTimer);
  }
}