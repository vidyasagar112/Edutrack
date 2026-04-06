import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AdminService } from '../../../core/services/admin.service';
import { CourseService } from '../../../core/services/course.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {

  // data
  users: any[] = [];
  courses: any[] = [];
  filteredUsers: any[] = [];

  // add these properties
activeUserTab = 'students';
students: any[] = [];
instructors: any[] = [];
isDeletingId: number | null = null;
deleteSuccess = '';
deleteError = '';

  // stats
  totalUsers = 0;
  totalCourses = 0;
  totalStudents = 0;
  totalInstructors = 0;

  // email
  selectedUserId: number | null = null;
  emailSubject = '';
  emailBody = '';
  emailSuccess = '';
  emailError = '';
  isSendingEmail = false;

  // search
  searchUser = '';

  isLoading = true;
  activeTab = 'overview';

  constructor(
    private adminService: AdminService,
    private courseService: CourseService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    // load users
    this.adminService.getAllUsers().subscribe({
  next: (res) => {
    this.users = res.data || [];
    this.filteredUsers = this.users;
    this.totalUsers = this.users.length;

    // separate students and instructors
    this.students = this.users.filter((u: any) =>
      u.roles?.some((r: any) =>
        r.name === 'ROLE_STUDENT'));
    this.instructors = this.users.filter((u: any) =>
      u.roles?.some((r: any) =>
        r.name === 'ROLE_INSTRUCTOR'));

    this.totalStudents = this.students.length;
    this.totalInstructors = this.instructors.length;
  }
});

    // load courses
    this.courseService.getAllPublished().subscribe({
      next: (res) => {
        this.courses = res.data || [];
        this.totalCourses = this.courses.length;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  searchUsers(): void {
    if (!this.searchUser.trim()) {
      this.filteredUsers = this.users;
      return;
    }
    this.filteredUsers = this.users.filter(u =>
      u.fullName?.toLowerCase().includes(
        this.searchUser.toLowerCase()) ||
      u.email?.toLowerCase().includes(
        this.searchUser.toLowerCase())
    );
  }

  getUserRole(user: any): string {
    if (!user.roles?.length) return 'No Role';
    return user.roles[0].name
      .replace('ROLE_', '');
  }

  getRoleBadgeColor(user: any): string {
    const role = this.getUserRole(user);
    const colors: any = {
      'ADMIN'      : 'bg-danger',
      'INSTRUCTOR' : 'bg-success',
      'STUDENT'    : 'bg-primary'
    };
    return colors[role] || 'bg-secondary';
  }

  selectUserForEmail(userId: number): void {
    this.selectedUserId = userId;
    this.activeTab = 'email';
    this.emailSubject = '';
    this.emailBody = '';
    this.emailSuccess = '';
    this.emailError = '';
  }

  sendEmail(): void {
    if (!this.selectedUserId ||
        !this.emailSubject ||
        !this.emailBody) {
      this.emailError = 'All fields are required!';
      return;
    }

    this.isSendingEmail = true;
    this.emailError = '';

    this.adminService.sendEmail(
      this.selectedUserId,
      this.emailSubject,
      this.emailBody
    ).subscribe({
      next: () => {
        this.isSendingEmail = false;
        this.emailSuccess = 'Email sent successfully!';
        this.emailSubject = '';
        this.emailBody = '';
        this.selectedUserId = null;
      },
      error: (err) => {
        this.isSendingEmail = false;
        this.emailError =
          err.error?.message || 'Failed to send email!';
      }
    });
  }
  deleteUser(userId: number, userName: string): void {
  if (!confirm(
    `Are you sure you want to delete ${userName}?`)) {
    return;
  }

  this.isDeletingId = userId;
  this.deleteSuccess = '';
  this.deleteError = '';

  this.adminService.deleteUser(userId).subscribe({
    next: () => {
      this.isDeletingId = null;
      this.deleteSuccess = `${userName} deleted successfully!`;
      // remove from lists
      this.users = this.users.filter(
        u => u.id !== userId);
      this.students = this.students.filter(
        u => u.id !== userId);
      this.instructors = this.instructors.filter(
        u => u.id !== userId);
      this.filteredUsers = this.filteredUsers.filter(
        u => u.id !== userId);
      this.totalUsers = this.users.length;
      this.totalStudents = this.students.length;
      this.totalInstructors = this.instructors.length;
      setTimeout(() => this.deleteSuccess = '', 3000);
    },
    error: () => {
      this.isDeletingId = null;
      this.deleteError = 'Failed to delete user!';
      setTimeout(() => this.deleteError = '', 3000);
    }
  });
}

setUserTab(tab: string): void {
  this.activeUserTab = tab;
}

  setTab(tab: string): void {
    this.activeTab = tab;
  }

  logout(): void {
    this.authService.logout();
  }
}