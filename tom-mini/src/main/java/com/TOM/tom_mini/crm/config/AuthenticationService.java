package com.TOM.tom_mini.crm.config;

import com.TOM.tom_mini.crm.dto.RegistrationRequest;
import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.entity.Role;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest request) {
        var customer = Customer.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .modifiedAt(LocalDate.now())
                .createdAt(LocalDate.now())
                .role(Role.CUSTOMER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        customerRepository.save(customer);
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(RegistrationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
