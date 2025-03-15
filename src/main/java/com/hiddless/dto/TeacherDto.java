package com.hiddless.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record TeacherDto (Integer id,
                          String name,
                          String surname,
                          LocalDate birthDate,
                          ETeacherSubject subject,
                          int yearsOfExperience,
                          boolean isTenured,
                          double salary) implements Serializable {

    /// Constructor
    public TeacherDto {
        if (id == null || id < 0) throw new IllegalArgumentException("Id cannot be negative!");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be Blank");
        if (surname == null || surname.isBlank()) throw new IllegalArgumentException("Surname cannot be Blank");
        if (birthDate == null) throw new IllegalArgumentException("Birth Date cannot be blank");
        if (subject == null) throw new IllegalArgumentException("Subject cannot be blank");
        if (yearsOfExperience < 0) throw new IllegalArgumentException("Years of Experience cannot be negative");
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
    }

    /// Methods
    public String fullName() {
        return id + " " + name + " " + surname + " " + salary + " " + yearsOfExperience;
    }

    public String experienceLevel() {
        return (yearsOfExperience > 10) ? "Senior Teacher" : "Experienced Teacher";
    }

    @Override
    public String toString() {
        return "TeacherDto{" +
                "id=" + id +
                ", name ='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", subject=" + subject +
                ", yearsOfExperience=" + yearsOfExperience +
                ", isTenured=" + isTenured +
                ", salary=" + salary +
                '}';
    }

}
