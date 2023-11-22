package com.example.testapi.services;

import com.example.testapi.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String principal){
        return userRepo.findByUsernameOrEmail(principal, principal).orElseThrow(() -> new UsernameNotFoundException("Username/Email or password not found\n\n"));
    }

/*
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException{

        return userRepo.findFirstByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        new ArrayList<>()) // Consider populating roles/authorities if needed
                )
                .orElseThrow(() -> new CustomException("Username/Email or password not found"));
    }



    public UserDetails loadUserByPrincipal(String principal) throws UsernameNotFoundException {



    }
    public boolean isEmail(String identifier) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(identifier).matches();
    }

 */
}