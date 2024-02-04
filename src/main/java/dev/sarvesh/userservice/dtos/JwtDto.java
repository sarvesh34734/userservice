package dev.sarvesh.userservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JwtDto {

    private String userId;
    private String email;
    private List<String> roles;
    private Long expiry;

}
