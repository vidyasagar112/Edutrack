import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SectionService {

  private readonly API =
    `${environment.apiUrl}/sections`;

  constructor(private http: HttpClient) {}

  // ── Public ──────────────────────────────────────────

  getSectionsByCourse(
      courseId: number): Observable<any> {
    return this.http.get(
      `${this.API}/course/${courseId}`);
  }

  // ── Student ─────────────────────────────────────────

  markComplete(sectionId: number): Observable<any> {
    return this.http.patch(
      `${this.API}/${sectionId}/complete`, {});
  }

  unmarkComplete(sectionId: number): Observable<any> {
    return this.http.patch(
      `${this.API}/${sectionId}/uncomplete`, {});
  }

  // ── Instructor ──────────────────────────────────────

  addSection(courseId: number,
             data: {
               title: string;
               description?: string;
               sectionOrder: number;
               durationMinutes?: number;
               contentUrl?: string;
             }): Observable<any> {
    return this.http.post(
      `${this.API}/course/${courseId}`, data);
  }

  updateSection(sectionId: number,
                data: any): Observable<any> {
    return this.http.put(
      `${this.API}/${sectionId}`, data);
  }

  deleteSection(sectionId: number): Observable<any> {
    return this.http.delete(
      `${this.API}/${sectionId}`);
  }
}