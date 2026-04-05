import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { QuizService } from '../../../core/services/quiz.service';

@Component({
  selector: 'app-take-quiz',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './take-quiz.component.html',
  styleUrl: './take-quiz.component.css'
})
export class TakeQuizComponent implements OnInit, OnDestroy {

  quiz: any = null;
  questions: any[] = [];
  currentIndex = 0;
  answers: { [key: number]: string } = {};
  timeLeft = 0;
  timer: any;
  isLoading = true;
  isSubmitting = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private quizService: QuizService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadQuiz(+id);
    }
  }

  loadQuiz(id: number): void {
    this.quizService.getQuizById(id).subscribe({
      next: (res) => {
        this.quiz = res.data;
        this.timeLeft = this.quiz.timeLimitMinutes * 60;
        this.loadQuestions(id);
      }
    });
  }

  loadQuestions(quizId: number): void {
    this.quizService.getQuestions(quizId).subscribe({
      next: (res) => {
        this.questions = res.data || [];
        this.isLoading = false;
        this.startTimer();
      }
    });
  }

  startTimer(): void {
    this.timer = setInterval(() => {
      this.timeLeft--;
      if (this.timeLeft <= 0) {
        this.submitQuiz();
      }
    }, 1000);
  }

  selectAnswer(questionId: number, option: string): void {
    this.answers[questionId] = option;
  }

  get currentQuestion(): any {
    return this.questions[this.currentIndex];
  }

  get progress(): number {
    return ((this.currentIndex + 1) / this.questions.length) * 100;
  }

  get answeredCount(): number {
    return Object.keys(this.answers).length;
  }

  get formattedTime(): string {
    const mins = Math.floor(this.timeLeft / 60);
    const secs = this.timeLeft % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  }

  get timeClass(): string {
    if (this.timeLeft < 60) return 'text-danger';
    if (this.timeLeft < 180) return 'text-warning';
    return 'text-success';
  }

  next(): void {
    if (this.currentIndex < this.questions.length - 1) {
      this.currentIndex++;
    }
  }

  previous(): void {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  goTo(index: number): void {
    this.currentIndex = index;
  }

  submitQuiz(): void {
    clearInterval(this.timer);
    this.isSubmitting = true;

    const request = {
      quizId: this.quiz.id,
      answers: this.answers
    };

    this.quizService.submitAttempt(request).subscribe({
      next: (res) => {
        this.router.navigate(['/quiz/result', res.data.id],
          { state: { result: res.data } });
      },
      error: () => {
        this.isSubmitting = false;
      }
    });
  }

  ngOnDestroy(): void {
    clearInterval(this.timer);
  }
}