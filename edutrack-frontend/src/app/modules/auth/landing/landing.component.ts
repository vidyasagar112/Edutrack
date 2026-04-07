import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterModule],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent implements OnInit {

  features = [
    { icon: '🎓', title: 'Quality Courses', description: 'Learn from expert instructors with structured courses' },
    { icon: '📝', title: 'Interactive Quizzes', description: 'Test your knowledge with MCQ quizzes after each topic' },
    { icon: '📊', title: 'Progress Tracking', description: 'Track your learning progress across all enrolled courses' },
    { icon: '🤖', title: 'AI Analytics', description: 'Get AI-powered suggestions to improve your performance' }
  ];

  stats = [
    { value: '50+', label: 'Courses' },
    { value: '1000+', label: 'Students' },
    { value: '95%', label: 'Pass Rate' },
    { value: '20+', label: 'Instructors' }
  ];

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // if already logged in redirect to correct page
    if (this.authService.isLoggedIn()) {
      if (this.authService.isAdmin()) {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/dashboard']);
      }
    }
  }

  getStarted(): void {
    if (this.authService.isLoggedIn()) {
      if (this.authService.isAdmin()) {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/dashboard']);
      }
    } else {
      this.router.navigate(['/register']);
    }
  }
}