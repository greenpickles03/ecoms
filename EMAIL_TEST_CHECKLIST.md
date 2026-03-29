# 📧 Email Sending Test - Complete Checklist

## ✅ Pre-Test Checklist

### Account & Credentials
- [ ] You have a Gmail account
- [ ] You've generated an app password (https://myaccount.google.com/apppasswords)
- [ ] You have the test email: a.n.royo@outlook.com ready
- [ ] MySQL database is running (test with: `mysql -u root -p`)

### Environment Setup
- [ ] Terminal open and ready
- [ ] Project directory accessible: `/Users/andrewneil/Documents/Dev App/Intellij/ecoms`
- [ ] Maven installed (test with: `mvn --version`)
- [ ] Java 17+ installed (test with: `java --version`)

### Project Status
- [ ] Project compiles without errors
- [ ] Database credentials correct in application.yaml
- [ ] Port 8080 is available (not in use by other apps)

---

## 🚀 Step-By-Step Test

### Step 1: Set Environment Variables
- [ ] Opened Terminal 1
- [ ] Ran: `export MAIL_USERNAME="your-gmail@gmail.com"`
- [ ] Ran: `export MAIL_PASSWORD="your-app-password"`
- [ ] Ran: `export MAIL_FROM="your-gmail@gmail.com"`
- [ ] Verified with: `echo $MAIL_USERNAME` (should show your email)

### Step 2: Navigate to Project
- [ ] Ran: `cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"`
- [ ] Verified with: `pwd` (should show ecoms directory)

### Step 3: Start Application
- [ ] Ran: `mvn spring-boot:run`
- [ ] Waited for: "Started EcomsApplication in X.XXX seconds"
- [ ] No error messages in logs
- [ ] Application ready at: http://localhost:8080

### Step 4: Open Second Terminal
- [ ] Terminal 2 opened
- [ ] Navigated to project: `cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"`

### Step 5: Create Test User
- [ ] Ran register command in Terminal 2
- [ ] Received response with status: 201
- [ ] Response contains user details
- [ ] User email is: test@ecoms.com (or your test email)

### Step 6: Generate Password Reset Code
- [ ] Ran generate-code command in Terminal 2
- [ ] Received response with status: 200
- [ ] Response message: "Code generated and sent to your email successfully"
- [ ] No error messages in Terminal 1

### Step 7: Check Email
- [ ] Opened: https://outlook.live.com
- [ ] Logged in to: a.n.royo@outlook.com
- [ ] Waited 1-2 minutes for email delivery
- [ ] Email appeared in Inbox (or Junk folder)
- [ ] Email From: your-gmail@gmail.com
- [ ] Email Subject: "Your E-Commerce Password Reset Code"
- [ ] Email Body contains: 6-digit code (e.g., 523847)

---

## ✅ Success Verification

### Terminal 1 (Application Server) Should Show:
- [ ] "Starting EcomsApplication"
- [ ] "Started EcomsApplication in X.XXX seconds"
- [ ] No ERROR messages
- [ ] Port 8080 is active

### Terminal 2 (Test Commands) Should Show:
- [ ] Register response: status 201
- [ ] Generate-code response: status 200
- [ ] No connection errors

### Email (a.n.royo@outlook.com) Should Show:
- [ ] One or more emails from your-gmail@gmail.com
- [ ] Subject: "Your E-Commerce Password Reset Code"
- [ ] Each email contains a different 6-digit code
- [ ] Email was received within 2 minutes of request

---

## 🐛 Troubleshooting Checklist

### If No Email Received After 5 Minutes

**Check 1: Verify Response Status**
- [ ] Did generate-code return status 200? (Check Terminal 2)
- [ ] If 404: User doesn't exist (register user again)
- [ ] If 500: Email service failed (check Terminal 1 logs)

**Check 2: Email Client**
- [ ] Checked Inbox in Outlook
- [ ] Checked Spam/Junk folder
- [ ] Checked Other folders
- [ ] Refreshed inbox (F5)

**Check 3: Environment Variables**
- [ ] Verified MAIL_USERNAME is set: `echo $MAIL_USERNAME`
- [ ] Verified MAIL_PASSWORD is set: `echo $MAIL_PASSWORD`
- [ ] Verified MAIL_FROM is set: `echo $MAIL_FROM`
- [ ] All are NOT empty
- [ ] Password has no extra spaces

**Check 4: Application Logs**
- [ ] Check Terminal 1 for "ERROR" messages
- [ ] Check for "Failed to send email"
- [ ] Check for "Connection timeout"
- [ ] Check for "Authentication failed"

**Check 5: Gmail Settings**
- [ ] Used app password (not regular password)
- [ ] App password is 16 characters
- [ ] Gmail account allows app passwords
- [ ] No special security rules blocking

**Check 6: Try Again**
- [ ] Stop app: Ctrl+C in Terminal 1
- [ ] Restart app: `mvn spring-boot:run`
- [ ] Wait 30 seconds
- [ ] Run test again in Terminal 2
- [ ] Wait 2 minutes for email

---

## 📊 Test Results Log

### Attempt 1
- Date/Time: _______________
- User Email: _______________
- Generate Code Response: _______________
- Email Received: ☐ Yes ☐ No ☐ In Spam
- Code in Email: _______________
- Issues: _______________

### Attempt 2
- Date/Time: _______________
- User Email: _______________
- Generate Code Response: _______________
- Email Received: ☐ Yes ☐ No ☐ In Spam
- Code in Email: _______________
- Issues: _______________

### Attempt 3
- Date/Time: _______________
- User Email: _______________
- Generate Code Response: _______________
- Email Received: ☐ Yes ☐ No ☐ In Spam
- Code in Email: _______________
- Issues: _______________

---

## 🎯 Final Validation

After successful email receipt, verify:

- [ ] Email From address matches configured MAIL_FROM
- [ ] Email To address matches recipient (a.n.royo@outlook.com)
- [ ] Email Subject is exactly: "Your E-Commerce Password Reset Code"
- [ ] Email Body includes greeting and signature
- [ ] Code in email is 6 digits (0-9 only)
- [ ] Code matches pattern: [0-9]{6}
- [ ] Each email has different code (if sent multiple times)
- [ ] Email arrives within 2 minutes of request

---

## 📋 Command Reference

### Start Application
```bash
cd "/Users/andrewneil/Documents/Dev App/Intellij/ecoms"
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-app-password"
export MAIL_FROM="your-gmail@gmail.com"
mvn spring-boot:run
```

### Register User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@ecoms.com","password":"Pass123!"}'
```

### Generate Code
```bash
curl -X POST http://localhost:8080/api/users/generate-code \
  -H "Content-Type: application/json" \
  -d '{"email":"test@ecoms.com"}'
```

---

## ✨ Success Criteria

**Email Test is SUCCESSFUL when:**

✅ Application starts without errors  
✅ Register endpoint returns status 201  
✅ Generate-code endpoint returns status 200  
✅ Email received in a.n.royo@outlook.com  
✅ Email from your Gmail account  
✅ Email contains correct subject  
✅ Email body contains 6-digit code  
✅ Email arrives within 2 minutes  

**If ALL above are YES → Email Sending is WORKING ✅**

---

## 🎉 Test Complete!

Once you've verified all above items:

**Email Sending Feature is PRODUCTION-READY** ✅

You can now:
1. Use this feature in production
2. Test with real user emails
3. Integrate with frontend
4. Deploy to staging/production

---

## 📞 Support Documents

If you need help:

1. **Quick Commands:** `EMAIL_TEST_COMMANDS.md`
2. **Quick Start:** `EMAIL_SENDING_QUICK_START.md`
3. **Detailed Guide:** `EMAIL_SENDING_TEST_GUIDE.md`
4. **Unit Tests:** `mvn test -Dtest=EmailServiceTest`

---

*Checklist Version: 1.0*
*Created: March 30, 2026*
*Test Email: a.n.royo@outlook.com*

