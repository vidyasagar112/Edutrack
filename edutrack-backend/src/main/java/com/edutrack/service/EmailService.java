package com.edutrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired private JavaMailSender javaMailSender;
    @Autowired private UserRepository userRepository;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    // ── core email sender ───────────────────────────────────
    public void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            javaMailSender.send(message);
            System.out.println("Email sent to: " + to);

        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Email error: " + e.getMessage());
        }
    }

    // ── welcome email after registration ────────────────────
    public void sendWelcomeEmail(String to, String fullName) {
        String subject = "Welcome to EduTrack! 🎓";
        String body = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #2563eb;">Welcome to EduTrack, %s! 🎓</h2>
                    <p>We are excited to have you on board.</p>
                    <p>With EduTrack you can:</p>
                    <ul>
                        <li>Enroll in courses</li>
                        <li>Attempt quizzes</li>
                        <li>Track your progress</li>
                        <li>View performance analytics</li>
                    </ul>
                    <p>Start learning today!</p>
                    <br>
                    <p>Best regards,<br><b>EduTrack Team</b></p>
                </div>
                """.formatted(fullName);

        sendEmail(to, subject, body);
    }

    // ── enrollment confirmation email ───────────────────────
    public void sendEnrollmentEmail(String to, String fullName,
                                     String courseTitle, String instructorName) {
        String subject = "Enrollment Confirmed — " + courseTitle;
        String body = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #2563eb;">Enrollment Confirmed! 📚</h2>
                    <p>Hi <b>%s</b>,</p>
                    <p>You have successfully enrolled in:</p>
                    <div style="background:#f0f4ff; padding:16px; border-radius:8px;">
                        <h3 style="color:#1d4ed8;">%s</h3>
                        <p>Instructor: <b>%s</b></p>
                    </div>
                    <p>Start learning and track your progress on EduTrack!</p>
                    <br>
                    <p>Best regards,<br><b>EduTrack Team</b></p>
                </div>
                """.formatted(fullName, courseTitle, instructorName);

        sendEmail(to, subject, body);
    }

    // ── quiz result email ───────────────────────────────────
    public void sendQuizResultEmail(String to, String fullName,
                                     String quizTitle, int score,
                                     int totalQuestions, double percentage) {
        String result = percentage >= 50 ? "PASSED ✅" : "FAILED ❌";
        String color  = percentage >= 50 ? "#16a34a" : "#dc2626";

        String subject = "Quiz Result — " + quizTitle;
        String body = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #2563eb;">Quiz Result 🎯</h2>
                    <p>Hi <b>%s</b>,</p>
                    <p>Here are your results for:</p>
                    <div style="background:#f0f4ff; padding:16px; border-radius:8px;">
                        <h3 style="color:#1d4ed8;">%s</h3>
                        <p>Score: <b>%d / %d</b></p>
                        <p>Percentage: <b>%.1f%%</b></p>
                        <p>Result: <b style="color:%s;">%s</b></p>
                    </div>
                    <p>Keep practicing to improve your scores!</p>
                    <br>
                    <p>Best regards,<br><b>EduTrack Team</b></p>
                </div>
                """.formatted(fullName, quizTitle, score,
                              totalQuestions, percentage, color, result);

        sendEmail(to, subject, body);
    }

    // ── course completion email ─────────────────────────────
    public void sendCourseCompletionEmail(String to, String fullName,
                                           String courseTitle) {
        String subject = "Course Completed — " + courseTitle + " 🎉";
        String body = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #16a34a;">Congratulations! 🎉</h2>
                    <p>Hi <b>%s</b>,</p>
                    <p>You have successfully completed:</p>
                    <div style="background:#f0fdf4; padding:16px; border-radius:8px;">
                        <h3 style="color:#15803d;">%s</h3>
                    </div>
                    <p>Amazing achievement! Keep learning and growing.</p>
                    <br>
                    <p>Best regards,<br><b>EduTrack Team</b></p>
                </div>
                """.formatted(fullName, courseTitle);

        sendEmail(to, subject, body);
    }
    
 // ── Forgot password email ───────────────────────────
    public void sendForgotPasswordEmail(String to,
                                         String fullName,
                                         String resetToken) {
        String resetLink = "http://localhost:4200/reset-password?token="
                + resetToken;
        String subject = "Reset Your EduTrack Password 🔐";
        String body = """
                <div style="font-family:Arial,sans-serif;
                            max-width:600px;margin:auto;">
                  <h2 style="color:#1F4E79;">
                    Reset Your Password 🔐
                  </h2>
                  <p>Hi <b>%s</b>,</p>
                  <p>We received a request to reset your password.</p>
                  <p>Click the button below to reset it:</p>
                  <div style="text-align:center;margin:30px 0;">
                    <a href="%s"
                       style="background:#1F4E79;color:white;
                              padding:14px 28px;border-radius:8px;
                              text-decoration:none;font-weight:bold;
                              font-size:16px;">
                      Reset Password
                    </a>
                  </div>
                  <p style="color:#666;">
                    This link expires in <b>30 minutes</b>.
                  </p>
                  <p style="color:#666;">
                    If you didn't request this, ignore this email.
                  </p>
                  <br>
                  <p>Best regards,<br><b>EduTrack Team</b></p>
                </div>
                """.formatted(fullName, resetLink);

        sendEmail(to, subject, body);
    }

    // ── Email verification email ────────────────────────
    public void sendEmailVerificationEmail(String to,
                                            String fullName,
                                            String verifyToken) {
        String verifyLink =
                "http://localhost:4200/verify-email?token="
                + verifyToken;
        String subject = "Verify Your EduTrack Email ✅";
        String body = """
                <div style="font-family:Arial,sans-serif;
                            max-width:600px;margin:auto;">
                  <h2 style="color:#1F4E79;">
                    Verify Your Email ✅
                  </h2>
                  <p>Hi <b>%s</b>,</p>
                  <p>Welcome to EduTrack! Please verify your
                     email to activate your account.</p>
                  <div style="text-align:center;margin:30px 0;">
                    <a href="%s"
                       style="background:#059669;color:white;
                              padding:14px 28px;border-radius:8px;
                              text-decoration:none;font-weight:bold;
                              font-size:16px;">
                      Verify Email
                    </a>
                  </div>
                  <p style="color:#666;">
                    This link expires in <b>24 hours</b>.
                  </p>
                  <br>
                  <p>Best regards,<br><b>EduTrack Team</b></p>
                </div>
                """.formatted(fullName, verifyLink);

        sendEmail(to, subject, body);
    }

    // ── send email to any user by id — admin only ───────────
    public void sendCustomEmail(Long userId, String subject, String body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "id", userId));
        sendEmail(user.getEmail(), subject, body);
    }
}