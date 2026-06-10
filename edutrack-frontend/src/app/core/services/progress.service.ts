import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProgressService {

  private readonly API =
    `${environment.apiUrl}/progress`;

  constructor(private http: HttpClient) {}

  // ── Student ─────────────────────────────────────────

  getMyProgress(): Observable<any> {
    return this.http.get(`${this.API}/my`);
  }

  // ── Instructor / Admin ──────────────────────────────

  getStudentProgress(
      studentId: number): Observable<any> {
    return this.http.get(
      `${this.API}/student/${studentId}`);
  }

  getCourseProgress(courseId: number): Observable<any> {
    return this.http.get(
      `${this.API}/course/${courseId}`);
  }
}