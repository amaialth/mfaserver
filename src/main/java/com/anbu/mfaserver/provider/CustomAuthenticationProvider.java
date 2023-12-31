package com.anbu.mfaserver.provider;

import com.anbu.mfaserver.model.User;
import com.anbu.mfaserver.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> userDetails = userRepository.findByUsername(username);
        if(userDetails.isPresent()){
            if (userDetails != null && passwordEncoder.matches(password, userDetails.get().getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDetails.get(), password);
            } else {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new UsernameNotFoundException("Username Not Found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
