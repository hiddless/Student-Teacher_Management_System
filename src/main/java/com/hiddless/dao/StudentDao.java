package com.hiddless.dao;

import com.hiddless.dto.StudentDto;
import com.hiddless.utils.SpecialColours;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentDao implements IDaoGenerics<StudentDto> {

    /// Field
    private ArrayList<StudentDao> studentDtoList ;
    int maxId=0;
    private static final String FILE_NAME = "students.txt";
    private final Scanner scanner = new Scanner(System.in);

    static {

    }

    /// Constructor without Parameter
    public StudentDao(){
        studentDtoList = new ArrayList<>();

        createFileIfNotExists();
        loadStudentsListFromFile();
    }

    private void createFileIfNotExists(){
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                if (file.createNewFile()){
                    System.out.println(SpecialColours.YELLOW + FILE_NAME +"File created" +SpecialColours.RESET);
                }
            }catch (IOException e) {
                System.out.println(SpecialColours.RED + "There is an error in creating!" + SpecialColours.RESET);
                e.printStackTrace();
            }
        }
    }

    private void saveToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (StudentDto student : studentDtoList) {
                bufferedWriter.write(studentToCsv(student) + "\n");
            }
            System.out.println(SpecialColours.GREEN + "Students saved to File" +SpecialColours.RESET);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadStudentsListFromFile() {
        studentDtoList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;
            while ((line = reader.readLine()) != null) {
                StudentDto student = csvToStudent(line);
                if (student != null) {
                    studentDtoList.add(student);
                }
            }

        }catch (IOException e) {
            System.out.println(SpecialColours.RED + "There is an error in reading files." +SpecialColours.RESET);
            e.printStackTrace();
        }
    }


    private String studentToCsv(StudentDto student) {
        return
                student.getId() + "," +
                        student.getName() + "," +
                        student.getSurname() + "," +
                        student.getMidTerm() + "," +
                        student.getFinalTerm() + "," +
                        student.getResultTerm() + "," +
                        student.getStatus() + "," +
                        student.getBirthDate() + "," +
                        student.getEStudentType();
    }
}
