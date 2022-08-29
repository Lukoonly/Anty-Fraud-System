package antifraud.service;

import antifraud.domain.entity.User;
import antifraud.domain.repository.UserRep;
import antifraud.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {

    private UserRep userRep;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRep.findUserByUsernameIgnoreCase(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return new UserPrincipal(user.get());
    }
}