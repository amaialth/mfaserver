package com.anbu.mfaserver.controller;

import com.anbu.mfaserver.exception.InvalidTokenException;
import com.anbu.mfaserver.model.LoginRequest;
import com.anbu.mfaserver.model.MfaVerificationRequest;
import com.anbu.mfaserver.model.MfaVerificationResponse;
import com.anbu.mfaserver.model.User;
import com.anbu.mfaserver.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AuthController {

    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;

    public AuthController(UserService userService, AuthenticationProvider authenticationProvider) {
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody User user) {
        // Register User // Generate QR code using the Secret KEY
        try {
            return ResponseEntity.ok(userService.registerUser(user));
        } catch (QrGenerationException e) {
            return ResponseEntity.internalServerError().body("Something went wrong. Try again.");
        }
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        // Validate the user credentials and return the JWT / send redirect to MFA page
        try {//Get the user and Compare the password
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(MfaVerificationResponse.builder()
                    .username(loginRequest.getUsername())
                    .tokenValid(Boolean.FALSE)
                    .authValid(Boolean.TRUE)
                    .mfaRequired(Boolean.TRUE)
                    .message("User Authenticated using username and Password")
                    .jwt("")
                    .build());

        } catch (Exception e){
            return ResponseEntity.ok(MfaVerificationResponse.builder()
                    .username(loginRequest.getUsername())
                    .tokenValid(Boolean.FALSE)
                    .authValid(Boolean.FALSE)
                    .mfaRequired(Boolean.FALSE)
                    .message("Invalid Credentials. Please try again.")
                    .jwt("")
                    .build());
        }
    }

    @PostMapping("/verifyTotp")
    public ResponseEntity<?> verifyTotp(@Validated @RequestBody MfaVerificationRequest request) {
        MfaVerificationResponse mfaVerificationResponse = MfaVerificationResponse.builder()
                .username(request.getUsername())
                .tokenValid(Boolean.FALSE)
                .message("Token is not Valid. Please try again.")
                .build();
        // Validate the OTP
        if(userService.verifyTotp(request.getTotp(), request.getUsername())){
            mfaVerificationResponse = MfaVerificationResponse.builder()
                    .username(request.getUsername())
                    .tokenValid(Boolean.TRUE)
                    .message("Token is valid")
                    .jwt("DUMMYTOKEN")
                    .build();
        }
        return ResponseEntity.ok(mfaVerificationResponse);
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) throws InvalidTokenException {
        try{
            if(userService.verifyUser(token)){
                return ResponseEntity.ok("Your email has been successfully verified.");
            } else {
                return ResponseEntity.ok("User details not found. Please login and regenerate the confirmation link.");
            }
        } catch (InvalidTokenException e){
            return ResponseEntity.ok("Link expired or token already verified.");
        }
    }
}
