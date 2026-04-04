export interface Analytics {
  studentId: number;
  studentName: string;
  studentEmail: string;
  totalQuizzesAttempted: number;
  averageScore: number;
  averagePercentage: number;
  highestScore: number;
  lowestScore: number;
  totalPassed: number;
  totalFailed: number;
  subjectPerformances: SubjectPerformance[];
  quizDetails: QuizDetail[];
  suggestions: string[];
}

export interface SubjectPerformance {
  subject: string;
  totalAttempts: number;
  averagePercentage: number;
  level: string;
}

export interface QuizDetail {
  quizId: number;
  quizTitle: string;
  subject: string;
  score: number;
  totalQuestions: number;
  percentage: number;
  result: string;
}