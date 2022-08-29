package antifraud.api.controller;

import antifraud.api.dto.*;
import antifraud.api.mapper.ResponseMapper;
import antifraud.api.mapper.TransactionMapper;
import antifraud.api.mapper.UserMapper;
import antifraud.service.ControlService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/auth")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@RestController
public class UserEntityController {

    ControlService controlService;
    ResponseMapper responseMapper;
    UserMapper userMapper;
    TransactionMapper transactionMapper;

    @PostMapping("/user")
    public ResponseEntity<ResponseSuccessRegDTO> regNewUser(@RequestBody @Valid SignUpUserDTO signUpUserDTO) {
        return new ResponseEntity<>(userMapper.toResponseSuccessRegDTOFromUser(controlService
                .regNewUser(signUpUserDTO)), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public List<ResponseSuccessRegDTO> getListOfUsers() {
        return controlService.getListOfUsers().stream()
                .map(user -> userMapper.toResponseSuccessRegDTOFromUser(user))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/user/{username}")
    public SuccessDeleteResponseDTO deleteUser(@PathVariable String username) {
        controlService.deleteUserByUserName(username);
        return SuccessDeleteResponseDTO.builder()
                .username(username)
                .status("Deleted successfully!")
                .build();
    }

    @PutMapping("/role")
    public ResponseSuccessRegDTO updateRole(@RequestBody @Valid UpdateRoleRequestDTO updateRoleRequestDTO) {
        return userMapper.toResponseSuccessRegDTOFromUser(controlService.updateRole(updateRoleRequestDTO));
    }

    @PutMapping("/access")
    public SuccessStatusResponseDTO setActivityUser(@RequestBody @Valid ActivityUpdateDTO activityUpdateDTO) {
        controlService.setUserActivity(activityUpdateDTO);
        return responseMapper.toSuccessStatusResponseDTO(activityUpdateDTO);
    }
}