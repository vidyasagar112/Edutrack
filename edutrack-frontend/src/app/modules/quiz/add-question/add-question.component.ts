import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { QuizService } from '../../../core/services/quiz.service';

@Component({
  selector: 'app-add-question',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './add-question.component.html',
  styleUrl: './add-question.component.css'
})
export class AddQuestionComponent implements OnInit {

  quizId = 0;
  questionText = '';
  optionA = '';
  optionB = '';
  optionC = '';
  optionD = '';
  correctOption = '';
  explanation = '';

  isLoading = false;
  errorMessage = '';
  successMessage = '';
  addedQuestions: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private quizService: QuizService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('quizId');
    if (id) {
      this.quizId = +id;
      this.loadExistingQuestions();
    }
  }

  loadExistingQuestions(): void {
    this.quizService.getQuestions(this.quizId).subscribe({
      next: (res) => {
        this.addedQuestions = res.data || [];
      }
    });
  }

  addQuestion(): void {
    if (!this.questionText || !this.optionA ||
        !this.optionB || !this.optionC ||
        !this.optionD || !this.correctOption) {
      this.errorMessage = 'All fields are required!';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.quizService.addQuestion(this.quizId, {
      questionText : this.questionText,
      optionA      : this.optionA,
      optionB      : this.optionB,
      optionC      : this.optionC,
      optionD      : this.optionD,
      correctOption: this.correctOption,
      explanation  : this.explanation
    }).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.successMessage =
          `Question ${this.addedQuestions.length + 1} added!`;
        this.addedQuestions.push(res.data);
        this.clearForm();
        setTimeout(() => this.successMessage = '', 2000);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage =
          err.error?.message || 'Failed to add question!';
      }
    });
  }

  clearForm(): void {
    this.questionText = '';
    this.optionA = '';
    this.optionB = '';
    this.optionC = '';
    this.optionD = '';
    this.correctOption = '';
    this.explanation = '';
  }

  finishAndGoToCourse(): void {
    this.router.navigate(['/dashboard']);
  }
}