# 📧 Authentication Failed - Complete Fix Guide

## The Problem

```
Failed to send email: Authentication failed
```

This means Gmail rejected your SMTP login credentials.

---

## Why This Happens

Gmail has **two types of passwords**:

```
Your Gmail Login Password:     P@ssw0rd123!
├─ Works for: Logging into Gmail
├─ Works for: Gmail mobile app
└─ Works for: SMTP? ❌ NO

Gmail App Password:            abcd efgh ijkl mnop
├─ Works for: Third-party apps
├─ Works for: Email clients
└─ Works for: SMTP? ✅ YES
```

**You need the APP PASSWORD for email sending!**

---

## Solution: Get & Use App Password

### Step 1: Verify 2-Factor Authentication is ON

Go to: https://myaccount.google.com/security

Look for: "2-Step Verification"

**If it says "OFF":**
1. Click "Enable 2-Step Verification"
2. Follow the steps
3. Come back when done

**If it says "ON":**
1. Great! Continue to Step 2

---

### Step 2: Generate App Password

Go to: https://myaccount.google.com/apppasswords

**You should see a form with two dropdowns.**

**If you don't see this page:**
- 2-Factor Authentication is NOT enabled
- Go back to Step 1 and enable it first

**To generate password:**

1. Click first dropdown → Select **"Mail"**
2. Click second dropdown → Select **"Windows Computer"** (or your device)
3. Click **"GENERATE"** button
4. Google will show you a password like: `abcd efgh ijkl mnop`
5. **COPY THIS PASSWORD COMPLETELY** (all 16 characters with spaces)

---

### Step 3: Stop Running Application

In the terminal where your app is running:
```bash
# Press Ctrl+C to stop
```

Wait for it to stop completely.

---

### Step 4: Clear Old Variables & Set New Ones

Open a **NEW terminal** and run:

```bash
# IMPORTANT: Use the 16-char app password you just copied!
# Replace "your-gmail@gmail.com" with your actual Gmail
# Replace "xxxx xxxx xxxx xxxx" with the app password

export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="xxxx xxxx xxxx xxxx"
export MAIL_FROM="your-gmail@gmail.com"
```

**Example:**
```bash
export MAIL_USERNAME="andrew.neil@gmail.com"
export MAIL_PASSWORD="abcd efgh ijkl mnop"
export MAIL_FROM="andrew.neil@gmail.com"
```

---

### Step 5: Verify Variables are Set

In the SAME terminal where you set them, type:

```bash
echo "Username: $MAIL_USERNAME"
echo "Password: $MAIL_PASSWORD"
echo "From: $MAIL_FROM"
```

**Should show:**
```
Username: your-gmail@gmail.com
Password: xxxx xxxx xxxx xxxx
From: your-gmail@gmail.com
```

**If any are empty:** They weren't set correctly. Repeat Step 4.

---

### Step 6: Start Application Again

**In the SAME terminal where you set the variables:**

```bash
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
mvn spring-boot:run
```

**Wait for message:**
```
Started EcomsApplication in X.XXX seconds
```

This means app is running and ready.

---

### Step 7: Test Email Sending

**Open ANOTHER terminal** and run:

```bash
# Register a test user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@ecoms.com","password":"Password123!"}'
```

**Expected response:**
```json
{
  "message": "User created successfully",
  "status": 201
}
```

---

### Step 8: Send Password Reset Code

**In the SAME terminal:**

```bash
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email":"test@ecoms.com"}'
```

**Expected response:**
```json
{
  "message": "Code generated and sent to your email successfully",
  "status": 200
}
```

**Expected in Terminal 1 (app logs):**
```
No error about authentication!
Email sent successfully
```

---

### Step 9: Verify Email Received

1. Open browser and go to: https://outlook.live.com
2. Login to: **a.n.royo@outlook.com**
3. Check **Inbox** (and Spam/Junk folder if not there)
4. Look for email with subject: **"Your E-Commerce Password Reset Code"**
5. Email should contain a **6-digit code**

---

## ✅ Success Signs

**In Terminal 1 (App Logs):**
```
✅ No "Authentication failed" error
✅ Shows "Email sent successfully"
```

**In Response:**
```
✅ Status: 200
✅ Message: "Code generated and sent to your email successfully"
```

**In Email (a.n.royo@outlook.com):**
```
✅ Email received
✅ From: your-gmail@gmail.com
✅ Subject: "Your E-Commerce Password Reset Code"
✅ Body contains: 6-digit code
```

---

## ❌ Still Getting Error?

### Check 1: Wrong Password Type

```bash
# Check what you're using
echo $MAIL_PASSWORD
```

**If it's your Gmail login password:**
- ❌ WRONG! That won't work with SMTP
- Get app password from: https://myaccount.google.com/apppasswords
- It should be 16 characters like: `abcd efgh ijkl mnop`

### Check 2: 2FA Not Enabled

```
Go to: https://myaccount.google.com/security
Look for: "2-Step Verification"
```

**If it says "OFF":**
- Enable it first
- Then get app password
- Then restart app

### Check 3: Spaces in Password

App password might have spaces:
```
abcd efgh ijkl mnop   ← With spaces is correct!
xxxxxxxxxxxxxxxx      ← Without spaces also works
```

Both formats work. Use whichever you copied.

### Check 4: Variables Not Persisting

```bash
# Variables are lost if you close terminal
# If you closed the terminal and reopened it, set them AGAIN

export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-16-char-app-password"
export MAIL_FROM="your-gmail@gmail.com"

# Then restart app
mvn spring-boot:run
```

---

## 📋 Complete Checklist

Before you test:

- [ ] 2-Factor Authentication is ON (checked at myaccount.google.com/security)
- [ ] Got 16-character app password (from myaccount.google.com/apppasswords)
- [ ] Copied ALL 16 characters (including spaces if present)
- [ ] Stopped previous app (Ctrl+C)
- [ ] Set MAIL_USERNAME environment variable
- [ ] Set MAIL_PASSWORD to app password (NOT Gmail password)
- [ ] Set MAIL_FROM environment variable
- [ ] Verified variables with echo command
- [ ] Started new app in SAME terminal (mvn spring-boot:run)
- [ ] Waited for "Started EcomsApplication" message
- [ ] Tested in different terminal
- [ ] Got status 200 response
- [ ] Email received in a.n.royo@outlook.com

---

## 🎯 The Main Difference (Critical!)

```
❌ WRONG - Using Gmail Password:
export MAIL_PASSWORD="MyGmailPassword123"
Result: Authentication failed ❌

✅ CORRECT - Using App Password:
export MAIL_PASSWORD="abcd efgh ijkl mnop"
Result: Email sent successfully ✅
```

---

## 🚀 Try Now

1. Get app password: https://myaccount.google.com/apppasswords
2. Stop app: Ctrl+C
3. Set variables with app password
4. Start app: mvn spring-boot:run
5. Test sending code
6. Check a.n.royo@outlook.com

**Should work now! ✅**

---

*Authentication Failed Fix*  
*Root Cause: Regular password instead of app password*  
*Solution: Get 16-char app password from Gmail*  
*Time: 10 minutes*  

