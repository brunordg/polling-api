package com.nps.pollingapi.services.jwt;

import com.nps.pollingapi.repository.UserPrincipalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserPrincipalRepository userPrincipalRepository;

    public UserDetailsService userDetailsService() {
        return email -> userPrincipalRepository.findFirstByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
