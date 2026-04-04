import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Course, CourseRequest } from '../../shared/models/course';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllPublished(): Observable<any> {
    return this.http.get(`${this.apiUrl}/courses`);
  }

  getCourseById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/courses/${id}`);
  }

  searchCourses(keyword: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/courses/search?keyword=${keyword}`);
  }

  getMyCourses(): Observable<any> {
    return this.http.get(`${this.apiUrl}/courses/my-courses`);
  }

  createCourse(request: CourseRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/courses`, request);
  }

  updateCourse(id: number, request: CourseRequest): Observable<any> {
    return this.http.put(`${this.apiUrl}/courses/${id}`, request);
  }

  deleteCourse(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/courses/${id}`);
  }
}