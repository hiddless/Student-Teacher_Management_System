package com.hiddless.Controller;

import com.hiddless.dao.IDaoGenerics;
import com.hiddless.dao.StudentDao;
import com.hiddless.dto.StudentDto;
import com.hiddless.utils.SpecialColours;

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
    public Optional<StudentDto> create(StudentDto studentDto) {
        Optional<StudentDto> createdStudent = studentDao.create(studentDto);

        if (createdStudent == null) {
            System.out.println(SpecialColours.RED + "Failed to create student. Please Try again." + SpecialColours.RESET);
            return Optional.empty();
        }
        return createdStudent;
    }

    /// Find by name
    @Override
    public Optional<StudentDto> findByName() {
        return studentDao.findByName(name);
    }

    @Override
    public Optional<StudentDto> findById(int id) {
        return studentDao.findById(id);
    }

    /// List
    @Override
    public List<StudentDto> list() {
        return studentDao.list();
    }

    /// Update
    @Override
    public Optional<StudentDto> update(int id, StudentDto studentDto) {
        return  studentDao.update(id, studentDto);
    }

    /// Delete
    @Override
    public Optional<StudentDto> delete(int id){
        return studentDao.delete(id);
    }

    /// Chooise
    @Override
    public void chooise() {
        studentDao.chooise();
    }
}
