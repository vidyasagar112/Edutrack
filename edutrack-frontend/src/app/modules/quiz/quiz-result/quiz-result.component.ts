import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-quiz-result',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './quiz-result.component.html',
  styleUrl: './quiz-result.component.css'
})
export class QuizResultComponent implements OnInit {

  result: any = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    const nav = this.router.getCurrentNavigation();
    this.result = nav?.extras?.state?.['result'] ||
      history.state?.result;

    if (!this.result) {
      this.router.navigate(['/dashboard']);
    }
  }

  get isPassed(): boolean {
    return this.result?.result === 'PASS';
  }

  get scorePercent(): number {
    return this.result?.percentage || 0;
  }
}