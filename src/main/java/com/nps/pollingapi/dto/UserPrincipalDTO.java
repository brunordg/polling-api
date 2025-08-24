package com.nps.pollingapi.dto;

import com.nps.pollingapi.entity.UserPrincipal;
import com.nps.pollingapi.enums.UserRole;

import java.io.Serializable;

/**
 * DTO for {@link UserPrincipal}
 */
public record UserPrincipalDTO(Long id,
                               String email,
                               String password,
                               String firstName,
                               String lastName,
                               UserRole role
) implements Serializable {

    public UserPrincipalDTO(Long id) {
        this(id, null, null, null, null, null);
    }

    public static UserPrincipalDTO fromEntity(UserPrincipal userPrincipal) {
        return new UserPrincipalDTO(
                userPrincipal.getId(),
                userPrincipal.getEmail(),
                userPrincipal.getPassword(),
                userPrincipal.getFirstName(),
                userPrincipal.getLastName(),
                userPrincipal.getRole()
        );
    }

    public UserPrincipal toEntity() {
        return new UserPrincipal(
                this.id,
                this.email,
                this.password,
                this.firstName,
                this.lastName,
                this.role
        );
    }
}