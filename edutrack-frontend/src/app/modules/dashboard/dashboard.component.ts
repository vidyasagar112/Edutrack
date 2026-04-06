import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { EnrollmentService } from '../../core/services/enrollment.service';
import { QuizService } from '../../core/services/quiz.service';
import { ProgressService } from '../../core/services/progress.service';
import { CourseService } from '../../core/services/course.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  fullName = '';
  isStudent = false;
  isInstructor = false;
  isAdmin = false;

  // student data
  enrollments: any[] = [];
  recentAttempts: any[] = [];

  // instructor data
  myCourses: any[] = [];

  // stats
  totalEnrolled = 0;
  completedCourses = 0;
  totalAttempts = 0;
  averageScore = 0;

  isLoading = true;

  constructor(
    private authService: AuthService,
    private enrollmentService: EnrollmentService,
    private quizService: QuizService,
    private progressService: ProgressService,
    private courseService: CourseService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    this.fullName = user?.fullName || '';
    this.isStudent = this.authService.isStudent();
    this.isInstructor = this.authService.isInstructor();
    this.isAdmin = this.authService.isAdmin();

    if (this.isAdmin) {
      this.router.navigate(['/admin']);
    } else if (this.isStudent) {
      this.loadStudentData();
    } else if (this.isInstructor) {
      this.loadInstructorData();
    }
  }

  loadStudentData(): void {
    this.enrollmentService.getMyEnrollments().subscribe({
      next: (res) => {
        this.enrollments = res.data || [];
        this.totalEnrolled = this.enrollments.length;
        this.completedCourses = this.enrollments
          .filter((e: any) => e.status === 'COMPLETED').length;
      }
    });

    this.quizService.getMyAttempts().subscribe({
      next: (res) => {
        this.recentAttempts = (res.data || []).slice(0, 5);
        this.totalAttempts = res.data?.length || 0;
        if (res.data?.length > 0) {
          const total = res.data.reduce(
            (sum: number, a: any) => sum + a.percentage, 0);
          this.averageScore = Math.round(
            total / res.data.length);
        }
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  loadInstructorData(): void {
    this.courseService.getMyCourses().subscribe({
      next: (res) => {
        this.myCourses = res.data || [];
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  getTotalStudents(): number {
    return this.myCourses.reduce(
      (sum, c) => sum + (c.enrollmentCount || 0), 0);
  }

  getPublishedCount(): number {
    return this.myCourses.filter(c => c.published).length;
  }

  getProgressColor(percent: number): string {
    if (percent >= 75) return 'bg-success';
    if (percent >= 50) return 'bg-warning';
    return 'bg-danger';
  }
}