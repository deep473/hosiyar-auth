package com.auth.assessment.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    //ADMIN
    @GetMapping("/create")
    public String createAssessment() {
        return "Assessment created successfully";
    }

    //STUDENT
    @GetMapping("/result")
    public String viewAssessmentResult() {
        return "Your assessment result is: 85%";
    }
}