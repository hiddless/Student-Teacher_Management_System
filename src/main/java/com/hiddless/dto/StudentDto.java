package com.hiddless.dto;

import com.hiddless.utils.ERole;
import com.hiddless.utils.EStudentType;
import com.hiddless.utils.SpecialColours;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@EqualsAndHashCode

public class StudentDto extends PersonDto implements Serializable {

    private static final long serialVersionUID = 556364655645656546L;

    private EStudentType eStudentType;
    private ERole eRole;
    private Double midTerm;
    private Double finalTerm;
    private Double resultTerm;
    private String status;


    static {
        System.out.println(SpecialColours.GREEN + "static StudentDto downloaded" + SpecialColours.RESET);
    }
    /// Constructor with Constructor
    public StudentDto(Integer id, String name, String surname, LocalDate birthDate, Double midTerm,Double finalTerm, EStudentType eStudentType, ERole eRole) {
        super(id,name ,surname,birthDate);
        this.midTerm = midTerm;
        this.finalTerm = finalTerm;
        this.resultTerm = calculateResult();
        this.status = determineStatus();
        this.eStudentType = eStudentType;
        this.eRole= eRole;
    }

    @Override
    public void displayInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Öğrenci ")
                .append(name)
                .append("")
                .append(surname)
                .append("")
                .append(eRole)
                .append("")
                .append(eStudentType)
                .append("")
                .append(midTerm)
                .append("")
                .append(finalTerm)
                .append("")
                .append(resultTerm)
                .append("")
        ;
        System.out.println(stringBuilder.toString());
    }

    private Double calculateResult() {
        if (midTerm == null|| finalTerm == null)
            return 0.0;
        else
            return (midTerm * 0.4 + finalTerm * 0.6);
    }

    private String determineStatus(){
        if (this.resultTerm == null) return "Unknown";
        return (this.resultTerm >= 50.0) ? "Geçti" : "Kaldı";
    }


    @Override
    public String toString() {
        return "StudentDto{" +
                "eStudentType=" + eStudentType +
                ", eRole=" + eRole +
                ", midTerm=" + midTerm +
                ", finalTerm=" + finalTerm +
                ", resultTerm=" + resultTerm +
                ", status='" + status + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", createdDate=" + createdDate +
                "} " + super.toString();
    }

    public EStudentType geteStudentType() {
        return eStudentType;
    }

    public void seteStudentType(EStudentType eStudentType) {
        this.eStudentType = eStudentType;
    }

    public Double getMidTerm() {
        return midTerm;
    }

    public void setMidTerm(Double midTerm) {
        this.midTerm = midTerm;
    }

    public Double getFinalTerm() {
        return finalTerm;
    }

    public void setFinalTerm(Double finalTerm) {
        this.finalTerm = finalTerm;
    }

    public Double getResultTerm() {
        return resultTerm;
    }

    public void setResultTerm(Double resultTerm) {
        this.resultTerm = resultTerm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ERole geteRole() {
        return eRole;
    }

    public void seteRole(ERole eRole) {
        this.eRole = eRole;
    }
}
