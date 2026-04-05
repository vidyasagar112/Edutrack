import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsService } from '../../core/services/analytics.service';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css'
})
export class AnalyticsComponent implements OnInit {

  analytics: any = null;
  isLoading = true;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.analyticsService.getMyAnalytics().subscribe({
      next: (res) => {
        this.analytics = res.data;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  getLevelColor(level: string): string {
    const colors: any = {
      'STRONG' : 'bg-success',
      'AVERAGE': 'bg-warning',
      'WEAK'   : 'bg-danger'
    };
    return colors[level] || 'bg-secondary';
  }

  getLevelIcon(level: string): string {
    const icons: any = {
      'STRONG' : '💪',
      'AVERAGE': '📈',
      'WEAK'   : '⚠️'
    };
    return icons[level] || '📊';
  }

  getProgressColor(percent: number): string {
    if (percent >= 75) return 'bg-success';
    if (percent >= 50) return 'bg-warning';
    return 'bg-danger';
  }
}