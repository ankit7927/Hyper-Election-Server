package com.x64tech.meserver.controllers;

import com.x64tech.meserver.models.ElectionModel;
import com.x64tech.meserver.models.MessageModel;
import com.x64tech.meserver.services.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/election")
public class AdminElection {

    @Autowired
    private ElectionService electionService;

    @GetMapping("")
    public String election(Model model, HttpSession session) {
        List<ElectionModel> election = electionService.getElection();
        model.addAttribute("elections", election);
        return "admin/election";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("election", new ElectionModel());
        return "admin/create_election";
    }

    @PostMapping("save")
    public String save(@ModelAttribute("election") ElectionModel electionModel, HttpSession session) {
        ElectionModel saveElection = electionService.saveElection(electionModel);
        if (saveElection != null) {
            session.setAttribute("message", new MessageModel("Election Saved", "alert-success"));
            return "redirect:/admin/election";
        }
        session.setAttribute("message", new MessageModel("Failed to save election", "alert-warning"));
        return "redirect:/admin/create";

    }

    @GetMapping("{eleID}")
    public String detail(Model model, @PathVariable("eleID") String eleID, HttpSession session) {
        try {
            ElectionModel electionByID = electionService.getElectionByID(eleID);
            model.addAttribute("election", electionByID);
            return "admin/edit_election";
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel(e.getMessage(), "alert-danger"));
            return "redirect:/admin/election";
        }

    }

    @PostMapping("update")
    public String update(@ModelAttribute("election") ElectionModel electionModel, HttpSession session) {
        try {
            electionService.updateEle(electionModel);
            session.setAttribute("message", new MessageModel("Election updated", "alert-success"));
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel(e.getMessage(), "alert-danger"));
        }
        return "redirect:/admin/election";
    }

    @GetMapping("delete/ele")
    public String deleteElection(@RequestParam(value = "eleID") String eleID,
            @RequestParam(value = "dispose") Boolean dispose, HttpSession session) {
        try {
            if (dispose)
                electionService.disposeElection(eleID);
            else {
                // electionService.deleteElection(eleID);
                System.out.println("deleting it");
            }
            session.setAttribute("message", new MessageModel("Election \"" + eleID + "\" deleted", "alert-success"));
        } catch (Exception e) {
            session.setAttribute("message",
                    new MessageModel("Failed to delete election " + e.getMessage(), "alert-warning"));
        }
        return "redirect:/admin/election";
    }

    @GetMapping("delete/cand")
    public String deleteCandidate(@RequestParam(value = "candID") String candID,
            @RequestParam(value = "eleID") String eleID, HttpSession session) {
        try {
            electionService.deleteCandidate(candID, eleID);
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel("Failed to delete Candidate " + e, "alert-danger"));
        }
        return "redirect:/admin/election/" + eleID;
    }

    @GetMapping("all-ele")
    public String allElection(Model model){
        List<ElectionModel> aList = electionService.getElection();
        model.addAttribute("elections", aList);
        return "admin/list_election";
    }

    @GetMapping("past-ele")
    public String pastElections(Model model){
        List<ElectionModel> aList = electionService.getPastEle();
        model.addAttribute("elections", aList);
        return "admin/list_election";
    }
}
