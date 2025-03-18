package com.hiddless.dto;

import com.hiddless.utils.SpecialColours;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.logging.Logger;

public class StudentDto extends PersonDto implements Serializable {

    // Serileştirme
    private static final long serialVersionUID = 556364655645656546L;

    // Logger
    private static final Logger logger = Logger.getLogger(StudentDto.class.getName());


    private EStudentType eStudentType;
    private ERole eRole;
    private Double midTerm;
    private Double finalTerm;
    private Double resultTerm;
    private String status;

    static {
        System.out.println(SpecialColours.BLUE + " static StudentDto Installed" + SpecialColours.RESET);
    }

    public StudentDto() {
        super();
        this.eStudentType = EStudentType.OTHER;
        this.eRole = ERole.STUDENT;
        this.midTerm = 0.0;
        this.finalTerm = 0.0;
        this.resultTerm = calculateResult();
        this.status = determineStatus();
    }

    public StudentDto(Integer id, String name, String surname, LocalDate birthDate,
                      Double midTerm, Double finalTerm, EStudentType eStudentType, ERole eRole) {
        super(id, name, surname, birthDate);
        this.eStudentType = (eStudentType != null) ? eStudentType : EStudentType.OTHER;
        this.eRole = (eRole != null) ? eRole : ERole.STUDENT;
        this.midTerm = (midTerm != null) ? midTerm : 0.0;
        this.finalTerm = (finalTerm != null) ? finalTerm : 0.0;
        this.resultTerm = calculateResult();
        this.status = determineStatus();
    }

    public StudentDto(Integer id, String name, String surname, LocalDate birthDate, EStudentType eStudentType, ERole eRole) {
        this(id, name, surname, birthDate, 0.0, 0.0, eStudentType, eRole);
    }

    private Double calculateResult() {
        if (midTerm == null || finalTerm == null) {
            logger.warning("⚠\uFE0F Grade calculation error: Midterm or Final contains null value!");
            return 0.0;
        }
        return (midTerm * 0.4) + (finalTerm * 0.6);
    }

    private String determineStatus() {
        return (this.resultTerm >= 50.0) ? "Passed" : "Failed";
    }

    @Override
    public String toString() {
        return "StudentDto{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", birthDate=" + getBirthDate() +
                ", eStudentType=" + eStudentType +
                ", eRole=" + eRole +
                ", midTerm=" + midTerm +
                ", finalTerm=" + finalTerm +
                ", resultTerm=" + resultTerm +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public void displayInfo() {
        logger.info(this.toString());
    }

    // Getter ve Setter Metotları
    public EStudentType getEStudentType() {
        return eStudentType;
    }

    public void setEStudentType(EStudentType eStudentType) {
        this.eStudentType = (eStudentType != null) ? eStudentType : EStudentType.OTHER;
    }

    public ERole getERole() {
        return eRole;
    }

    public void setERole(ERole eRole) {
        this.eRole = (eRole != null) ? eRole : ERole.STUDENT;
    }

    public Double getMidTerm() {
        return midTerm;
    }

    public void setMidTerm(Double midTerm) {
        this.midTerm = (midTerm != null) ? midTerm : 0.0;
        this.resultTerm = calculateResult();
        this.status = determineStatus();
    }

    public Double getFinalTerm() {
        return finalTerm;
    }

    public void setFinalTerm(Double finalTerm) {
        this.finalTerm = (finalTerm != null) ? finalTerm : 0.0;
        this.resultTerm = calculateResult();
        this.status = determineStatus();
    }

    public Double getResultTerm() {
        return resultTerm;
    }

    public String getStatus() {
        return status;
    }
}
