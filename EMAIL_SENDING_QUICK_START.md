# 📧 Email Sending Test - Complete Instructions

## Overview

You want to test if emails are actually being sent to `a.n.royo@outlook.com`. This document provides step-by-step instructions.

---

## ⚠️ Important: Set Gmail Credentials First

Before testing, you MUST set environment variables with your Gmail credentials:

### macOS/Linux Terminal:

```bash
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-16-char-app-password"
export MAIL_FROM="your-gmail@gmail.com"
```

### Get Gmail App Password:

1. Go to: https://myaccount.google.com/
2. Click **Security** (left sidebar)
3. Scroll to **App passwords**
4. Select: Mail → Windows/Mac/Linux
5. Copy the 16-character password
6. Use it as `MAIL_PASSWORD` (without spaces)

---

## 🚀 Quick Test (5 minutes)

### Step 1: Set Environment Variables

```bash
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="xxxx xxxx xxxx xxxx"
export MAIL_FROM="your-email@gmail.com"
```

### Step 2: Start the Application

```bash
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
mvn spring-boot:run
```

**Wait for:** `Started EcomsApplication in X seconds`

### Step 3: Create a Test User

Open new terminal, run:

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test.user@ecoms.com",
    "password": "TestPass123!"
  }'
```

**Expected Response:**
```json
{
  "message": "User created successfully",
  "status": 201
}
```

### Step 4: Generate Code (Send Email)

```bash
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.user@ecoms.com"
  }'
```

**Expected Response:**
```json
{
  "message": "Code generated and sent to your email successfully",
  "status": 200
}
```

### Step 5: Check Email

✅ **Open `a.n.royo@outlook.com` inbox**

Look for:
- **From:** your-email@gmail.com
- **Subject:** Your E-Commerce Password Reset Code
- **Body:** Contains 6-digit code (e.g., 523847)

---

## 🔧 Detailed Setup Instructions

### Full Step-by-Step

#### Step 1: Stop Any Running Application

```bash
# Press Ctrl+C in the terminal running the app
# Or find and kill the process:
pkill -f "mvn spring-boot:run"
```

#### Step 2: Navigate to Project

```bash
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
```

#### Step 3: Verify MySQL is Running

```bash
# Check if MySQL is running
mysql -u root -p

# If it asks for password, enter: P@$$w0rdv1
# If connected, type: quit
```

#### Step 4: Set Environment Variables (IMPORTANT!)

```bash
# In the SAME terminal window where you'll run mvn:
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-app-password"
export MAIL_FROM="your-gmail@gmail.com"

# Verify they're set:
echo $MAIL_USERNAME
echo $MAIL_PASSWORD
echo $MAIL_FROM
```

#### Step 5: Build and Start Application

```bash
mvn clean package
java -jar target/ecoms-0.0.1-SNAPSHOT.jar
```

**Or directly:**

```bash
mvn spring-boot:run
```

**Wait for this message:**
```
2026-03-30T02:30:00.000+08:00 INFO 2113 --- [main] com.black.ecoms.EcomsApplication : Started EcomsApplication in 5.123 seconds
```

#### Step 6: Open Another Terminal for Tests

```bash
# New terminal window/tab
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
```

#### Step 7: Register Test User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Email Test User",
    "email": "testuser@example.com",
    "password": "Password123!"
  }'
```

#### Step 8: Send Password Reset Code

```bash
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com"
  }'
```

#### Step 9: Check Email

1. Open browser: https://outlook.live.com
2. Login to: a.n.royo@outlook.com
3. Check **Inbox** and **Junk** folders
4. Look for email with subject: "Your E-Commerce Password Reset Code"

---

## 📊 What Happens When Email Sends

### Success Flow

```
1. User registers with email: testuser@example.com
   ↓
2. User requests password reset: /api/users/generate-code
   ↓
3. System generates 6-digit code: 892347
   ↓
4. System sends email via Gmail SMTP
   ↓
5. Email arrives at: a.n.royo@outlook.com
   ↓
✅ Check inbox for the email
```

### Expected Email Content

```
From: your-gmail@gmail.com
To: a.n.royo@outlook.com
Subject: Your E-Commerce Password Reset Code
Date: March 30, 2026, 2:35 PM

─────────────────────────────────────────
Hello,

You have requested to reset your password.

Your password reset code is: 892347

This code will expire in 10 minutes.

If you did not request a password reset, 
please ignore this email.

Best regards,
E-Commerce Team
─────────────────────────────────────────
```

---

## ✅ Success Indicators

### If Email Sends Successfully:

✅ No errors in application logs  
✅ generate-code returns status 200  
✅ Email appears in a.n.royo@outlook.com within 1 minute  
✅ Email contains 6-digit code  
✅ Email subject matches configuration  

### Example Success Log:

```
[INFO] 2026-03-30T02:30:45.123+08:00 INFO 2113 --- [main] com.black.ecoms.EcomsApplication : Started EcomsApplication
[DEBUG] 2026-03-30T02:31:00.456+08:00 DEBUG com.black.ecoms.service.EmailService : Sending email to: testuser@example.com
[DEBUG] 2026-03-30T02:31:01.789+08:00 DEBUG com.black.ecoms.service.EmailService : Email sent successfully
```

---

## ❌ Troubleshooting

### Issue 1: "Error sending email" in logs

**Cause:** Gmail credentials not set

**Solution:**
```bash
# Verify environment variables
echo $MAIL_USERNAME
echo $MAIL_PASSWORD
echo $MAIL_FROM

# If empty, set them:
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-app-password"
export MAIL_FROM="your-gmail@gmail.com"

# Restart application
```

### Issue 2: Email not received

**Check:**
1. Junk/Spam folder in Outlook
2. MAIL_FROM address matches registered address
3. Email was sent (check app logs)
4. 5-minute wait for delivery

**Solution:**
```bash
# Try with different Gmail account
export MAIL_USERNAME="alternative-gmail@gmail.com"
export MAIL_PASSWORD="alternative-app-password"
export MAIL_FROM="alternative-gmail@gmail.com"

# Restart and test again
mvn spring-boot:run
```

### Issue 3: "User not found" error

**Cause:** Email in generate-code doesn't match registered user

**Solution:**
```bash
# Register user with EXACT email
curl -X POST http://localhost:8080/api/users/register \
  -d '{"email":"john@example.com","password":"pass","name":"John"}'

# Then use SAME email
curl -X POST http://localhost:8080/api/users/generate-code \
  -d '{"email":"john@example.com"}'
```

### Issue 4: "Authentication failed" error

**Cause:** Gmail app password incorrect

**Solution:**
1. Go to: https://myaccount.google.com/apppasswords
2. Generate NEW app password
3. Copy ALL 16 characters (including spaces or without)
4. Set: `export MAIL_PASSWORD="xxxx xxxx xxxx xxxx"`
5. Restart application

---

## 🧪 Using Postman (Alternative)

If curl is unfamiliar, use Postman:

### Register User in Postman

```
Method: POST
URL: http://localhost:8080/api/users/register
Headers: Content-Type: application/json

Body:
{
  "name": "Test User",
  "email": "testuser@example.com",
  "password": "Password123!"
}
```

### Generate Code in Postman

```
Method: POST
URL: http://localhost:8080/api/users/generate-code
Headers: Content-Type: application/json

Body:
{
  "email": "testuser@example.com"
}
```

---

## 📝 Test Checklist

Complete this checklist before considering the test done:

- [ ] Maven project compiles without errors
- [ ] MySQL database is running
- [ ] Environment variables (MAIL_USERNAME, MAIL_PASSWORD, MAIL_FROM) are set
- [ ] Application started successfully (mvn spring-boot:run)
- [ ] Test user registered successfully (register endpoint returns 201)
- [ ] Generate code endpoint returns status 200
- [ ] No error messages in application logs
- [ ] Email received in a.n.royo@outlook.com inbox
- [ ] Email contains correct 6-digit code
- [ ] Email from address is correct
- [ ] Email subject is correct

---

## 🎯 Expected Result

**✅ Email should arrive in a.n.royo@outlook.com within 1-2 minutes**

If it doesn't:
1. Check Spam/Junk folder
2. Check Gmail credentials
3. Check application logs
4. Try with different test email

---

## 📞 Quick Troubleshooting Commands

```bash
# Check if app is running
lsof -i :8080

# Kill running app
kill -9 <PID>

# Check MySQL
mysql -u root -p

# Test email config
mvn -Dtest=EmailServiceTest test

# View logs
tail -100 /tmp/application.log

# Check environment vars
env | grep MAIL
```

---

## 📚 Additional Resources

- **Main Test Report:** TEST_REPORT_GENERATE_CODE.md
- **Testing Guide:** TESTING_GUIDE_GENERATE_CODE.md
- **Email Integration Test:** src/test/java/.../EmailSendingIntegrationTest.java
- **Application Config:** src/main/resources/application.yaml

---

## 🎉 Summary

To test email sending:

1. **Set Gmail credentials** (environment variables)
2. **Start application** (mvn spring-boot:run)
3. **Register user** (POST /api/users/register)
4. **Generate code** (POST /api/users/generate-code)
5. **Check email** (a.n.royo@outlook.com)

**Expected:** Email arrives within 2 minutes with 6-digit code

---

*Created: March 30, 2026*
*Test Email: a.n.royo@outlook.com*

