import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: number;
  email: string;
  fullName: string;
  roles: string[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly API = `${environment.apiUrl}/auth`;
  private currentUserSubject =
    new BehaviorSubject<AuthResponse | null>(
      this.loadUserFromStorage());

  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  // ── Auth API Calls ──────────────────────────────────

  register(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.API}/register`, request)
      .pipe(tap((res: any) => {
        if (res?.success) {
          this.saveUser(res.data, false);
        }
      }));
  }

  login(request: LoginRequest,
        rememberMe = false): Observable<any> {
    return this.http.post(`${this.API}/login`, request)
      .pipe(tap((res: any) => {
        if (res?.success) {
          this.saveUser(res.data, rememberMe);
        }
      }));
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(
      `${this.API}/forgot-password`, { email });
  }

  resetPassword(token: string,
                newPassword: string): Observable<any> {
    return this.http.post(
      `${this.API}/reset-password`,
      { token, newPassword });
  }

  verifyEmail(token: string): Observable<any> {
    return this.http.get(
      `${this.API}/verify-email?token=${token}`);
  }

  resendVerification(email: string): Observable<any> {
    return this.http.post(
      `${this.API}/resend-verification`, { email });
  }

  // ── Session Management ──────────────────────────────

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    sessionStorage.removeItem('currentUser');
    sessionStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

  private saveUser(user: AuthResponse,
                   rememberMe: boolean): void {
    const storage = rememberMe
      ? localStorage : sessionStorage;
    storage.setItem('currentUser',
      JSON.stringify(user));
    storage.setItem('token', user.token);
    this.currentUserSubject.next(user);
  }

  private loadUserFromStorage(): AuthResponse | null {
    const raw = localStorage.getItem('currentUser')
      || sessionStorage.getItem('currentUser');
    return raw ? JSON.parse(raw) : null;
  }

  // ── Getters ─────────────────────────────────────────

  getToken(): string | null {
    return localStorage.getItem('token')
      || sessionStorage.getItem('token');
  }

  getCurrentUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }

  isInstructor(): boolean {
    if (this.isAdmin()) return false;
    return this.hasRole('ROLE_INSTRUCTOR');
  }

  isStudent(): boolean {
    if (this.isAdmin()) return false;
    return this.hasRole('ROLE_STUDENT');
  }

  private hasRole(role: string): boolean {
    return this.getCurrentUser()
      ?.roles?.includes(role) ?? false;
  }
}