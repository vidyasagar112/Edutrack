import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CourseService } from '../../../core/services/course.service';

@Component({
  selector: 'app-create-course',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './create-course.component.html',
  styleUrl: './create-course.component.css'
})
export class CreateCourseComponent {

  title = '';
  description = '';
  subject = '';
  thumbnailUrl = '';
  published = false;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  subjects = [
    'Java', 'Python', 'SQL', 'Spring Boot',
    'Angular', 'React', 'JavaScript',
    'Data Science', 'Machine Learning', 'Other'
  ];

  constructor(
    private courseService: CourseService,
    private router: Router
  ) {}

  createCourse(): void {
    if (!this.title || !this.subject) {
      this.errorMessage = 'Title and Subject are required!';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.courseService.createCourse({
      title      : this.title,
      description: this.description,
      subject    : this.subject,
      thumbnailUrl: this.thumbnailUrl,
      published  : this.published
    }).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success) {
          this.successMessage = 'Course created successfully!';
          setTimeout(() => {
            this.router.navigate(['/courses', res.data.id]);
          }, 1500);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage =
          err.error?.message || 'Failed to create course!';
      }
    });
  }
}