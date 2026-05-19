import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class DocumentService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  uploadDocument(courseId: number,
                 file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(
      `${this.apiUrl}/documents/upload/${courseId}`,
      formData);
  }

  getDocuments(courseId: number): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/documents/course/${courseId}`);
  }

  deleteDocument(documentId: number): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/documents/${documentId}`);
  }

  getDownloadUrl(documentId: number): string {
    return `${this.apiUrl}/documents/download/${documentId}`;
  }
}