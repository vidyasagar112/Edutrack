import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProfileService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMyProfile(): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile/me`);
  }

  updateProfile(data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/profile/me`, data);
  }

  getProfileById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile/${id}`);
  }
}