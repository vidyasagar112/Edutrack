import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class DocumentService {

  private readonly API =
    `${environment.apiUrl}/documents`;

  constructor(private http: HttpClient) {}

  // ── Public ──────────────────────────────────────────

  getDocuments(courseId: number): Observable<any> {
    return this.http.get(
      `${this.API}/course/${courseId}`);
  }

  getDownloadUrl(documentId: number): string {
    return `${this.API}/download/${documentId}`;
  }

  // ── Instructor ──────────────────────────────────────

  uploadDocument(courseId: number,
                 file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(
      `${this.API}/upload/${courseId}`, formData);
  }

  deleteDocument(documentId: number): Observable<any> {
    return this.http.delete(
      `${this.API}/${documentId}`);
  }
}