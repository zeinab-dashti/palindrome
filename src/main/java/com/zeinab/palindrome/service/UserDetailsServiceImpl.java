package com.zeinab.palindrome.service;

import com.zeinab.palindrome.entity.UserEntity;
import com.zeinab.palindrome.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;


@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username));
        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                aListOfAuthoritiesWithEmbeddedRoleAndRefreshToken(userEntity));

    }

    private Collection<? extends GrantedAuthority> aListOfAuthoritiesWithEmbeddedRoleAndRefreshToken(UserEntity userEntity) {
        return Arrays.asList(
                new SimpleGrantedAuthority(userEntity.getRole().name()),
                new SimpleGrantedAuthority(userEntity.getRefreshToken())
        );
    }
}