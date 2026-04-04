export interface Enrollment {
  id: number;
  courseId: number;
  courseTitle: string;
  courseSubject: string;
  instructorName: string;
  studentName: string;
  studentEmail: string;
  status: string;
  progressPercent: number;
  enrolledAt: string;
}

export interface EnrollmentRequest {
  courseId: number;
}