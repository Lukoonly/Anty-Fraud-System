package antifraud.api.mapper;

import antifraud.api.dto.ResponseSuccessRegDTO;
import antifraud.api.dto.SignUpUserDTO;
import antifraud.domain.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public User toUserFromSignUpUserDTO(SignUpUserDTO signUpUserDTO, PasswordEncoder passwordEncoder, String role,
                                        boolean isAccountLocked) {
        return User.builder()
                .username(signUpUserDTO.getUsername())
                .name(signUpUserDTO.getName())
                .password(passwordEncoder.encode(signUpUserDTO.getPassword()))
                .role(role)
                .accountNonLocked(isAccountLocked)
                .build();
    }

    public ResponseSuccessRegDTO toResponseSuccessRegDTOFromUser(User user) {
        return ResponseSuccessRegDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole().replaceAll("ROLE_", ""))
                .build();
    }
}