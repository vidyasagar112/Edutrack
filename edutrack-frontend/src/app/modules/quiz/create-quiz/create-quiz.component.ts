import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { QuizService } from '../../../core/services/quiz.service';

@Component({
  selector: 'app-create-quiz',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './create-quiz.component.html',
  styleUrl: './create-quiz.component.css'
})
export class CreateQuizComponent implements OnInit {

  courseId = 0;
  title = '';
  description = '';
  difficulty = 'MEDIUM';
  timeLimitMinutes = 30;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private quizService: QuizService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('courseId');
    if (id) this.courseId = +id;
  }

  createQuiz(): void {
    if (!this.title || !this.difficulty) {
      this.errorMessage = 'Title and Difficulty are required!';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.quizService.createQuiz({
      title            : this.title,
      description      : this.description,
      difficulty       : this.difficulty,
      timeLimitMinutes : this.timeLimitMinutes,
      courseId         : this.courseId
    }).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success) {
          this.successMessage = 'Quiz created! Now add questions.';
          setTimeout(() => {
            this.router.navigate(
              ['/quiz', res.data.id, 'add-question']);
          }, 1500);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage =
          err.error?.message || 'Failed to create quiz!';
      }
    });
  }
}