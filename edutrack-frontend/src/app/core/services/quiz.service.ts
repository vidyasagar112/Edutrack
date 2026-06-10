import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class QuizService {

  private readonly API = `${environment.apiUrl}/quizzes`;

  constructor(private http: HttpClient) {}

  // ── Public ──────────────────────────────────────────

  getQuizzesByCourse(courseId: number): Observable<any> {
    return this.http.get(
      `${this.API}/course/${courseId}`);
  }

  getQuizById(id: number): Observable<any> {
    return this.http.get(`${this.API}/${id}`);
  }

  getQuestions(quizId: number): Observable<any> {
    return this.http.get(
      `${this.API}/${quizId}/questions`);
  }

  // ── Student ─────────────────────────────────────────

  submitAttempt(request: {
    quizId: number;
    answers: { [key: number]: string };
  }): Observable<any> {
    return this.http.post(
      `${this.API}/attempt`, request);
  }

  getMyAttempts(): Observable<any> {
    return this.http.get(`${this.API}/attempts/my`);
  }

  // ── Instructor ──────────────────────────────────────

  createQuiz(data: any): Observable<any> {
    return this.http.post(this.API, data);
  }

  addQuestion(quizId: number,
              data: any): Observable<any> {
    return this.http.post(
      `${this.API}/${quizId}/questions`, data);
  }

  updateQuiz(id: number,
             data: any): Observable<any> {
    return this.http.put(`${this.API}/${id}`, data);
  }

  deleteQuiz(id: number): Observable<any> {
    return this.http.delete(`${this.API}/${id}`);
  }

  deleteQuestion(questionId: number): Observable<any> {
    return this.http.delete(
      `${this.API}/questions/${questionId}`);
  }

  getQuizAttempts(quizId: number): Observable<any> {
    return this.http.get(
      `${this.API}/${quizId}/attempts`);
  }
}