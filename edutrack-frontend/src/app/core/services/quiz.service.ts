import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { QuizAttemptRequest } from '../../shared/models/quiz';

@Injectable({
  providedIn: 'root'
})
export class QuizService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getQuizzesByCourse(courseId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/quizzes/course/${courseId}`);
  }

  getQuizById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/quizzes/${id}`);
  }

  getQuestions(quizId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/quizzes/${quizId}/questions`);
  }

  submitAttempt(request: QuizAttemptRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/quizzes/attempt`, request);
  }

  getMyAttempts(): Observable<any> {
    return this.http.get(`${this.apiUrl}/quizzes/attempts/my`);
  }

  createQuiz(request: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/quizzes`, request);
  }

  addQuestion(quizId: number, request: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/quizzes/${quizId}/questions`, request);
  }

  deleteQuiz(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/quizzes/${id}`);
  }
}