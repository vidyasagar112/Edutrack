import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // load user from localStorage on startup
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }

  register(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/register`, request).pipe(
      tap((response: any) => {
        if (response.success) {
          this.saveUser(response.data);
        }
      })
    );
  }

  login(request: LoginRequest,
      rememberMe = false): Observable<any> {
  return this.http.post(
    `${this.apiUrl}/auth/login`, request).pipe(
    tap((response: any) => {
      if (response.success) {
        this.saveUser(response.data, rememberMe);
      }
    })
  );
}

private saveUser(user: AuthResponse,
                  rememberMe = false): void {
  // use sessionStorage if not remember me
  // use localStorage if remember me
  if (rememberMe) {
    localStorage.setItem('currentUser',
      JSON.stringify(user));
    localStorage.setItem('token', user.token);
  } else {
    sessionStorage.setItem('currentUser',
      JSON.stringify(user));
    sessionStorage.setItem('token', user.token);
  }
  this.currentUserSubject.next(user);
}

getToken(): string | null {
  return localStorage.getItem('token') ||
         sessionStorage.getItem('token');
}

getCurrentUser(): AuthResponse | null {
  const saved = localStorage.getItem('currentUser') ||
                sessionStorage.getItem('currentUser');
  return saved ? JSON.parse(saved) : null;
}

logout(): void {
  localStorage.removeItem('currentUser');
  localStorage.removeItem('token');
  sessionStorage.removeItem('currentUser');
  sessionStorage.removeItem('token');
  this.currentUserSubject.next(null);
}

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isStudent(): boolean {
    const user = this.getCurrentUser();
    return user?.roles?.includes('ROLE_STUDENT') ?? false;
  }

  isInstructor(): boolean {
    const user = this.getCurrentUser();
    return user?.roles?.includes('ROLE_INSTRUCTOR') ?? false;
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.roles?.includes('ROLE_ADMIN') ?? false;
  }
  forgotPassword(email: string): Observable<any> {
  return this.http.post(
    `${this.apiUrl}/auth/forgot-password`, { email });
}

resetPassword(token: string,
              newPassword: string): Observable<any> {
  return this.http.post(
    `${this.apiUrl}/auth/reset-password`,
    { token, newPassword });
}

verifyEmail(token: string): Observable<any> {
  return this.http.get(
    `${this.apiUrl}/auth/verify-email?token=${token}`);
}

resendVerification(email: string): Observable<any> {
  return this.http.post(
    `${this.apiUrl}/auth/resend-verification`, { email });
}
}