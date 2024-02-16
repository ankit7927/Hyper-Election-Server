package com.x64tech.meserver.services;

import com.x64tech.meserver.models.Role;
import com.x64tech.meserver.models.UserModel;
import com.x64tech.meserver.network.BCNetwork;
import com.x64tech.meserver.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(UserModel userModel) throws Exception {
        
        UserModel optional = userRepo.existByUusernameEmail(userModel.getUsername(), userModel.getEmail());

        if (optional != null) 
            throw new Exception("username or email alredy taken......");

        //BCNetwork.RegisterUser(userModel.getUsername(), userModel.getPassword());

        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setAdditional(new UserModel.Additional());
        userModel.setRole(Role.USER);
        userRepo.save(userModel);
    }

    public UserModel getUserPro(String userID) {
        UserModel userModel = userRepo.userByUSerID(userID);
        userModel.setPassword("");
        return userModel;
    }

    public void updatePro(String userID, UserModel userModel) throws Exception {
        Optional<UserModel> oldUser = userRepo.findById(userID);
        if (oldUser.isEmpty())
            throw new Exception("user not found");
        UserModel temp = oldUser.get();
        temp.setName(userModel.getName());
        temp.setEmail(userModel.getEmail());
        temp.setGender(userModel.getGender());

        userRepo.save(temp);
    }

    public void updateAdd(String userID, UserModel.Additional additional) throws Exception {
        Optional<UserModel> oldUser = userRepo.findById(userID);
        if (oldUser.isEmpty())
            throw new Exception("user not found");
        UserModel temp = oldUser.get();
        temp.setAdditional(additional);
        userRepo.save(temp);
    }

    public String confirmPass(String userID, String password) throws Exception {
        Optional<UserModel> user = userRepo.findById(userID);
        if (user.isEmpty())
            throw new Exception("user not found");
        else if(passwordEncoder.matches(password, user.get().getPassword())){
            return password;
        }else{
            throw new Exception("Password not matched...");
        }
    }

}
