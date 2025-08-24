package com.nps.pollingapi.controller;

import com.nps.pollingapi.dto.UserPrincipalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("user")
public class UserPrincipalController {

    @PostMapping
    public ResponseEntity<UserPrincipalDTO> save(@RequestBody UserPrincipalDTO userPrincipalDTO) {
        return ResponseEntity.ok(new UserPrincipalDTO(1L,
                userPrincipalDTO.email(),
                userPrincipalDTO.password(),
                userPrincipalDTO.firstName(),
                userPrincipalDTO.lastName(),
                userPrincipalDTO.role()));
    }

}
