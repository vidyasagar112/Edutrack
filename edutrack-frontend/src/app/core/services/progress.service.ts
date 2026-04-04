import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProgressService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMyProgress(): Observable<any> {
    return this.http.get(`${this.apiUrl}/progress/my`);
  }

  getStudentProgress(studentId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/progress/student/${studentId}`);
  }

  getCourseProgress(courseId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/progress/course/${courseId}`);
  }
}