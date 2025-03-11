package com.hiddless.dto;

import java.time.LocalDate;
import java.util.Date;

abstract public class PersonDto {

    protected Integer id;
    protected String name;
    protected String surname;
    protected LocalDate birthDate;
    protected Date createdDate;

    /// Constructor without Parameter
    public PersonDto() {
        this.id = 0;
        this.name = "name unknown";
        this.surname = "surname unknown";
        this.birthDate= LocalDate.now();
        this.createdDate = new Date(System.currentTimeMillis());
    }

    /// Constructor with Parameter
    public PersonDto(Integer id ,String name, String surname, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.createdDate = new Date(System.currentTimeMillis());
    }

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

    abstract public void displayInfo();

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
