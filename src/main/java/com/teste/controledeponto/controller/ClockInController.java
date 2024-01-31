package com.teste.controledeponto.controller;

import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.model.User;
import com.teste.controledeponto.service.ClockInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("batidas")
public class ClockInController {

    private final ClockInService clockInService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid ClockinDTO body,
                                     @AuthenticationPrincipal User user) {

        clockInService.save(body, user);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }
}
