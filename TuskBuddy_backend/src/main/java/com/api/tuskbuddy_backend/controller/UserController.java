package com.api.tuskbuddy_backend.controller;

import com.api.tuskbuddy_backend.config.JwtService;
import com.api.tuskbuddy_backend.dto.AuthResponse;
import com.api.tuskbuddy_backend.dto.LoginRequest;
import com.api.tuskbuddy_backend.dto.UserRegistrationRequest;
import com.api.tuskbuddy_backend.dto.UserResponse;
import com.api.tuskbuddy_backend.exception.UnauthorizedException;
import com.api.tuskbuddy_backend.entity.User;
import com.api.tuskbuddy_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        User registered = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(registered.getId(), registered.getUsername(), registered.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password");
        }
        User user = userService.findByEmail(request.getEmail());
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
