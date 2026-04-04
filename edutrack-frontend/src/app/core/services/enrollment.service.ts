import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  enroll(courseId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/enrollments`, { courseId });
  }

  getMyEnrollments(): Observable<any> {
    return this.http.get(`${this.apiUrl}/enrollments/my`);
  }

  getEnrollmentsByCourse(courseId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/enrollments/course/${courseId}`);
  }

  updateProgress(enrollmentId: number, percent: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/enrollments/${enrollmentId}/progress?percent=${percent}`, {});
  }

  dropEnrollment(enrollmentId: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/enrollments/${enrollmentId}/drop`, {});
  }
}