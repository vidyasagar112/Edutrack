import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProfileService {

  private readonly API =
    `${environment.apiUrl}/profile`;

  constructor(private http: HttpClient) {}

  // ── Own Profile ─────────────────────────────────────

  getMyProfile(): Observable<any> {
    return this.http.get(`${this.API}/me`);
  }

  updateProfile(data: {
    firstName?: string;
    middleName?: string;
    lastName?: string;
    prnNumber?: string;
    mothersName?: string;
    dateOfBirth?: string | null;
    category?: string;
    caste?: string;
    fatherAnnualIncome?: number | null;
    phoneNumber?: string;
    address?: string;
  }): Observable<any> {
    return this.http.put(`${this.API}/me`, data);
  }

  // ── Instructor / Admin ──────────────────────────────

  getProfileById(id: number): Observable<any> {
    return this.http.get(`${this.API}/${id}`);
  }
}