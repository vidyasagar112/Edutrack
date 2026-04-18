import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SectionService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getSectionsByCourse(courseId: number): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/sections/course/${courseId}`);
  }

  addSection(courseId: number, data: any): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/sections/course/${courseId}`, data);
  }

  updateSection(sectionId: number,
                data: any): Observable<any> {
    return this.http.put(
      `${this.apiUrl}/sections/${sectionId}`, data);
  }

  deleteSection(sectionId: number): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/sections/${sectionId}`);
  }

  markComplete(sectionId: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/sections/${sectionId}/complete`, {});
  }

  unmarkComplete(sectionId: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/sections/${sectionId}/uncomplete`, {});
  }
}