import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class EnrollmentService {

  private readonly API =
    `${environment.apiUrl}/enrollments`;

  constructor(private http: HttpClient) {}

  // ── Student ─────────────────────────────────────────

  enroll(courseId: number): Observable<any> {
    return this.http.post(this.API, { courseId });
  }

  getMyEnrollments(): Observable<any> {
    return this.http.get(`${this.API}/my`);
  }

  updateProgress(enrollmentId: number,
                 percent: number): Observable<any> {
    return this.http.patch(
      `${this.API}/${enrollmentId}/progress?percent=${percent}`,
      {});
  }

  dropEnrollment(enrollmentId: number): Observable<any> {
    return this.http.patch(
      `${this.API}/${enrollmentId}/drop`, {});
  }

  // ── Instructor ──────────────────────────────────────

  getEnrollmentsByCourse(
      courseId: number): Observable<any> {
    return this.http.get(
      `${this.API}/course/${courseId}`);
  }
}