package com.x64tech.meserver.controllers;

import com.x64tech.meserver.configs.CustomUserDetails;
import com.x64tech.meserver.models.ElectionModel;
import com.x64tech.meserver.models.MessageModel;
import com.x64tech.meserver.models.VoteModel;
import com.x64tech.meserver.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("election")
public class ElectionCont {
    @Autowired
    private ElectionService electionService;

    @GetMapping("")
    public String election(Model model) {
        List<ElectionModel> election = electionService.getCurrent();
        model.addAttribute("elections", election);
        return "election";
    }

    @GetMapping("{eleID}")
    public String detail(Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("eleID") String eleID, HttpSession session) {
        try {
            ElectionModel electionByID = electionService.getElectionByID(eleID);
            model.addAttribute("election", electionByID);
            model.addAttribute("candidate", new ElectionModel.Candidate());
            model.addAttribute("userID", userDetails != null ? userDetails.getUserID() : "");
            model.addAttribute("voteModel", new VoteModel());
            return "election_details";
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel(e.getMessage(), "alert-danger"));
            return "redirect:/election";
        }
    }

    @PostMapping("apply-candidate")
    public String forCandidate(@ModelAttribute("candidate") ElectionModel.Candidate candidate, HttpSession session) {
        try {

            electionService.addCandidate(candidate);
            session.setAttribute("message", new MessageModel("Candidate Added", "alert-success"));
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel(e.getMessage(), "alert-danger"));
        }
        return "redirect:/election/" + candidate.getElectionID();
    }

    @PostMapping("give-vote")
    public String giveVote(@ModelAttribute("voteModel") VoteModel voteModel,
            @AuthenticationPrincipal CustomUserDetails userDetails, HttpSession session) {
        try {
            electionService.giveVote(voteModel, userDetails);

            session.setAttribute("message", new MessageModel("Voted successfully", "alert-success"));
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel(e.getMessage(), "alert-danger"));
        }

        return "redirect:/election/" + voteModel.getEleID();
    }

    @GetMapping("all-ele")
    public String allElection(Model model){
        List<ElectionModel> aList = electionService.getElection();
        model.addAttribute("elections", aList);
        return "list_election";
    }

    @GetMapping("past-ele")
    public String pastElections(Model model){
        List<ElectionModel> aList = electionService.getPastEle();
        model.addAttribute("elections", aList);
        return "list_election";
    }
}
