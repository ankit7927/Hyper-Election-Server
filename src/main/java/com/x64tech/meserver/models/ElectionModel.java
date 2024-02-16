package com.x64tech.meserver.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document("Election")
@NoArgsConstructor
@Setter
@Getter
public class ElectionModel {
    @MongoId
    private String electionID;
    private String electionName;
    private String electionDic;
    private String electionPhase;
    private List<Candidate> candidates;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Candidate {
        private String candidateName;
        private String electionID;
        private String userID;
        private String logo;
        private String vote;

        @Override
        public String toString() {
            return "Candidate{" +
                    "candidateName='" + candidateName + '\'' +
                    ", electionID='" + electionID + '\'' +
                    ", userID='" + userID + '\'' +
                    ", logo='" + logo + '\'' +
                    ", vote='" + vote + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ElectionModel{" +
                "electionID='" + electionID + '\'' +
                ", electionName='" + electionName + '\'' +
                ", electionDic='" + electionDic + '\'' +
                ", electionPhase='" + electionPhase + '\'' +
                ", candidates=" + candidates +
                '}';
    }
}
