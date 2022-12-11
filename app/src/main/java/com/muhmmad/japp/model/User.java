package com.muhmmad.japp.model;

import java.util.ArrayList;

public class User {

    public User() {

    }

    public User(String name, String email, String phone, String type) {
        this.name = name;
        this.email = email;
        this.type = type;
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User(String name, String email, String phone, String gender, String type, String dateOfBirth, String country, String nationality, String city, ArrayList<String> skills, ArrayList<String> education, ArrayList<String> languages) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.nationality = nationality;
        this.city = city;
        this.skills = skills;
        this.education = education;
        this.languages = languages;
        this.gender = gender;
    }

    public User(String name, String email, String phone, String type, String country, String city, String companySize, String description, ArrayList<Job> jobs) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.country = country;
        this.city = city;
        this.companySize = companySize;
        this.description = description;
        this.jobs = jobs;
    }

    String name;
    String email;
    String type;
    String phone;
    String dateOfBirth;
    String country;
    String nationality;
    String city;
    String photo;
    ArrayList<String> skills;
    ArrayList<String> education;
    ArrayList<String> languages;
    String companySize;
    String description;
    ArrayList<Job> jobs;
    String gender;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public ArrayList<String> getEducation() {
        return education;
    }

    public void setEducation(ArrayList<String> education) {
        this.education = education;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
