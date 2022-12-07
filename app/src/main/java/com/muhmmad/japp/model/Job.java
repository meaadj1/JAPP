package com.muhmmad.japp.model;

import java.util.ArrayList;

public class Job {

    public Job(String title, String description, String companyName, String companyImage, String category, String type, String location, ArrayList<String> requirements, String experience, String status, String specialization, String companyUid) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.companyImage = companyImage;
        this.category = category;
        this.type = type;
        this.location = location;
        this.requirements = requirements;
        this.experience = experience;
        this.status = status;
        this.specialization = specialization;
        this.companyUid = companyUid;
    }

    String title;
    String description;
    String companyName;
    String companyImage;
    String category;
    String type;
    String location;
    ArrayList<String> requirements;
    String experience;
    String status;
    String specialization;
    String companyUid;

}
