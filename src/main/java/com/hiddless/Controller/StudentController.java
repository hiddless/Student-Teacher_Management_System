package com.hiddless.Controller;

import com.hiddless.dao.IDaoGenerics;
import com.hiddless.dao.StudentDao;
import com.hiddless.dto.StudentDto;
import com.hiddless.log.LogExecutionTime;
import com.hiddless.utils.SpecialColours;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentController implements IDaoGenerics<StudentDto> {

    /// Injection
    private final StudentDao studentDao;

    /// Constructor without Parameter

    public StudentController() {
        this.studentDao= new StudentDao();
    }

    /// Create
    @Override
    @LogExecutionTime
    public Optional<StudentDto> create(StudentDto studentDto) {
        if (studentDto == null || studentDao.findById(studentDto.getId()).isPresent()) {
            System.out.println(SpecialColours.RED + "Cannot be added due to invalid or existing student. " + SpecialColours.RESET);
            return Optional.empty();
        }
        Optional<StudentDto> createdStudent = studentDao.create(studentDto);
        createdStudent.ifPresentOrElse(
                temp -> System.out.println(SpecialColours.GREEN + "Student added." +SpecialColours.RESET),
                () -> System.out.println(SpecialColours.RED + " Cannot add the Student" + SpecialColours.RESET));
        return createdStudent;
    }

    /// Find by name
    @Override
    @LogExecutionTime
    public Optional<StudentDto> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("You have entered an invalid name.");
        }
        return studentDao.findByName(name.trim());

    }

    /// Find by id
    @Override
    @LogExecutionTime
    public Optional<StudentDto> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("You have entered an invalid id.");
        }
        return studentDao.findById(id);
    }

    /// List
    @Override
    @LogExecutionTime
    public List<StudentDto> list() {
        List<StudentDto> studentDtoList = Optional.of(studentDao.list()).orElse(Collections.emptyList());
        if (studentDtoList.isEmpty()) {
            System.out.println(SpecialColours.RED + "Student list is empty."+ SpecialColours.RESET);
        }
        return studentDtoList;
    }

    /// Update
    @Override
    @LogExecutionTime
    public Optional<StudentDto> update(int id, StudentDto studentDto) {
        if (id <= 0 || studentDto == null) {
            throw new IllegalArgumentException("Enter a valid Student Name.");
        }
        return  studentDao.update(id, studentDto);
    }

    /// Delete
    @Override
    @LogExecutionTime
    public Optional<StudentDto> delete(int id){
        if (id <= 0) {
            throw new IllegalArgumentException("Enter a valid Student ID");
        }
        return studentDao.delete(id);
    }

    /// Chooise
    @Override
    @LogExecutionTime
    public void chooise() {
        studentDao.chooise();
    }
}
