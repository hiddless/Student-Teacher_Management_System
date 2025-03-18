package com.hiddless.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.logging.Logger;

public record TeacherDto(
        Integer id,
        String name,
        String surname,
        LocalDate birthDate,
        ETeacherSubject subject,
        int yearsOfExperience,
        boolean isTenured,
        double salary
) implements Serializable {

    private static final Logger logger = Logger.getLogger(TeacherDto.class.getName());

    public TeacherDto {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID cannot be negative!");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty!");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException(" Date of birth cannot be empty!");
        }
        if (subject == null) {
            throw new IllegalArgumentException("Profession field cannot be empty!\n");
        }
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Years of experience cannot be negative!");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative!");
        }
    }
    public String fullName() {
        return id + " - " + name + " " + surname + " (" + subject + ")";
    }

    public String experienceLevel() {
        if (yearsOfExperience >= 15) {
            return "Senior Teacher ";
        } else if (yearsOfExperience >= 5) {
            return "Experienced Teacher";
        } else {
            return "New Teacher";
        }
    }

    @Override
    public String toString() {
        return "TeacherDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", subject=" + subject +
                ", yearsOfExperience=" + yearsOfExperience +
                ", isTenured=" + isTenured +
                ", salary=" + salary +
                ", experienceLevel=" + experienceLevel() +
                '}';
    }
}