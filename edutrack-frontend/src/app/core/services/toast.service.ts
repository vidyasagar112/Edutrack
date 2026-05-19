import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error' | 'warning' | 'info';
  duration?: number;
}

@Injectable({ providedIn: 'root' })
export class ToastService {

  private toastsSubject =
    new BehaviorSubject<Toast[]>([]);
  toasts$ = this.toastsSubject.asObservable();
  private nextId = 0;

  show(message: string,
       type: Toast['type'] = 'info',
       duration = 3500): void {
    const toast: Toast = {
      id: this.nextId++,
      message,
      type,
      duration
    };
    const current = this.toastsSubject.value;
    this.toastsSubject.next([...current, toast]);

    setTimeout(() => this.remove(toast.id), duration);
  }

  success(message: string): void {
    this.show(message, 'success');
  }

  error(message: string): void {
    this.show(message, 'error', 5000);
  }

  warning(message: string): void {
    this.show(message, 'warning', 4000);
  }

  info(message: string): void {
    this.show(message, 'info');
  }

  remove(id: number): void {
    const current = this.toastsSubject.value;
    this.toastsSubject.next(
      current.filter(t => t.id !== id));
  }
}