package com.jobfinder.jobportal.payload;

public class RegisterRequest {
    private String email;
    private String password;
    private String role;
    private String username;     // 👈 new field
    private String companyName;  // 👈 new field (only for COMPANY)
    private String fullName;     // 👈 new field (only for APPLICANT)

    // 🧱 Default constructor για Spring / Jackson
    public RegisterRequest() {}

    // 🔧 Constructor για χειροκίνητη δημιουργία, αν χρειαστεί
    public RegisterRequest(String email, String password, String role,
                           String username, String companyName, String fullName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
        this.companyName = companyName;
        this.fullName = fullName;
    }

    // ✏️ Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // 📤 Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getFullName() {
        return fullName;
    }
}



