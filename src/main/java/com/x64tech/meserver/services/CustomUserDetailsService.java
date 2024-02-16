package com.x64tech.meserver.services;
import com.x64tech.meserver.configs.CustomUserDetails;
import com.x64tech.meserver.models.UserModel;
import com.x64tech.meserver.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepo.userByUsername(username);
        if (userModel == null)
            throw new UsernameNotFoundException("username not found....");
        return new CustomUserDetails(userModel);
    }
}
