import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMyAnalytics(): Observable<any> {
    return this.http.get(`${this.apiUrl}/analytics/my`);
  }

  getStudentAnalytics(studentId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/analytics/student/${studentId}`);
  }
}