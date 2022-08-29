package antifraud.service;

import antifraud.api.dto.ActivityUpdateDTO;
import antifraud.api.dto.SignUpUserDTO;
import antifraud.api.dto.UpdateRoleRequestDTO;
import antifraud.api.mapper.UserMapper;
import antifraud.domain.entity.User;
import antifraud.domain.entity.enumEntityes.UserRoles;
import antifraud.domain.exceptions.BadRequestException;
import antifraud.domain.exceptions.ConflictDataException;
import antifraud.domain.exceptions.NotFoundException;
import antifraud.domain.exceptions.UserNotSaveExceptions;
import antifraud.domain.repository.IpRep;
import antifraud.domain.repository.StolenCardRep;
import antifraud.domain.repository.UserRep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Service
public class ControlService {

    private UserRep userRep;
    private UserMapper userMapper;
    private PasswordEncoder encoder;
    private StolenCardRep stolenCardRep;
    private IpRep ipRep;

    public User regNewUser(SignUpUserDTO signUpUserDTO) {
        String role;
        boolean isAccountLocked;
        if (userRep.findUserByUsernameIgnoreCase(signUpUserDTO.getUsername()).isPresent()) {
            throw new UserNotSaveExceptions("User not save!");
        }
        if (userRep.count() == 0) {
            role = UserRoles.ADMINISTRATOR.toString();
            isAccountLocked = true;
        } else {
            role = UserRoles.MERCHANT.toString();
            isAccountLocked = false;
        }
        return userRep.save(userMapper.toUserFromSignUpUserDTO(signUpUserDTO, encoder, role, isAccountLocked));
    }

    public List<User> getListOfUsers() {
        List<User> result = new ArrayList<>();
        userRep.findAll().forEach(result::add);
        return result;
    }

    public void deleteUserByUserName(String username) {
        User user = getUerByUsernameIgnoreCase(username);
        userRep.delete(user);
    }

    public User updateRole(UpdateRoleRequestDTO updateRoleRequestDTO) {
        String username = updateRoleRequestDTO.getUsername();
        User user = getUerByUsernameIgnoreCase(username);
        String role = "ROLE_" + updateRoleRequestDTO.getRole();
        if (user.getRole().equals(role)) {
            throw new ConflictDataException("The user already has that role");
        }
        if (!(role.equals(UserRoles.MERCHANT.toString()) || role.equals(UserRoles.SUPPORT.toString()))) {
            throw new BadRequestException("Wrong role!");
        }
        user.setRole(role);
        return userRep.save(user);
    }


    public void setUserActivity(ActivityUpdateDTO activityUpdateDTO) {
        String username = activityUpdateDTO.getUsername();
        User user = getUerByUsernameIgnoreCase(username);
        if (user.getRole().equals(UserRoles.ADMINISTRATOR.toString())) {
            throw new NotFoundException("Wrong username, user is not exists");
        }
        user.setAccountNonLocked(activityUpdateDTO.getOperation().equalsIgnoreCase(("UNLOCK")));
        userRep.save(user);
    }

    private User getUerByUsernameIgnoreCase(String username) {
        return userRep.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Wrong username, user is not exists"));
    }
}