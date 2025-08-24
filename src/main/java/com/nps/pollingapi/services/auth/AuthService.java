package com.nps.pollingapi.services.auth;

import com.nps.pollingapi.dto.SignupRequest;
import com.nps.pollingapi.dto.UserPrincipalDTO;
import com.nps.pollingapi.entity.UserPrincipal;
import com.nps.pollingapi.enums.UserRole;
import com.nps.pollingapi.repository.UserPrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserPrincipalRepository userPrincipalRepository;

    private final PasswordEncoder passwordEncoder;

    public boolean hasUserWithEmail(String email) {
        return userPrincipalRepository.findFirstByEmail(email).isPresent();
    }

    public UserPrincipalDTO createUser(SignupRequest signupRequest) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setEmail(signupRequest.email());
        userPrincipal.setPassword(passwordEncoder.encode(signupRequest.password()));
        userPrincipal.setFirstName(signupRequest.firstName());
        userPrincipal.setLastName(signupRequest.lastName());
        userPrincipal.setRole(UserRole.USER);

        userPrincipal = userPrincipalRepository.save(userPrincipal);

        return UserPrincipalDTO.fromEntity(userPrincipal);
    }
}
