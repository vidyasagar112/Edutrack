import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CourseService {

  private readonly API = `${environment.apiUrl}/courses`;

  constructor(private http: HttpClient) {}

  // ── Public ──────────────────────────────────────────

  getAllPublished(): Observable<any> {
    return this.http.get(this.API);
  }

  getCourseById(id: number): Observable<any> {
    return this.http.get(`${this.API}/${id}`);
  }

  searchCourses(keyword: string): Observable<any> {
    return this.http.get(
      `${this.API}/search?keyword=${keyword}`);
  }

  searchWithFilters(keyword: string,
                    category: string,
                    subject: string): Observable<any> {
    const params: string[] = [];
    if (keyword) params.push(`keyword=${keyword}`);
    if (category) params.push(`category=${category}`);
    if (subject) params.push(`subject=${subject}`);
    const query = params.length
      ? '?' + params.join('&') : '';
    return this.http.get(
      `${this.API}/search-filter${query}`);
  }

  getCategories(): Observable<any> {
    return this.http.get(`${this.API}/categories`);
  }

  getSubjects(): Observable<any> {
    return this.http.get(`${this.API}/subjects`);
  }

  // ── Instructor ──────────────────────────────────────

  getMyCourses(): Observable<any> {
    return this.http.get(`${this.API}/my-courses`);
  }

  createCourse(data: any): Observable<any> {
    return this.http.post(this.API, data);
  }

  updateCourse(id: number,
               data: any): Observable<any> {
    return this.http.put(`${this.API}/${id}`, data);
  }

  deleteCourse(id: number): Observable<any> {
    return this.http.delete(`${this.API}/${id}`);
  }

  uploadThumbnail(courseId: number,
                  file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(
      `${this.API}/${courseId}/thumbnail`,
      formData);
  }
}