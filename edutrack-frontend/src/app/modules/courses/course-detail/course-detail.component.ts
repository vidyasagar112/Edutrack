import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CourseService } from '../../../core/services/course.service';
import { QuizService } from '../../../core/services/quiz.service';
import { EnrollmentService } from '../../../core/services/enrollment.service';
import { AuthService } from '../../../core/services/auth.service';
import { SectionService } from '../../../core/services/section.service';
import { DocumentService } from '../../../core/services/document.service';
import { RatingService } from '../../../core/services/rating.service';

@Component({
  selector: 'app-course-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './course-detail.component.html',
  styleUrl: './course-detail.component.css'
})
export class CourseDetailComponent implements OnInit {

  course: any = null;
  quizzes: any[] = [];
  sections: any[] = [];
  isLoading = true;
  isStudent = false;
  isInstructor = false;
  isEnrolling = false;
  isPublishing = false;
  successMessage = '';
  errorMessage = '';

  // add section form
  showAddSection = false;
  newSectionTitle = '';
  newSectionDescription = '';
  newSectionDuration = 0;
  newSectionUrl = '';
  isAddingSection = false;

  // documents and ratings
  documents: any[] = [];
  ratings: any[] = [];
  myRating: any = null;
  selectedRating = 0;
  reviewText = '';
  isSubmittingRating = false;
  isEnrolled = false;
  isUploadingDoc = false;

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private quizService: QuizService,
    private enrollmentService: EnrollmentService,
    private authService: AuthService,
    private sectionService: SectionService,
    private documentService: DocumentService,
    private ratingService: RatingService
  ) {}

  ngOnInit(): void {
    this.isStudent = this.authService.isStudent();
    this.isInstructor = this.authService.isInstructor();
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
        this.loadSections(id);
        this.loadDocuments(id);
        this.loadRatings(id);
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

  loadSections(courseId: number): void {
    this.sectionService.getSectionsByCourse(courseId)
      .subscribe({
        next: (res) => {
          this.sections = res.data || [];
        }
      });
  }

  loadDocuments(id: number): void {
    this.documentService.getDocuments(id).subscribe({
      next: (res) => {
        this.documents = res.data || [];
      }
    });
  }

  loadRatings(id: number): void {
    this.ratingService.getCourseRatings(id).subscribe({
      next: (res) => {
        this.ratings = res.data || [];
      }
    });
    if (this.isStudent) {
      this.ratingService.getMyRating(id).subscribe({
        next: (res) => {
          if (res.data) {
            this.myRating = res.data;
            this.selectedRating = res.data.rating;
            this.reviewText = res.data.review || '';
          }
        }
      });
    }
  }

  enroll(): void {
    this.isEnrolling = true;
    this.errorMessage = '';
    this.successMessage = '';
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

  togglePublish(): void {
    this.isPublishing = true;
    const updated = {
      title       : this.course.title,
      description : this.course.description,
      subject     : this.course.subject,
      thumbnailUrl: this.course.thumbnailUrl,
      published   : !this.course.published
    };
    this.courseService.updateCourse(
      this.course.id, updated).subscribe({
      next: () => {
        this.isPublishing = false;
        this.course.published = !this.course.published;
        this.successMessage = this.course.published
          ? 'Course published!' : 'Course unpublished!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: () => {
        this.isPublishing = false;
        this.errorMessage = 'Failed to update course!';
      }
    });
  }

  addSection(): void {
    if (!this.newSectionTitle) return;
    this.isAddingSection = true;

    const order = this.sections.length + 1;

    this.sectionService.addSection(this.course.id, {
      title          : this.newSectionTitle,
      description    : this.newSectionDescription,
      sectionOrder   : order,
      durationMinutes: this.newSectionDuration,
      contentUrl     : this.newSectionUrl
    }).subscribe({
      next: (res) => {
        this.isAddingSection = false;
        this.sections.push(res.data);
        this.showAddSection = false;
        this.newSectionTitle = '';
        this.newSectionDescription = '';
        this.newSectionDuration = 0;
        this.newSectionUrl = '';
        this.successMessage = 'Section added!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: () => { this.isAddingSection = false; }
    });
  }

  deleteSection(sectionId: number): void {
    if (!confirm('Delete this section?')) return;
    this.sectionService.deleteSection(sectionId).subscribe({
      next: () => {
        this.sections = this.sections.filter(
          s => s.id !== sectionId);
        this.successMessage = 'Section deleted!';
        setTimeout(() => this.successMessage = '', 3000);
      }
    });
  }

  toggleSectionComplete(section: any): void {
    if (section.completedByStudent) {
      this.sectionService.unmarkComplete(section.id)
        .subscribe({
          next: () => {
            section.completedByStudent = false;
            this.updateLocalProgress();
          }
        });
    } else {
      this.sectionService.markComplete(section.id)
        .subscribe({
          next: () => {
            section.completedByStudent = true;
            this.updateLocalProgress();
          }
        });
    }
  }

  updateLocalProgress(): void {
    const total = this.sections.length;
    const completed = this.sections.filter(
      s => s.completedByStudent).length;
    if (total > 0) {
      this.course.progressPercent =
        Math.round((completed / total) * 100);
    }
  }

  get completedSections(): number {
    return this.sections.filter(
      s => s.completedByStudent).length;
  }

  get progressPercent(): number {
    if (this.sections.length === 0) return 0;
    return Math.round(
      (this.completedSections / this.sections.length) * 100);
  }

  isOwner(): boolean {
    const user = this.authService.getCurrentUser();
    return this.course?.instructorEmail === user?.email;
  }

  getDifficultyColor(difficulty: string): string {
    const colors: any = {
      'EASY'  : 'bg-success',
      'MEDIUM': 'bg-warning',
      'HARD'  : 'bg-danger'
    };
    return colors[difficulty] || 'bg-secondary';
  }

  onDocumentUpload(event: any): void {
    const file = event.target.files[0];
    if (!file) return;
    this.isUploadingDoc = true;
    this.documentService.uploadDocument(
      this.course.id, file).subscribe({
      next: (res) => {
        this.isUploadingDoc = false;
        this.documents.push(res.data);
        this.successMessage = 'Document uploaded!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        this.isUploadingDoc = false;
        this.errorMessage =
          err.error?.message || 'Upload failed!';
      }
    });
  }

  deleteDocument(docId: number): void {
    if (!confirm('Delete this document?')) return;
    this.documentService.deleteDocument(docId).subscribe({
      next: () => {
        this.documents = this.documents.filter(
          d => d.id !== docId);
      }
    });
  }

  submitRating(): void {
    if (this.selectedRating === 0) return;
    this.isSubmittingRating = true;
    this.ratingService.rateCourse(
      this.course.id,
      this.selectedRating,
      this.reviewText).subscribe({
      next: (res) => {
        this.isSubmittingRating = false;
        this.myRating = res.data;
        this.loadRatings(this.course.id);
        this.successMessage = 'Rating submitted! Thank you!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: () => {
        this.isSubmittingRating = false;
      }
    });
  }

  getFileIcon(fileType: string): string {
    if (!fileType) return '📄';
    if (fileType.includes('pdf')) return '📕';
    if (fileType.includes('word') ||
        fileType.includes('msword')) return '📘';
    if (fileType.includes('excel') ||
        fileType.includes('sheet')) return '📗';
    if (fileType.includes('powerpoint') ||
        fileType.includes('presentation')) return '📙';
    return '📄';
  }

  getDownloadUrl(docId: number): string {
    return this.documentService.getDownloadUrl(docId);
  }

  getStars(rating: number): string {
    const full = Math.round(rating);
    return '⭐'.repeat(full) + '☆'.repeat(5 - full);
  }
}