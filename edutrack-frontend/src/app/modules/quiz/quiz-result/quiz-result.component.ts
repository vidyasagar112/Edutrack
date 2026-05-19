import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { QuizService } from '../../../core/services/quiz.service';

@Component({
  selector: 'app-quiz-result',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './quiz-result.component.html',
  styleUrl: './quiz-result.component.css'
})
export class QuizResultComponent implements OnInit {

  result: any = null;
  questions: any[] = [];
  submittedAnswers: { [key: number]: string } = {};
  isLoading = false;

  constructor(
    private router: Router,
    private quizService: QuizService
  ) {}

  ngOnInit(): void {
    const state = history.state;
    this.result = state?.result;
    this.submittedAnswers = state?.answers || {};

    if (!this.result) {
      this.router.navigate(['/dashboard']);
      return;
    }

    this.loadQuestions(this.result.quizId);
  }

  loadQuestions(quizId: number): void {
    this.isLoading = true;
    this.quizService.getQuestions(quizId).subscribe({
      next: (res) => {
        this.questions = res.data || [];
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  get isPassed(): boolean {
    return this.result?.result === 'PASS';
  }

  get scorePercent(): number {
    return this.result?.percentage || 0;
  }

  isCorrect(question: any): boolean {
    const submitted =
      this.submittedAnswers[question.id];
    return submitted === question.correctOption;
  }

  isWrong(question: any): boolean {
    const submitted =
      this.submittedAnswers[question.id];
    return submitted !== undefined &&
           submitted !== question.correctOption;
  }

  wasNotAnswered(question: any): boolean {
    return this.submittedAnswers[question.id]
           === undefined;
  }

  getOptionClass(question: any,
                  option: string): string {
    const submitted =
      this.submittedAnswers[question.id];
    if (option === question.correctOption) {
      return 'option-correct';
    }
    if (option === submitted &&
        submitted !== question.correctOption) {
      return 'option-wrong';
    }
    return 'option-neutral';
  }

  getOptionText(question: any,
                 opt: string): string {
    return question['option' + opt];
  }
}