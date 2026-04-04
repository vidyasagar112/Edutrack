export interface Quiz {
  id: number;
  title: string;
  description: string;
  difficulty: string;
  timeLimitMinutes: number;
  courseId: number;
  courseTitle: string;
  totalQuestions: number;
  createdAt: string;
}

export interface Question {
  id: number;
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
  correctOption?: string;
  explanation?: string;
  quizId: number;
}

export interface QuizAttemptRequest {
  quizId: number;
  answers: { [questionId: number]: string };
}

export interface QuizAttemptResponse {
  id: number;
  quizId: number;
  quizTitle: string;
  studentName: string;
  score: number;
  totalQuestions: number;
  percentage: number;
  result: string;
  attemptedAt: string;
}