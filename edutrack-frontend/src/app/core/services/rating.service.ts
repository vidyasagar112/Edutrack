import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RatingService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  rateCourse(courseId: number,
             rating: number,
             review: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/ratings/course/${courseId}`,
      { rating, review });
  }

  getCourseRatings(courseId: number): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/ratings/course/${courseId}`);
  }

  getMyRating(courseId: number): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/ratings/course/${courseId}/my`);
  }
}