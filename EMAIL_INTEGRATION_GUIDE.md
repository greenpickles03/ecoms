# Email Integration Guide

## Overview
Email functionality has been successfully integrated into your Spring Boot E-Commerce application. The system can now send password reset codes to users via email.

## Configuration Required

### 1. Gmail Configuration (Using App Password)

If you're using Gmail, follow these steps:

#### Step 1: Enable 2-Factor Authentication
1. Go to https://myaccount.google.com/
2. Navigate to **Security** on the left sidebar
3. Enable **2-Step Verification** if not already enabled

#### Step 2: Generate App Password
1. Go to https://myaccount.google.com/apppasswords
2. Select **Mail** and **Windows Computer** (or your OS)
3. Click **Generate**
4. Copy the 16-character password

#### Step 3: Update application.yaml
Update `/src/main/resources/application.yaml` with your Gmail credentials:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password  # (16-character password from Step 2)
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000

app:
  mail:
    from: your-email@gmail.com
    subject-code: Your E-Commerce Password Reset Code
```

### 2. Using Other Email Providers

#### For Yahoo Mail:
```yaml
spring:
  mail:
    host: smtp.mail.yahoo.com
    port: 587
    username: your-email@yahoo.com
    password: your-app-password
```

#### For Outlook:
```yaml
spring:
  mail:
    host: smtp-mail.outlook.com
    port: 587
    username: your-email@outlook.com
    password: your-password
```

## API Endpoints

### 1. Generate Password Reset Code (Send via Email)

**Endpoint:** `POST /api/users/generate-code`

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Success Response (200):**
```json
{
  "message": "Code generated and sent to your email successfully",
  "status": 200
}
```

**User Not Found (404):**
```json
{
  "message": "User not found",
  "status": 404
}
```

**Email Send Failure (500):**
```json
{
  "message": "Code generated but failed to send email",
  "status": 500
}
```

### 2. Change Password (Use the code received via email)

**Endpoint:** `POST /api/users/change-password`

**Request Body:**
```json
{
  "email": "user@example.com",
  "newPassword": "newPassword123",
  "requestCode": "123456"  // Code received in email
}
```

**Success Response (200):**
```json
{
  "message": "Password changed successfully",
  "details": { /* user object */ },
  "status": 200
}
```

## How It Works

1. **User Requests Password Reset:**
   - User sends their email to `/api/users/generate-code`
   
2. **System Generates Code:**
   - A random 6-digit code is generated
   - Code is sent to user's email
   
3. **User Receives Email:**
   - Email contains the reset code
   - Email body explains the reset process
   
4. **User Changes Password:**
   - User submits email, new password, and code to `/api/users/change-password`
   - System verifies the code and updates the password

## Files Modified/Created

### New Files:
- `src/main/java/com/black/ecoms/service/EmailService.java` - Email service for sending emails
- `src/main/java/com/black/ecoms/dto/GenerateCodeRequest.java` - DTO for generate code request

### Modified Files:
- `pom.xml` - Added spring-boot-starter-mail dependency
- `src/main/resources/application.yaml` - Added email configuration
- `src/main/java/com/black/ecoms/service/UsersService.java` - Integrated email service
- `src/main/java/com/black/ecoms/controller/UsersController.java` - Updated generate-code endpoint

## Testing the Email Feature

### Using Postman or cURL:

```bash
# Generate password reset code
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com"
  }'
```

The user will receive an email with the reset code.

```bash
# Change password with the code
curl -X POST http://localhost:8080/api/users/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "newPassword": "newPassword123",
    "requestCode": "123456"
  }'
```

## Email Template

The current email body is:
```
Hello,

You have requested to reset your password.

Your password reset code is: 123456

This code will expire in 10 minutes.

If you did not request a password reset, please ignore this email.

Best regards,
E-Commerce Team
```

You can customize this in `EmailService.java` by modifying the `buildPasswordResetEmailBody()` method.

## Security Notes

⚠️ **Important:**
- Store email credentials in environment variables, not in application.yaml for production
- Set a shorter expiration time for the reset code (currently stored in memory)
- Consider storing the code in the database with an expiration timestamp
- For production, use app-specific passwords (like Gmail App Passwords)
- Never commit actual credentials to version control

## Troubleshooting

### "Failed to send email" Error
- Check your email credentials in application.yaml
- Ensure 2FA is enabled if using Gmail
- Verify the app password is correct (16 characters)
- Check network connectivity

### Gmail Authentication Fails
- Verify you've generated an App Password
- Ensure 2-Step Verification is enabled
- Check that you're using the correct 16-character app password

### Port Issues
- Port 587 is the standard for STARTTLS
- Port 465 is for SSL/TLS connections (requires different configuration)
- Ensure firewall allows outgoing connections on these ports

