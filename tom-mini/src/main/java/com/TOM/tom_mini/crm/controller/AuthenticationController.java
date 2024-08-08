package com.TOM.tom_mini.crm.controller;

import com.TOM.tom_mini.crm.config.AuthenticationResponse;
import com.TOM.tom_mini.crm.config.AuthenticationService;
import com.TOM.tom_mini.crm.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello You!");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody RegistrationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
