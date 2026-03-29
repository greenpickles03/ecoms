# 🔧 Authentication Failed - SOLUTION

## ❌ Error Message
```
Failed to send email: Authentication failed
```

## 🎯 Why This Happens

Gmail has **two types of passwords:**

1. **Regular Password** ❌ (DOESN'T WORK for SMTP)
   - Your account password
   - For login only
   - Won't work with applications

2. **App Password** ✅ (THIS IS WHAT YOU NEED)
   - Special 16-character password
   - For applications only
   - Only works if 2FA is enabled

---

## ✅ SOLUTION: Get Gmail App Password

### Step 1: Enable 2-Factor Authentication (if not already enabled)

1. Go to: https://myaccount.google.com/
2. Click **Security** (left sidebar)
3. Look for "2-Step Verification"
4. If NOT enabled:
   - Click "Enable 2-Step Verification"
   - Follow the steps
   - Use your phone number

### Step 2: Get App Password

1. Go to: https://myaccount.google.com/apppasswords
   - (You must have 2FA enabled for this to appear)

2. Select **Mail** from dropdown

3. Select your device (Windows, Mac, etc.)

4. Click **Generate**
   - Google will show a 16-character password
   - Example: `abcd efgh ijkl mnop`

5. **Copy this password** (all 16 characters, with or without spaces)

### Step 3: Set Environment Variables

```bash
# IMPORTANT: Use the APP PASSWORD you just copied, NOT your regular password

export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="abcd efgh ijkl mnop"    # ← Use the 16-char app password!
export MAIL_FROM="your-gmail@gmail.com"
```

### Step 4: Start Application

```bash
# Make sure to set env vars BEFORE running the app
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-16-char-app-password"
export MAIL_FROM="your-gmail@gmail.com"

cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
mvn spring-boot:run
```

---

## 🔍 Verify Setup is Correct

### Check 1: Environment Variables Are Set

```bash
# In the SAME terminal where you set exports, verify:
echo "Username: $MAIL_USERNAME"
echo "Password: $MAIL_PASSWORD"
echo "From: $MAIL_FROM"
```

**Should show:**
```
Username: your-gmail@gmail.com
Password: abcd efgh ijkl mnop
From: your-gmail@gmail.com
```

### Check 2: Password Format

The app password should be:
- ✅ 16 characters
- ✅ Contains letters and numbers
- ✅ Format: `xxxx xxxx xxxx xxxx` (with spaces) OR `xxxxxxxxxxxxxxxx` (without spaces)
- ✅ All lowercase when copied

### Check 3: 2FA is Enabled

1. Go to: https://myaccount.google.com/
2. Click **Security**
3. Look for "2-Step Verification" - should say "On"

If it says "Off", you MUST enable it first before app passwords work.

---

## ❌ Common Mistakes

### Mistake 1: Using Regular Password Instead of App Password
```bash
❌ WRONG:
export MAIL_PASSWORD="MyGmailPassword123"

✅ CORRECT:
export MAIL_PASSWORD="abcd efgh ijkl mnop"
```

### Mistake 2: Not Enabling 2FA First
App passwords only work if 2-Factor Authentication is enabled.
- Check: https://myaccount.google.com/security
- Enable 2FA if it says "Off"

### Mistake 3: Copying Password Incorrectly
- Copy ALL 16 characters including spaces
- Don't skip any characters
- Paste it exactly as shown

### Mistake 4: Setting Env Vars in Wrong Terminal
```bash
❌ WRONG: Set variables in Terminal 1, but run app in Terminal 2

✅ CORRECT: Set variables and run app in the SAME terminal
export MAIL_USERNAME="..."
export MAIL_PASSWORD="..."
export MAIL_FROM="..."
mvn spring-boot:run  # Run in same terminal!
```

### Mistake 5: Not Restarting App After Changing Password
```bash
❌ WRONG: Set new password but don't restart app

✅ CORRECT:
1. Stop app (Ctrl+C)
2. Set new password (export ...)
3. Start app again (mvn spring-boot:run)
```

---

## 🔄 Step-By-Step Fix

### 1. Stop Application
```bash
# If running, press Ctrl+C in the terminal
```

### 2. Get Gmail App Password

Go to: https://myaccount.google.com/apppasswords

**If this page doesn't show:**
1. Go to: https://myaccount.google.com/security
2. Enable "2-Step Verification" first
3. Then try apppasswords again

**If it shows:**
1. Select "Mail" from dropdown
2. Select your device (e.g., "Windows Computer")
3. Click "Generate"
4. **Copy the 16-character password**

### 3. Set Environment Variables (NEW TERMINAL)

```bash
# Open a NEW terminal window

# Copy your Gmail email
export MAIL_USERNAME="your-gmail@gmail.com"

# Paste the 16-character app password you just copied
export MAIL_PASSWORD="xxxx xxxx xxxx xxxx"

# Copy your Gmail email again
export MAIL_FROM="your-gmail@gmail.com"

# Verify they're set
echo $MAIL_USERNAME
echo $MAIL_PASSWORD
echo $MAIL_FROM
```

### 4. Start Application

```bash
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
mvn spring-boot:run
```

**Wait for:** `Started EcomsApplication in X seconds`

### 5. Test Email Sending (NEW TERMINAL)

```bash
# In a NEW terminal, run:
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@ecoms.com","password":"Pass123!"}'

# Then:
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email":"test@ecoms.com"}'
```

### 6. Check Results

**In Terminal 1 (app logs):**
- Should see: "Email sent successfully" (no authentication error)

**In Terminal 2 (command response):**
- Should see: `"status": 200`

**In a.n.royo@outlook.com:**
- Should receive: Password reset email with code

---

## ✅ Success Indicators

When authentication is fixed:

✅ **App logs:** No "Authentication failed" error  
✅ **App logs:** Shows "Email sent successfully"  
✅ **Response:** Status 200 from generate-code endpoint  
✅ **Email:** Arrives in a.n.royo@outlook.com within 2 minutes  

---

## 🆘 If It Still Fails

### Try These Checks:

**Check 1: Is 2FA Actually Enabled?**
```
Go to: https://myaccount.google.com/security
Look for: "2-Step Verification"
Should say: "On" (not "Off")
```

**Check 2: Is the Password Correct?**
```
Go to: https://myaccount.google.com/apppasswords
Regenerate a NEW app password
Copy ALL 16 characters
Set it: export MAIL_PASSWORD="new-password"
Restart app
```

**Check 3: Are Env Vars Really Set?**
```bash
# In your app terminal, type:
echo $MAIL_USERNAME
echo $MAIL_PASSWORD
echo $MAIL_FROM

# Should all show values (not empty)
```

**Check 4: Is Port 587 Accessible?**
```bash
# Test connection to Gmail SMTP:
nc -zv smtp.gmail.com 587

# Should show: "Connection succeeded"
```

**Check 5: Less Secure Apps (Older Gmail Accounts)**

If your Gmail is old (pre-2022), check:
1. Go to: https://myaccount.google.com/security
2. Look for: "Less secure app access"
3. If present: Turn it ON
4. Restart app

---

## 📋 Quick Checklist

Before testing again:

- [ ] 2-Factor Authentication is enabled (not Off)
- [ ] You have Gmail app password (16 characters)
- [ ] Environment variable MAIL_USERNAME is set correctly
- [ ] Environment variable MAIL_PASSWORD is set to app password (NOT regular password)
- [ ] Environment variable MAIL_FROM is set correctly
- [ ] App is restarted after setting new variables
- [ ] No typos in email addresses
- [ ] All variables verified with `echo $VARIABLE`

---

## 🎯 The Key Difference

**Regular Password:**
```
Used for: Logging into Gmail website
Works with: Gmail website, Gmail app
Format: Your personal password
Email SMTP: ❌ DOESN'T WORK
```

**App Password:**
```
Used for: Third-party applications
Works with: Email clients, scripts, apps
Format: 16 random characters
Email SMTP: ✅ WORKS PERFECTLY
```

---

## 🎉 Once It's Fixed

When authentication works:

1. Email sends successfully
2. No "Authentication failed" error
3. Emails arrive in a.n.royo@outlook.com
4. Feature is production-ready

---

## 📞 Final Checklist

```
Step 1: Enable 2FA ........................ ✅ Done?
Step 2: Get app password ................. ✅ Done?
Step 3: Set MAIL_USERNAME ................ ✅ Done?
Step 4: Set MAIL_PASSWORD (APP PW) ....... ✅ Done?
Step 5: Set MAIL_FROM .................... ✅ Done?
Step 6: Restart app ...................... ✅ Done?
Step 7: Test email sending ............... ✅ Done?
Step 8: Check a.n.royo@outlook.com ....... ✅ Done?

If ALL ✅: Email sending is WORKING!
```

---

## 🚀 Try Again Now

```bash
# 1. Get new app password from Gmail
# 2. Set variables
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-new-16-char-app-password"
export MAIL_FROM="your-gmail@gmail.com"

# 3. Start app
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
mvn spring-boot:run

# 4. Test in another terminal
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@ecoms.com","password":"Pass123!"}'

curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email":"test@ecoms.com"}'

# 5. Check a.n.royo@outlook.com for email
```

---

*Solution Guide: March 30, 2026*
*Error: Authentication failed*
*Cause: Using regular password instead of app password*
*Fix: Get Gmail app password and use it instead*

