package com.x64tech.meserver.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@Document("User")
@Setter
@Getter
public class UserModel {
    @MongoId
    private String userID;
    private String name;
    private String email;
    private String gender;
    private String username;
    private String password;
    private Role role;
    private Additional additional;

    @Override
    public String toString() {
        return "UserModel{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", additional=" + additional +
                '}';
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Additional {
        int adharNo, panNo;
        String address1, address2, city, state, zip;

        @Override
        public String toString() {
            return "Additional{" +
                    "adharNo=" + adharNo +
                    ", panNo=" + panNo +
                    ", address1='" + address1 + '\'' +
                    ", address2='" + address2 + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", zip='" + zip + '\'' +
                    '}';
        }
    }
}
