package com.teste.controledeponto.controller;

import com.teste.controledeponto.dto.clockin.ClockinDTO;
import com.teste.controledeponto.dto.clockin.ClockinResponseDTO;
import com.teste.controledeponto.mapper.ClockinMapper;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("batidas")
public class ClockInController {

    private final ClockInService clockInService;
    private final ClockinMapper mapper;

    @PostMapping
    public ResponseEntity<ClockinResponseDTO> save(@AuthenticationPrincipal User user, @RequestBody ClockinDTO body) {

        var clockIn = clockInService.save(body, user);
        var clockinResponseDTO = mapper.entityToDto(clockIn);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(clockinResponseDTO);
    }
}
