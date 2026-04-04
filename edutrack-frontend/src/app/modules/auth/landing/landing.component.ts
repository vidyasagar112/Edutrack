import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
imports: [CommonModule, RouterLink, RouterModule],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent {

  features = [
    {
      icon: '🎓',
      title: 'Quality Courses',
      description: 'Learn from expert instructors with structured courses'
    },
    {
      icon: '📝',
      title: 'Interactive Quizzes',
      description: 'Test your knowledge with MCQ quizzes after each topic'
    },
    {
      icon: '📊',
      title: 'Progress Tracking',
      description: 'Track your learning progress across all enrolled courses'
    },
    {
      icon: '🤖',
      title: 'AI Analytics',
      description: 'Get AI-powered suggestions to improve your performance'
    }
  ];

  stats = [
    { value: '50+', label: 'Courses' },
    { value: '1000+', label: 'Students' },
    { value: '95%', label: 'Pass Rate' },
    { value: '20+', label: 'Instructors' }
  ];

  constructor(private router: Router) {}

  getStarted(): void {
    this.router.navigate(['/register']);
  }
}