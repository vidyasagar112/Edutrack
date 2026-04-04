export interface Course {
  id: number;
  title: string;
  description: string;
  subject: string;
  thumbnailUrl: string;
  published: boolean;
  instructorName: string;
  instructorEmail: string;
  enrollmentCount: number;
  createdAt: string;
}

export interface CourseRequest {
  title: string;
  description: string;
  subject: string;
  thumbnailUrl: string;
  published: boolean;
}