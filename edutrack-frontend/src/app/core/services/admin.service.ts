import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<any> {
    return this.http.get(`${this.apiUrl}/admin/users`);
  }

  getAllCourses(): Observable<any> {
    return this.http.get(`${this.apiUrl}/courses`);
  }

  getAllEnrollments(): Observable<any> {
    return this.http.get(`${this.apiUrl}/admin/enrollments`);
  }

  sendEmail(userId: number, subject: string,
            body: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/email/send/${userId}`,
      { subject, body });
  }
  deleteUser(userId: number): Observable<any> {
  return this.http.delete(
    `${this.apiUrl}/admin/users/${userId}`);
}
}