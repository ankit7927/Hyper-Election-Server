package com.x64tech.meserver.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.x64tech.meserver.configs.CustomUserDetails;
import com.x64tech.meserver.models.ElectionModel;
import com.x64tech.meserver.models.VoteModel;
import com.x64tech.meserver.network.Transaction;
import com.x64tech.meserver.repository.ElectionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ElectionService {

    @Autowired
    private ElectionRepo electionRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private Transaction transaction;

    private ObjectMapper mapper = new ObjectMapper();

    public ElectionModel saveElection(ElectionModel electionModel) {
        try {
            electionModel.setCandidates(new ArrayList<>());
            return electionRepo.save(electionModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateEle(ElectionModel electionModel) throws Exception {
        Optional<ElectionModel> byId = electionRepo.findById(electionModel.getElectionID());
        if (byId.isEmpty())
            throw new Exception("Election not found");
        try {
            ElectionModel electionModel1 = byId.get();
            electionModel1.setElectionPhase(electionModel.getElectionPhase());
            electionModel1.setElectionName(electionModel.getElectionName());
            electionModel1.setElectionDic(electionModel.getElectionDic());
            electionRepo.save(electionModel1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ElectionModel> getElection() {
        return electionRepo.findAll();
    }

    public ElectionModel getElectionByID(String eleID) throws Exception {
        Optional<ElectionModel> byId = electionRepo.findById(eleID);
        if (byId.isEmpty())
            throw new Exception("Election not found..");
        else if (byId.get().getElectionPhase().equals("current")) {
            ElectionModel electionModel = byId.get();
            String state = transaction.interactContract(transaction.GETSTATE, eleID);
            Map<String, Integer> res = new HashMap<>();
            res = mapper.readValue(state, res.getClass());
            System.out.println(res);

            for (ElectionModel.Candidate candidate : electionModel.getCandidates()) {
                candidate.setVote(String.valueOf(res.get(candidate.getUserID())));
            }

            System.out.println(electionModel);
            return electionModel;
        } else
            return byId.get();
    }

    public void deleteElection(String electionID) {
        electionRepo.deleteById(electionID);
    }

    public void deleteCandidate(String candID, String eleID) throws Exception {
        Optional<ElectionModel> byId = electionRepo.findById(eleID);
        if (byId.isEmpty())
            throw new Exception("Election Not Found");
        ElectionModel electionModel = byId.get();
        List<ElectionModel.Candidate> candidates = electionModel.getCandidates();
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getUserID().equals(candID))
                candidates.remove(i);
        }
        electionModel.setCandidates(candidates);
        electionRepo.save(electionModel);
    }

    public List<ElectionModel> getCurrent() {
        List<ElectionModel> temp = new ArrayList<>();
        List<ElectionModel> all = electionRepo.findAll();

        for (ElectionModel electionModel : all) {
            if (electionModel.getElectionPhase().equals("current")
                    || electionModel.getElectionPhase().equals("upcoming"))
                temp.add(electionModel);
        }
        return temp;
    }

    public List<ElectionModel> getPastEle() {
        List<ElectionModel> temp = new ArrayList<>();
        List<ElectionModel> all = electionRepo.findAll();

        for (ElectionModel electionModel : all) {
            if (electionModel.getElectionPhase().equals("past"))
                temp.add(electionModel);
        }
        return temp;
    }

    public void addCandidate(ElectionModel.Candidate candidate) throws Exception {
        Optional<ElectionModel> optionalElectionModel = electionRepo.findById(candidate.getElectionID());
        if (optionalElectionModel.isEmpty())
            throw new Exception("Election Not Found");
        ElectionModel electionModel = optionalElectionModel.get();

        for (ElectionModel.Candidate cand : electionModel.getCandidates()) {
            if (cand.getUserID().equals(candidate.getUserID()))
                throw new Exception("You already a candidate in this election");
        }

        electionModel.getCandidates().add(candidate);
        electionRepo.save(electionModel);
    }

    /**
     * this method will check user password and then
     * connect with network to proceed for transactions
     *
     * @param userDetails
     * @param voteModel
     */
    public void giveVote(VoteModel voteModel, CustomUserDetails userDetails) throws Exception {
        final String pass = userService.confirmPass(userDetails.getUserID(), voteModel.getPassword());
        transaction.transferVote(userDetails, pass, voteModel);
    }

    public void disposeElection(String ElectionID) throws Exception {
        Optional<ElectionModel> byId = electionRepo.findById(ElectionID);
        if (byId.isEmpty())
            throw new Exception("Election not found..");
        ElectionModel electionModel = byId.get();
        String result = transaction.interactContract(transaction.DISPOSE, ElectionID);
        Map<String, Integer> res = new HashMap<>();
        res = mapper.readValue(result, res.getClass());

        for (ElectionModel.Candidate candidate : electionModel.getCandidates()) {
            candidate.setVote(String.valueOf(res.get(candidate.getUserID())));
        }
        electionModel.setElectionPhase("past");
        electionRepo.save(electionModel);
    }

}
