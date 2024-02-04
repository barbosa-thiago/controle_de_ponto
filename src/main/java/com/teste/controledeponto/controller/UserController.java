package com.teste.controledeponto.controller;

import com.teste.controledeponto.config.JwtTokenUtil;
import com.teste.controledeponto.dto.jwt.JwtRequest;
import com.teste.controledeponto.dto.jwt.JwtResponse;
import com.teste.controledeponto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            authenticationRequest.getUsername(), authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        var userDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.getUsername());

        var token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new JwtResponse(token));
    }
}
