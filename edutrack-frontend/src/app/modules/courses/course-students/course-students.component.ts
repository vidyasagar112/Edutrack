import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { EnrollmentService } from '../../../core/services/enrollment.service';

@Component({
  selector: 'app-course-students',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './course-students.component.html',
  styleUrl: './course-students.component.css'
})
export class CourseStudentsComponent implements OnInit {

  courseId = 0;
  enrollments: any[] = [];
  isLoading = true;
  courseName = '';

  constructor(
    private route: ActivatedRoute,
    private enrollmentService: EnrollmentService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('courseId');
    if (id) {
      this.courseId = +id;
      this.loadStudents();
    }
  }

  loadStudents(): void {
    this.enrollmentService
      .getEnrollmentsByCourse(this.courseId)
      .subscribe({
        next: (res) => {
          this.enrollments = res.data || [];
          if (this.enrollments.length > 0) {
            this.courseName =
              this.enrollments[0].courseTitle;
          }
          this.isLoading = false;
        },
        error: () => { this.isLoading = false; }
      });
  }

  getStatusColor(status: string): string {
    const colors: any = {
      'ACTIVE'   : 'bg-primary',
      'COMPLETED': 'bg-success',
      'DROPPED'  : 'bg-danger'
    };
    return colors[status] || 'bg-secondary';
  }
}