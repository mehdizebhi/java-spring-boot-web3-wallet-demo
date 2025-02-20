package dev.mehdizebhi.web3.models;


import dev.mehdizebhi.web3.entities.UserEntity;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class SpringUser extends User {
    private final UserEntity user;

    public SpringUser(UserEntity user) {
        super(user.getUsername(), user.getPasswordHash(), true, true, true, true, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.user = user;
    }
}
