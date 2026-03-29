# Email Sending Test Guide

## 🧪 Testing Email Functionality

This guide will help you test if the email sending is working correctly.

---

## Prerequisites

Before testing, you need to set up Gmail credentials:

### Step 1: Set Environment Variables

You need to provide your Gmail credentials. Set these environment variables:

```bash
# For macOS/Linux
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-app-password"
export MAIL_FROM="your-gmail@gmail.com"

# For Windows (Command Prompt)
set MAIL_USERNAME=your-gmail@gmail.com
set MAIL_PASSWORD=your-app-password
set MAIL_FROM=your-gmail@gmail.com
```

### Step 2: Get Gmail App Password

1. Go to https://myaccount.google.com/
2. Click "Security" in the left sidebar
3. Scroll down to "App passwords"
4. Select "Mail" and "Windows Computer"
5. Google will generate a 16-character password
6. Use this as `MAIL_PASSWORD` (without spaces)

---

## Test Method 1: Unit Tests (No Real Email)

The existing unit tests use mocks and don't actually send emails.

```bash
cd /Users/andrewneil/Documents/Dev\ App/Intellij/ecoms
mvn test -Dtest=EmailServiceTest
```

**Result:** Tests pass but no real emails sent

---

## Test Method 2: Integration Test (Real Email)

The integration test will prepare the system and show the email flow.

```bash
cd /Users/andrewneil/Documents/Dev\ App/Intellij/ecoms
mvn test -Dtest=EmailSendingIntegrationTest
```

**Expected Output:**
```
✅ User saved to database
   Email: emailtest@ecoms.com
   Name: Email Test User
   Can now generate and send code to: a.n.royo@outlook.com
```

---

## Test Method 3: Manual API Testing with Curl

### Step 1: Start the Application

```bash
cd /Users/andrewneil/Documents/Dev\ App/Intellij/ecoms
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-app-password"
export MAIL_FROM="your-gmail@gmail.com"
mvn spring-boot:run
```

### Step 2: Create a Test User

First, register a test user (if not exists):

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@ecoms.com",
    "password": "Password123!"
  }'
```

**Expected Response:**
```json
{
  "message": "User created successfully",
  "status": 201,
  "details": {
    "id": 1,
    "name": "Test User",
    "email": "test@ecoms.com",
    "roles": ["ROLE_USER"]
  }
}
```

### Step 3: Generate Code (Send Email)

```bash
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@ecoms.com"
  }'
```

**Expected Response:**
```json
{
  "message": "Code generated and sent to your email successfully",
  "status": 200
}
```

### Step 4: Check Email

**Check your inbox for email from your Gmail address:**

**From:** your-gmail@gmail.com  
**To:** a.n.royo@outlook.com (or whatever email you're testing)  
**Subject:** Your E-Commerce Password Reset Code  

**Email Content:**
```
Hello,

You have requested to reset your password.

Your password reset code is: [6-digit-code]

This code will expire in 10 minutes.

If you did not request a password reset, please ignore this email.

Best regards,
E-Commerce Team
```

---

## Test Method 4: Using Postman

### Step 1: Create Test User

**Method:** POST  
**URL:** `http://localhost:8080/api/users/register`  
**Headers:** `Content-Type: application/json`

**Body:**
```json
{
  "name": "Test User",
  "email": "test@ecoms.com",
  "password": "Password123!"
}
```

### Step 2: Generate Code (Send Email)

**Method:** POST  
**URL:** `http://localhost:8080/api/users/generate-code`  
**Headers:** `Content-Type: application/json`

**Body:**
```json
{
  "email": "test@ecoms.com"
}
```

### Step 3: Check Email

Wait 30 seconds, then check `a.n.royo@outlook.com` (or your test email) for the password reset email.

---

## Troubleshooting

### Problem: Email not sent, getting 500 error

**Possible Causes:**
1. Email credentials not set
2. Gmail app password incorrect
3. SMTP connection timeout

**Solution:**
```bash
# Verify environment variables are set
echo $MAIL_USERNAME
echo $MAIL_PASSWORD
echo $MAIL_FROM

# Check email configuration in application.yaml
cat src/main/resources/application.yaml | grep -A 10 "mail:"
```

### Problem: Email sent but not received

**Possible Causes:**
1. Email went to spam folder
2. Wrong recipient email
3. Gmail's email filter

**Solution:**
1. Check spam/junk folder in Outlook
2. Add sender email to contacts
3. Check email with different recipient

### Problem: "Given that there is no default password encoder" error

This means the application is trying to authenticate before email environment variables are loaded.

**Solution:**
```bash
# Make sure to set environment variables BEFORE running:
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-app-password"
export MAIL_FROM="your-gmail@gmail.com"
mvn spring-boot:run
```

### Problem: 404 User not found

Make sure the user email in generate-code request matches the registered user email.

```bash
# Register user with this email:
{"email": "test@ecoms.com"}

# Then use SAME email in generate-code:
{"email": "test@ecoms.com"}
```

---

## Expected Email Flow

```
┌─────────────┐
│ User Request│
└──────┬──────┘
       ↓
┌──────────────────────────┐
│ POST /api/users/register │  (Create user)
└──────┬───────────────────┘
       ↓
┌──────────────────────────────┐
│ User Saved to Database       │
│ Email: test@ecoms.com        │
└──────┬──────────────────────┘
       ↓
┌──────────────────────────────┐
│POST /api/users/generate-code │  (Request password reset)
│ Email: test@ecoms.com        │
└──────┬──────────────────────┘
       ↓
┌──────────────────────────────┐
│ Generate 6-digit Code        │
│ Code: 523847                 │
└──────┬──────────────────────┘
       ↓
┌──────────────────────────────┐
│ Create Email Message         │
│ To: a.n.royo@outlook.com     │
│ From: your-gmail@gmail.com   │
│ Subject: Password Reset Code │
│ Body: Contains code 523847   │
└──────┬──────────────────────┘
       ↓
┌──────────────────────────────┐
│ Send via SMTP (Gmail)        │
└──────┬──────────────────────┘
       ↓
┌──────────────────────────────┐
│ ✅ Email Delivered           │
│ Check: a.n.royo@outlook.com  │
└──────────────────────────────┘
```

---

## Test Checklist

- [ ] Gmail app password obtained
- [ ] Environment variables set (MAIL_USERNAME, MAIL_PASSWORD, MAIL_FROM)
- [ ] Application started with `mvn spring-boot:run`
- [ ] Test user created via register endpoint
- [ ] Generate code endpoint called
- [ ] Email received in inbox (check spam folder too)
- [ ] Email contains 6-digit code
- [ ] Email from address is correct
- [ ] Email subject is correct

---

## Email Content Example

### Actual Email Received

```
From: noreply@ecoms.com
To: a.n.royo@outlook.com
Date: March 30, 2026, 2:30 PM
Subject: Your E-Commerce Password Reset Code

Hello,

You have requested to reset your password.

Your password reset code is: 834291

This code will expire in 10 minutes.

If you did not request a password reset, please ignore this email.

Best regards,
E-Commerce Team
```

---

## Application.yaml Email Configuration

Current configuration:
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME:your-email@gmail.com}
    password: ${MAIL_PASSWORD:your-app-password}
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
    from: ${MAIL_FROM:your-email@gmail.com}
    subject-code: Your E-Commerce Password Reset Code
```

---

## Test Results

### If Email Sends Successfully ✅

```
✅ Email Configuration: Correct
✅ Gmail Authentication: Working
✅ SMTP Connection: Established
✅ Email Service: Functional
✅ Code Generation: Working
✅ Email Delivery: Success
```

### If Email Fails ❌

Check:
1. Environment variables set correctly
2. Gmail app password (not regular password)
3. Gmail account allows less secure apps
4. Network connection available
5. Gmail app password format (16 chars, no spaces)

---

## Next Steps

After confirming email sending works:

1. **Test Code Validation**
   - Use the 6-digit code to reset password
   - Call `/api/users/change-password` endpoint

2. **Load Testing**
   - Send multiple password reset requests
   - Verify each gets unique code
   - Verify emails arrive for each

3. **Production Deployment**
   - Use production Gmail credentials
   - Set environment variables on server
   - Test with production email addresses

---

## Support

If email is not sending:

1. Check `/tmp/test_output.txt` for error messages
2. Enable DEBUG logging: `logging.level.root=DEBUG`
3. Check Gmail account activity
4. Verify app-specific password (not 2FA)

---

*Test Guide Created: March 30, 2026*
*Updated: For a.n.royo@outlook.com testing*

