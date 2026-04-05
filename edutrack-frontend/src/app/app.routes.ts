import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  // public routes
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  {
    path: 'home',
    loadComponent: () =>
      import('./modules/auth/landing/landing.component')
        .then(m => m.LandingComponent)
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./modules/auth/login/login.component')
        .then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./modules/auth/register/register.component')
        .then(m => m.RegisterComponent)
  },

  // protected routes
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./modules/dashboard/dashboard.component')
        .then(m => m.DashboardComponent)
  },
  {
    path: 'courses',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./modules/courses/course-list/course-list.component')
        .then(m => m.CourseListComponent)
  },
  {
  path: 'courses/create',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./modules/courses/create-course/create-course.component')
      .then(m => m.CreateCourseComponent)
},
{
  path: 'quiz/create/:courseId',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./modules/quiz/create-quiz/create-quiz.component')
      .then(m => m.CreateQuizComponent)
},
{
  path: 'quiz/:quizId/add-question',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./modules/quiz/add-question/add-question.component')
      .then(m => m.AddQuestionComponent)
},
  {
    path: 'courses/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./modules/courses/course-detail/course-detail.component')
        .then(m => m.CourseDetailComponent)
  },
  {
    path: 'quiz/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./modules/quiz/take-quiz/take-quiz.component')
        .then(m => m.TakeQuizComponent)
  },
  {
    path: 'quiz/result/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./modules/quiz/quiz-result/quiz-result.component')
        .then(m => m.QuizResultComponent)
  },
  {
    path: 'analytics',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./modules/analytics/analytics.component')
        .then(m => m.AnalyticsComponent)
  },
  {
    path: 'progress',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./modules/progress/progress.component')
        .then(m => m.ProgressComponent)
  },

  // fallback
  { path: '**', redirectTo: '/home' }
];