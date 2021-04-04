package org.jaguar.test.service;


import org.jaguar.test.model.User;
import org.jaguar.test.model.UserAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/4/2
 */
@Service
public class UserService implements UserDetailsService {

    private static final Map<String, User> USERS = new HashMap<>();

    static {
        User user = new User("aa", "aa");

        Set<UserAuthority> userAuthorities = user.getAuthorities();
        userAuthorities.add(new UserAuthority("aaa"));
        userAuthorities.add(new UserAuthority("aaaa"));

        USERS.put(user.getUsername(), user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = USERS.get(username);
        if (user == null) {
            throw new UsernameNotFoundException(null);
        }
        return user;
    }
}
