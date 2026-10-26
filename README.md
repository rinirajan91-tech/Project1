# Selenium + TestNG practice framework

Java 17+ test framework for the Practice Test Automation website. Selenium Manager
resolves the ChromeDriver version for the installed Chrome browser.

## Run

Install Java 17 or newer, then run all four scenarios from PowerShell:

```powershell
.\mvnw.cmd test
```

The Maven wrapper downloads the configured Maven version the first time it runs.
The browser runs headless by default. To see the browser:

```powershell
.\mvnw.cmd test -Dbrowser.headless=false
```

## Scenarios

1. Log in with `student` / `Password123` and verify the success page and Log out link.
2. Use the Home link in the site header and verify the Home page can be scrolled to its end.
3. Open the Practice hub, follow **Test Login Page**, and verify login succeeds.
4. Open the Practice hub, follow **Test Table**, select **Language: Java**, and verify every visible course is Java.

Each test starts and closes its own browser session so cases can run independently.
