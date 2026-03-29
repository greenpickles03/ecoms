# 📧 Email Sending Test - Copy & Paste Commands

## 🚀 One-Command Quick Test

### Terminal 1: Start Application

```bash
export MAIL_USERNAME="your-gmail@gmail.com" && \
export MAIL_PASSWORD="your-app-password" && \
export MAIL_FROM="your-gmail@gmail.com" && \
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms" && \
mvn spring-boot:run
```

### Terminal 2: Run Tests

```bash
# 1. Register a test user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Email Test User",
    "email": "emailtest123@example.com",
    "password": "TestPassword123!"
  }'
```

```bash
# 2. Generate password reset code (sends email)
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "emailtest123@example.com"
  }'
```

### Step 3: Check Email

🔍 **Check a.n.royo@outlook.com inbox for:**

```
From: your-gmail@gmail.com
Subject: Your E-Commerce Password Reset Code
Body: Contains 6-digit code
```

---

## 📝 Detailed Instructions

### Step 1: Open Terminal 1 (App Server)

```bash
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
```

### Step 2: Set Environment Variables in Terminal 1

```bash
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-16-char-app-password"
export MAIL_FROM="your-gmail@gmail.com"
```

**Verify they're set:**
```bash
echo "Username: $MAIL_USERNAME"
echo "Password: $MAIL_PASSWORD"
echo "From: $MAIL_FROM"
```

### Step 3: Start Application in Terminal 1

```bash
mvn clean spring-boot:run
```

**Wait for:**
```
Started EcomsApplication in X.XXX seconds
```

### Step 4: Open Terminal 2 (Test Commands)

In a NEW terminal window/tab:

```bash
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
```

### Step 5: Register Test User in Terminal 2

Copy and paste:

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Email User",
    "email": "testuser@ecoms.com",
    "password": "SecurePassword123!"
  }'
```

**Expected Response:**
```json
{
  "message": "User created successfully",
  "status": 201,
  "details": {
    "id": 1,
    "name": "Test Email User",
    "email": "testuser@ecoms.com",
    "roles": ["ROLE_USER"]
  }
}
```

### Step 6: Generate Code (Send Email) in Terminal 2

Copy and paste:

```bash
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@ecoms.com"
  }'
```

**Expected Response:**
```json
{
  "message": "Code generated and sent to your email successfully",
  "status": 200
}
```

### Step 7: Check Email

**Open your browser:**

1. Go to: https://outlook.live.com
2. Login to: a.n.royo@outlook.com
3. Look in **Inbox** for email from your-gmail@gmail.com
4. Subject should be: "Your E-Commerce Password Reset Code"
5. Body contains: 6-digit code

**If not in Inbox:**
- Check **Junk** folder
- Check **Spam** folder
- Wait 2-3 minutes for delivery

---

## 🔍 Verify Response

### Success Response:
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "message": "Code generated and sent to your email successfully",
  "status": 200
}
```

### Error Response (User not found):
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "message": "User not found",
  "status": 404
}
```

### Error Response (Email failed):
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "message": "Code generated but failed to send email",
  "status": 500
}
```

---

## 🧪 Test Multiple Times

Try sending multiple codes to test repeatability:

```bash
# Send code 1
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email": "testuser@ecoms.com"}'

# Wait 30 seconds

# Send code 2
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email": "testuser@ecoms.com"}'

# Wait 30 seconds

# Send code 3
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email": "testuser@ecoms.com"}'
```

**Expected:** 3 emails arrive, each with different code

---

## 🛠️ If Email Doesn't Arrive

### Check 1: Verify Environment Variables

In Terminal 1 (check before starting app):

```bash
echo $MAIL_USERNAME
echo $MAIL_PASSWORD
echo $MAIL_FROM
```

Should show your Gmail credentials.

### Check 2: Look for Error Logs

In Terminal 1 (where app is running):

Look for messages like:
```
ERROR: Failed to send email
WARNING: Email sending failed
```

### Check 3: Try Different Test Email

```bash
# Register with different email
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "User 2",
    "email": "testuser2@ecoms.com",
    "password": "Pass123!"
  }'

# Generate code
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email": "testuser2@ecoms.com"}'
```

### Check 4: Verify Gmail App Password

Go to: https://myaccount.google.com/apppasswords

1. Select "Mail"
2. Select your device
3. Click "Generate"
4. Copy the 16-character password
5. Update environment variable: `export MAIL_PASSWORD="xxxx xxxx xxxx xxxx"`
6. Restart application (Ctrl+C, then run mvn spring-boot:run again)

---

## 📊 Success Checklist

After running the tests, verify:

- [ ] Terminal 1 shows: "Started EcomsApplication"
- [ ] Terminal 2 register response shows: status 201
- [ ] Terminal 2 generate-code response shows: status 200
- [ ] No error messages in Terminal 1
- [ ] Email received in a.n.royo@outlook.com
- [ ] Email from: your-gmail@gmail.com
- [ ] Email subject: "Your E-Commerce Password Reset Code"
- [ ] Email body contains: 6-digit code

---

## 🎯 Expected Email Example

```
From: noreply@ecoms.com
To: a.n.royo@outlook.com
Subject: Your E-Commerce Password Reset Code
Date: March 30, 2026, 2:40 PM

──────────────────────────────────────────────

Hello,

You have requested to reset your password.

Your password reset code is: 523847

This code will expire in 10 minutes.

If you did not request a password reset, 
please ignore this email.

Best regards,
E-Commerce Team

──────────────────────────────────────────────
```

---

## 🚀 Complete Flow Summary

```
Step 1: Set environment variables
        ↓
Step 2: Start application
        ↓
Step 3: Register test user (POST /api/users/register)
        ↓
Step 4: Generate code (POST /api/users/generate-code)
        ↓
Step 5: Application generates 6-digit code
        ↓
Step 6: Application sends email via Gmail SMTP
        ↓
Step 7: Email delivered to a.n.royo@outlook.com
        ↓
✅ Check inbox for email with code
```

---

## 💡 Pro Tips

1. **Keep Terminal 1 Open** - You need the app running
2. **Use Terminal 2 for Tests** - Keep your test commands separate
3. **Check Spam/Junk First** - New senders often go to spam
4. **Add Sender to Contacts** - Helps with delivery
5. **Use Unique Test Emails** - Like testuser1@ecoms.com, testuser2@ecoms.com
6. **Wait 2 Minutes** - Email delivery takes time

---

## 📞 Quick Reference

| Action | Command |
|--------|---------|
| Set Gmail username | `export MAIL_USERNAME="..."` |
| Set Gmail password | `export MAIL_PASSWORD="..."` |
| Set from address | `export MAIL_FROM="..."` |
| Start app | `mvn spring-boot:run` |
| Register user | `curl -X POST http://localhost:8080/api/users/register` |
| Generate code | `curl -X POST http://localhost:8080/api/users/generate-code` |
| Stop app | `Ctrl+C` in terminal |

---

## ✅ Test Complete!

Once you see the email in a.n.royo@outlook.com:

✅ Email sending is **WORKING**  
✅ SMTP configuration is **CORRECT**  
✅ Gmail authentication is **SUCCESSFUL**  
✅ Feature is **PRODUCTION-READY**  

---

*Last Updated: March 30, 2026*
*Test Email: a.n.royo@outlook.com*

