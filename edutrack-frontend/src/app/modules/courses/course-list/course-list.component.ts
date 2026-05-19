import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../../core/services/course.service';
import { EnrollmentService } from '../../../core/services/enrollment.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

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
  categories: string[] = [];
  subjects: string[] = [];

  searchKeyword = '';
  selectedCategory = '';
  selectedSubject = '';
  sortBy = 'latest';

  isLoading = true;
  isStudent = false;
  enrollingCourseId: number | null = null;
  

  constructor(
    private courseService: CourseService,
    private enrollmentService: EnrollmentService,
    private authService: AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.isStudent = this.authService.isStudent();
    this.loadCourses();
    this.loadFilters();
  }

  loadCourses(): void {
    this.courseService.getAllPublished().subscribe({
      next: (res) => {
        this.courses = res.data || [];
        this.applyFilters();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  loadFilters(): void {
    this.courseService.getCategories().subscribe({
      next: (res) => {
        this.categories = res.data || [];
      }
    });
    this.courseService.getSubjects().subscribe({
      next: (res) => {
        this.subjects = res.data || [];
      }
    });
  }

  applyFilters(): void {
    let result = [...this.courses];

    if (this.searchKeyword.trim()) {
      const kw = this.searchKeyword.toLowerCase();
      result = result.filter(c =>
        c.title.toLowerCase().includes(kw) ||
        c.subject?.toLowerCase().includes(kw) ||
        c.category?.toLowerCase().includes(kw)
      );
    }

    if (this.selectedCategory) {
      result = result.filter(
        c => c.category === this.selectedCategory);
    }

    if (this.selectedSubject) {
      result = result.filter(
        c => c.subject?.toLowerCase() ===
             this.selectedSubject.toLowerCase());
    }

    if (this.sortBy === 'rating') {
      result.sort((a, b) =>
        b.averageRating - a.averageRating);
    } else if (this.sortBy === 'popular') {
      result.sort((a, b) =>
        b.enrollmentCount - a.enrollmentCount);
    } else {
      result.sort((a, b) =>
        new Date(b.createdAt).getTime() -
        new Date(a.createdAt).getTime());
    }

    this.filteredCourses = result;
  }

  clearFilters(): void {
    this.searchKeyword = '';
    this.selectedCategory = '';
    this.selectedSubject = '';
    this.sortBy = 'latest';
    this.applyFilters();
  }

  enroll(courseId: number): void {
  this.enrollingCourseId = courseId;

  this.enrollmentService.enroll(courseId).subscribe({
    next: () => {
      this.enrollingCourseId = null;
      this.toast.success('Enrolled successfully! 🎉');
    },
    error: (err) => {
      this.enrollingCourseId = null;
      this.toast.error(
        err.error?.message || 'Enrollment failed!');
    }
  });
}

  getStars(rating: number): string[] {
    return Array(5).fill('').map((_, i) =>
      i < Math.round(rating) ? '⭐' : '☆');
  }

  getSubjectColor(subject: string): string {
    const colors: any = {
      'Java'    : 'bg-danger',
      'SQL'     : 'bg-primary',
      'Spring'  : 'bg-success',
      'Angular' : 'bg-warning',
      'Python'  : 'bg-info',
      'React'   : 'bg-dark'
    };
    return colors[subject] || 'bg-secondary';
  }
}