package com.x64tech.meserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MessageModel {
    String content, type;

    @Override
    public String toString() {
        return "MessageModel{" +
                "content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
