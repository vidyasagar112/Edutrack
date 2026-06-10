import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RatingService {

  private readonly API =
    `${environment.apiUrl}/ratings`;

  constructor(private http: HttpClient) {}

  // ── Public ──────────────────────────────────────────

  getCourseRatings(courseId: number): Observable<any> {
    return this.http.get(
      `${this.API}/course/${courseId}`);
  }

  // ── Student ─────────────────────────────────────────

  rateCourse(courseId: number,
             rating: number,
             review: string): Observable<any> {
    return this.http.post(
      `${this.API}/course/${courseId}`,
      { rating, review });
  }

  getMyRating(courseId: number): Observable<any> {
    return this.http.get(
      `${this.API}/course/${courseId}/my`);
  }
}