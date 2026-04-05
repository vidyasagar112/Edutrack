import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../../core/services/course.service';
import { EnrollmentService } from '../../../core/services/enrollment.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './course-list.component.html',
  styleUrl: './course-list.component.css'
})
export class CourseListComponent implements OnInit {

  courses: any[] = [];
  filteredCourses: any[] = [];
  searchKeyword = '';
  isLoading = true;
  isStudent = false;
  enrollingCourseId: number | null = null;
  successMessage = '';
  errorMessage = '';

  constructor(
    private courseService: CourseService,
    private enrollmentService: EnrollmentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isStudent = this.authService.isStudent();
    this.loadCourses();
  }

  loadCourses(): void {
    this.courseService.getAllPublished().subscribe({
      next: (res) => {
        this.courses = res.data || [];
        this.filteredCourses = this.courses;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  search(): void {
    if (!this.searchKeyword.trim()) {
      this.filteredCourses = this.courses;
      return;
    }
    this.filteredCourses = this.courses.filter(c =>
      c.title.toLowerCase().includes(
        this.searchKeyword.toLowerCase()) ||
      c.subject.toLowerCase().includes(
        this.searchKeyword.toLowerCase())
    );
  }

  enroll(courseId: number): void {
    this.enrollingCourseId = courseId;
    this.successMessage = '';
    this.errorMessage = '';

    this.enrollmentService.enroll(courseId).subscribe({
      next: (res) => {
        this.enrollingCourseId = null;
        this.successMessage = 'Enrolled successfully!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        this.enrollingCourseId = null;
        this.errorMessage =
          err.error?.message || 'Enrollment failed!';
        setTimeout(() => this.errorMessage = '', 3000);
      }
    });
  }

  getSubjectColor(subject: string): string {
    const colors: any = {
      'Java'      : 'bg-danger',
      'SQL'       : 'bg-primary',
      'Spring'    : 'bg-success',
      'Angular'   : 'bg-warning',
      'Python'    : 'bg-info',
      'React'     : 'bg-dark'
    };
    return colors[subject] || 'bg-secondary';
  }
}