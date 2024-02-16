package com.x64tech.meserver.repository;

import com.x64tech.meserver.models.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<UserModel, String> {
    @Query("{'username':?0}")
    UserModel userByUsername(String username);

    @Query("{'userID':?0}")
    UserModel userByUSerID(String userID);

    @Query("{'username':?0, 'email':?1}")
    UserModel existByUusernameEmail(String username, String email);
}
