package com.x64tech.meserver.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class VoteModel {
    String eleID, candID, password;

    @Override
    public String toString() {
        return "VoteModel{" +
                "eleID='" + eleID + '\'' +
                ", candID='" + candID + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
