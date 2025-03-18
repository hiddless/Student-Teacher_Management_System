package com.hiddless.dto;

import java.time.LocalDate;
import java.util.Date;

abstract public class PersonDto {

    protected Integer id;
    protected String name;
    protected String surname;
    protected LocalDate birthDate;
    protected final Date createdDate;


    public PersonDto() {
        this.id = 0;
        this.name = "Unknown";
        this.surname = "Unknown";
        this.birthDate = LocalDate.now();
        this.createdDate = new Date();
    }


    public PersonDto(Integer id, String name, String surname, LocalDate birthDate) {
        this.id = (id != null) ? id : 0;
        this.name = (name != null && !name.isBlank()) ? name : "Unknown";
        this.surname = (surname != null && !surname.isBlank()) ? surname : "Unknown";
        this.birthDate = (birthDate != null) ? birthDate : LocalDate.now();
        this.createdDate = new Date();
    }

    public abstract void displayInfo();

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", createdDate=" + createdDate +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = (id != null) ? id : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name != null && !name.isBlank()) ? name : "Unknown";
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = (surname != null && !surname.isBlank()) ? surname : "Unknown";
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = (birthDate != null) ? birthDate : LocalDate.now();
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}