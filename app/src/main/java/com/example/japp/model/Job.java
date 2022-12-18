package com.example.japp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Job implements Serializable {

    public Job() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCompanyUid() {
        return companyUid;
    }

    public void setCompanyUid(String companyUid) {
        this.companyUid = companyUid;
    }

    public Job(int id, String title, String description, ArrayList<String> requirements, String companyName, String companyImage, String category, String type, String location, String experience, String qualification, String status, String specialization, String companyUid) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.companyName = companyName;
        this.companyImage = companyImage;
        this.category = category;
        this.type = type;
        this.location = location;
        this.experience = experience;
        this.status = status;
        this.specialization = specialization;
        this.companyUid = companyUid;
        this.qualification = qualification;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String title;
    String description;
    List<String> requirements;
    String companyName;
    String companyImage;

    public ArrayList<String> getSaved() {
        return saved;
    }

    public void setSaved(ArrayList<String> saved) {
        this.saved = saved;
    }

    String category;
    String type;
    String location;
    String experience;
    String qualification;
    String status;
    String specialization;
    String companyUid;
    ArrayList<String> saved;
}
