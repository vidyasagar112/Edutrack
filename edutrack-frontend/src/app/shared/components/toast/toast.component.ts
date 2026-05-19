import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService, Toast }
  from '../../../core/services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.css'
})
export class ToastComponent implements OnInit {

  toasts: Toast[] = [];

  constructor(private toastService: ToastService) {}

  ngOnInit(): void {
    this.toastService.toasts$.subscribe(toasts => {
      this.toasts = toasts;
    });
  }

  remove(id: number): void {
    this.toastService.remove(id);
  }

  getIcon(type: string): string {
    const icons: any = {
      success : '✅',
      error   : '❌',
      warning : '⚠️',
      info    : 'ℹ️'
    };
    return icons[type] || 'ℹ️';
  }

  getBgClass(type: string): string {
    const classes: any = {
      success : 'toast-success',
      error   : 'toast-error',
      warning : 'toast-warning',
      info    : 'toast-info'
    };
    return classes[type] || 'toast-info';
  }
}