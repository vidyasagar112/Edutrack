import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CourseService } from '../../../core/services/course.service';
import { QuizService } from '../../../core/services/quiz.service';
import { EnrollmentService } from '../../../core/services/enrollment.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-course-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './course-detail.component.html',
  styleUrl: './course-detail.component.css'
})
export class CourseDetailComponent implements OnInit {

  course: any = null;
  quizzes: any[] = [];
  isLoading = true;
  isStudent = false;
  isEnrolling = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private quizService: QuizService,
    private enrollmentService: EnrollmentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isStudent = this.authService.isStudent();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadCourse(+id);
    }
  }

  loadCourse(id: number): void {
    this.courseService.getCourseById(id).subscribe({
      next: (res) => {
        this.course = res.data;
        this.loadQuizzes(id);
      }
    });
  }

  loadQuizzes(courseId: number): void {
    this.quizService.getQuizzesByCourse(courseId).subscribe({
      next: (res) => {
        this.quizzes = res.data || [];
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  enroll(): void {
    this.isEnrolling = true;
    this.enrollmentService.enroll(this.course.id).subscribe({
      next: () => {
        this.isEnrolling = false;
        this.successMessage = 'Enrolled successfully!';
      },
      error: (err) => {
        this.isEnrolling = false;
        this.errorMessage =
          err.error?.message || 'Enrollment failed!';
      }
    });
  }

  getDifficultyColor(difficulty: string): string {
    const colors: any = {
      'EASY'  : 'bg-success',
      'MEDIUM': 'bg-warning',
      'HARD'  : 'bg-danger'
    };
    return colors[difficulty] || 'bg-secondary';
  }
}