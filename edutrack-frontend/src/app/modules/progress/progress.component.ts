import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProgressService } from '../../core/services/progress.service';

@Component({
  selector: 'app-progress',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './progress.component.html',
  styleUrl: './progress.component.css'
})
export class ProgressComponent implements OnInit {

  progress: any = null;
  isLoading = true;

  constructor(private progressService: ProgressService) {}

  ngOnInit(): void {
    this.progressService.getMyProgress().subscribe({
      next: (res) => {
        this.progress = res.data;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  getStatusColor(status: string): string {
    const colors: any = {
      'ACTIVE'   : 'bg-primary',
      'COMPLETED': 'bg-success',
      'DROPPED'  : 'bg-danger'
    };
    return colors[status] || 'bg-secondary';
  }

  getProgressColor(percent: number): string {
    if (percent >= 75) return 'bg-success';
    if (percent >= 50) return 'bg-warning';
    return 'bg-info';
  }
}