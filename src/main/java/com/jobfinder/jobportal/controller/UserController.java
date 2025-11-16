package com.jobfinder.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.jobfinder.jobportal.entity.User;
import com.jobfinder.jobportal.security.JwtTokenProvider;
import com.jobfinder.jobportal.payload.LoginRequest;
import com.jobfinder.jobportal.payload.LoginResponse;
import com.jobfinder.jobportal.payload.RegisterRequest;
import com.jobfinder.jobportal.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    // ===============================
    // 🔐 AUTH ENDPOINTS
    // ===============================

    @PostMapping(value = "/login", produces = "application/json")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user.getEmail());
                String role = user.getRole();
                String username = user.getUsername(); // 👈 παίρνουμε το username

                // ✅ Επιστρέφουμε token, role και username
                return new LoginResponse(token, role, username);
            }
        }
        throw new RuntimeException("Invalid email or password");
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Το email υπάρχει ήδη");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole() != null ? request.getRole() : "USER");

        userService.createUser(newUser);

        return "Ο χρήστης δημιουργήθηκε με επιτυχία!";
    }

    // ===============================
    // 👤 USER CRUD ENDPOINTS
    // ===============================

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

