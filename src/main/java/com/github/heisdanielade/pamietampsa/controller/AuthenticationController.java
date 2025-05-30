package com.github.heisdanielade.pamietampsa.controller;

import com.github.heisdanielade.pamietampsa.dto.user.LoginUserDto;
import com.github.heisdanielade.pamietampsa.dto.user.RegisterUserDto;
import com.github.heisdanielade.pamietampsa.dto.user.ResendVerificationEmailRequestDto;
import com.github.heisdanielade.pamietampsa.dto.user.VerifyUserDto;
import com.github.heisdanielade.pamietampsa.entity.AppUser;
import com.github.heisdanielade.pamietampsa.response.ApiResponse;
import com.github.heisdanielade.pamietampsa.service.auth.AuthenticationService;
import com.github.heisdanielade.pamietampsa.service.auth.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/v1/auth", produces = "application/json")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path ="/signup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@Valid @RequestBody RegisterUserDto input) {
        AppUser registeredUser = authenticationService.signup(input);

        Map<String, Object> data = new HashMap<>();
        data.put("email", registeredUser.getEmail());
        data.put("role", registeredUser.getRole());
        data.put("enabled", registeredUser.isEnabled());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User registered successfully. Proceed to email verification.",
                data
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> authenticate(@RequestBody LoginUserDto input) {
        AppUser authenticatedUser = authenticationService.authenticate(input);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtToken);
        data.put("expirationTime", jwtService.getEXPIRATION_TIME());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User authenticated successfully.",
                data
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/verify-email")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyUser(@RequestBody VerifyUserDto input){
        authenticationService.verifyUser(input);
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Email verified successfully."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/resend-verification-email")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resendVerificationCode(@RequestBody ResendVerificationEmailRequestDto input){
        authenticationService.resendVerificationEmail(input);
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Email verification code sent successfully."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
