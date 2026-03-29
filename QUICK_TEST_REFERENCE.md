# Generate Code Testing - Quick Reference

## ⚡ Quick Commands

```bash
# Run all tests
mvn clean test

# Run only code generation tests
mvn test -Dtest=UsersServiceTest

# Run only email tests
mvn test -Dtest=EmailServiceTest

# Run single test
mvn test -Dtest=UsersServiceTest#testGenerateCodeSuccess

# View test report
open target/surefire-reports/index.html
```

---

## 📊 Test Summary

```
✅ UsersServiceTest
   ✓ testGenerateCodeSuccess
   ✓ testGenerateCodeUserNotFound
   ✓ testGenerateCodeEmailFailed
   ✓ testGeneratedCodeFormat
   ✓ testMultipleCodeGeneration

✅ EmailServiceTest
   ✓ testSendPasswordResetCodeSuccess
   ✓ testSendPasswordResetCodeFailure
   ✓ testSendEmailSuccess
   ✓ testSendEmailFailure
   ✓ testPasswordResetEmailContent
   ✓ testSendPasswordResetCodeNullEmail
   ✓ testSendPasswordResetCodeNullCode

✅ EcomsApplicationTests
   ✓ Application Context Load

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Total: 13 Tests | All PASSED ✅ | 100% Success
```

---

## 🔬 What Each Test Does

### UsersServiceTest

**1. testGenerateCodeSuccess** 
- Given: User exists in database
- When: Code generation requested
- Then: Returns 200 with success message

**2. testGenerateCodeUserNotFound**
- Given: User doesn't exist
- When: Code generation requested
- Then: Returns 404 with error message

**3. testGenerateCodeEmailFailed**
- Given: Email sending fails (SMTP error)
- When: Code generation requested
- Then: Returns 500 with warning message

**4. testGeneratedCodeFormat**
- Given: Code is generated
- When: Code is analyzed
- Then: Code is 6-digit numeric string

**5. testMultipleCodeGeneration**
- Given: Multiple requests
- When: Generate code called twice
- Then: Both codes are valid format

### EmailServiceTest

**1. testSendPasswordResetCodeSuccess**
- Given: Valid email and code
- When: Email send requested
- Then: Returns true (success)

**2. testSendPasswordResetCodeFailure**
- Given: SMTP server fails
- When: Email send requested
- Then: Returns false (caught exception)

**3. testSendEmailSuccess**
- Given: Generic email request
- When: Email send requested
- Then: Returns true (success)

**4. testSendEmailFailure**
- Given: Network error occurs
- When: Email send requested
- Then: Returns false (handled gracefully)

**5. testPasswordResetEmailContent**
- Given: Password reset email
- When: Email is sent
- Then: Contains correct to, from, subject, body

**6. testSendPasswordResetCodeNullEmail**
- Given: Null email address
- When: Email send requested
- Then: Returns false (null-safe)

**7. testSendPasswordResetCodeNullCode**
- Given: Null code value
- When: Email send requested
- Then: Returns true (still sends)

---

## 🎯 Key Assertions

### Code Generation Tests
```java
✓ result.get("status") == 200
✓ result.get("message") == "Code generated and sent..."
✓ code.matches("\\d{6}")
✓ codeInt >= 100000 && codeInt <= 999999
✓ emailService was called exactly 1 time
✓ userRepository was called exactly 1 time
```

### Email Service Tests
```java
✓ sendPasswordResetCode() returns true/false
✓ message.getTo()[0] == email
✓ message.getFrom() == "noreply@ecoms.com"
✓ message.getSubject() == "Your E-Commerce Password Reset Code"
✓ message.getText().contains(code)
✓ mailSender.send() called exactly 1 time
```

---

## 📈 Test Flow Diagram

```
User Request
    ↓
┌─────────────────────────────────┐
│ UsersController.generateCode()  │
│ POST /api/users/generate-code   │
└──────────────┬──────────────────┘
               ↓
        ┌──────────────┐
        │ Input: email │
        └──────┬───────┘
               ↓
┌─────────────────────────────────┐
│ UsersService.generateCode()     │
├─────────────────────────────────┤
│ 1. Validate user exists         │
│    ↓ userRepository.findByEmail │
│                                 │
│ 2. Generate 6-digit code        │
│    ↓ SecureRandom.nextInt()     │
│                                 │
│ 3. Send email with code         │
│    ↓ emailService.sendCode()    │
│                                 │
│ 4. Return response              │
│    ↓ Map<String, Object>        │
└──────────────┬──────────────────┘
               ↓
        ┌─────────────────┐
        │ Response        │
        │ Status: 200/404 │
        │ Message: ...    │
        └─────────────────┘
```

---

## 🧪 Test Execution Output

### Successful Run
```
$ mvn clean test

[INFO] Scanning for projects...
[INFO] Building ecoms 0.0.1-SNAPSHOT
[INFO] 
[INFO] --- clean:3.5.0:clean (default-clean) @ ecoms ---
[INFO] Deleting /path/to/target
[INFO] 
[INFO] --- compiler:3.14.1:compile (default-compile) @ ecoms ---
[INFO] Compiling 16 source files
[INFO] 
[INFO] --- compiler:3.14.1:testCompile (default-testCompile) @ ecoms ---
[INFO] Compiling 3 test source files
[INFO] 
[INFO] --- surefire:3.5.5:test (default-test) @ ecoms ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.black.ecoms.EcomsApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, 
       Time elapsed: 2.094 s -- in com.black.ecoms.EcomsApplicationTests
[INFO] Running Users Service - Generate Code Tests
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, 
       Time elapsed: 0.147 s -- in Users Service - Generate Code Tests
[INFO] Running Email Service Tests
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, 
       Time elapsed: 0.039 s -- in Email Service Tests
[INFO] 
[INFO] Results:
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
[INFO] Total time: 3.000 s
```

---

## 🔍 Test Details

### UsersServiceTest Example
```java
@Test
@DisplayName("Should generate code and send email successfully")
void testGenerateCodeSuccess() {
    // Arrange
    String email = "john@example.com";
    when(userRepository.findByEmail(email))
        .thenReturn(Optional.of(testUser));
    when(emailService.sendPasswordResetCode(anyString(), anyString()))
        .thenReturn(true);

    // Act
    Map<String, Object> result = usersService.generateCode(email);

    // Assert
    assertEquals(200, result.get("status"));
    assertEquals("Code generated and sent to your email successfully", 
                 result.get("message"));
    verify(emailService, times(1))
        .sendPasswordResetCode(email, usersService.code);
}
```

### EmailServiceTest Example
```java
@Test
@DisplayName("Should send password reset code email successfully")
void testSendPasswordResetCodeSuccess() {
    // Arrange
    String toEmail = "user@example.com";
    String code = "123456";
    doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    // Act
    boolean result = emailService.sendPasswordResetCode(toEmail, code);

    // Assert
    assertTrue(result);
    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
}
```

---

## 📚 Test Data

### Test User
```java
Users {
    id: 1,
    name: "John Doe",
    email: "john@example.com",
    password: "encoded_password",
    roles: [ROLE_USER],
    createdAt: 2026-03-30T02:25:40,
    updatedAt: 2026-03-30T02:25:40
}
```

### Generated Code Examples
```
Code 1: 523847 ✓ (valid: 6 digits, 100000-999999)
Code 2: 891234 ✓ (valid: 6 digits, 100000-999999)
Code 3: 100000 ✓ (valid: minimum boundary)
Code 4: 999999 ✓ (valid: maximum boundary)
```

### Email Content
```
From: noreply@ecoms.com
To: john@example.com
Subject: Your E-Commerce Password Reset Code
Body:
─────────────────────────────────────────────────
Hello,

You have requested to reset your password.

Your password reset code is: 523847

This code will expire in 10 minutes.

If you did not request a password reset, 
please ignore this email.

Best regards,
E-Commerce Team
─────────────────────────────────────────────────
```

---

## 🛠️ Troubleshooting

### Test Fails: "User not found"
```
Error: Expected status 200 but got 404

Solution:
- Check mock setup: when(userRepository.findByEmail(...))
- Verify email matches in test
```

### Test Fails: "Code format invalid"
```
Error: Code doesn't match \d{6}

Solution:
- Check SecureRandom range: 100000 + nextInt(900000)
- Verify code is converted to String correctly
```

### Test Fails: "Email not sent"
```
Error: Expected emailService called but wasn't

Solution:
- Check when(emailService.sendPasswordResetCode(...))
- Verify mock returns true
```

### Build Error: "Cannot find mock classes"
```
Error: MockitoAnnotations not found

Solution:
- Verify @Mock annotations present
- Check mockito dependency added to pom.xml
- Run: mvn clean install
```

---

## 📋 Test Checklist

Before running tests:
- [ ] Project compiles without errors
- [ ] All dependencies installed (mvn clean install)
- [ ] Database configured in application.yaml
- [ ] Email service configured
- [ ] Test classes in correct package

After tests pass:
- [ ] All 13 tests showing as PASSED
- [ ] Build status showing SUCCESS
- [ ] No ERROR messages in output
- [ ] Test execution time reasonable (< 5 seconds)

---

## 🚀 Next Steps

1. **Verify Tests Pass**
   ```bash
   mvn clean test
   ```

2. **Build Application**
   ```bash
   mvn clean package
   ```

3. **Run Application**
   ```bash
   java -jar target/ecoms-0.0.1-SNAPSHOT.jar
   ```

4. **Test Endpoint with Curl**
   ```bash
   curl -X POST http://localhost:8080/api/users/generate-code \
        -H "Content-Type: application/json" \
        -d '{"email":"user@example.com"}'
   ```

---

## 📞 Support

For issues or questions:
1. Check TEST_REPORT_GENERATE_CODE.md
2. Review TESTING_GUIDE_GENERATE_CODE.md
3. Check test class comments
4. Review application logs

---

## ✨ Summary

| Metric | Value |
|--------|-------|
| Total Tests | 13 |
| Passed | 13 |
| Failed | 0 |
| Success Rate | 100% |
| Execution Time | 3.0s |
| Status | ✅ READY |

**All tests passing! Feature is ready for production. 🎉**

---

*Last Updated: March 30, 2026*

