package com.hiddless.Controller;

import com.hiddless.dao.IDaoGenerics;
import com.hiddless.dao.TeacherDao;
import com.hiddless.dto.TeacherDto;
import com.hiddless.utils.SpecialColours;

import java.util.List;
import java.util.Optional;

public class TeacherController implements IDaoGenerics<TeacherDto> {

    /// Injection
    private final TeacherDao teacherDao;

    /// Constructor without Parameter
    public TeacherController() {
        this.teacherDao = new TeacherDao();
    }

    /// Create
    @Override
    public Optional<TeacherDto> create(TeacherDto teacherDto){
        Optional<TeacherDto> createdTeacher = teacherDao.create(teacherDto);
        if (createdTeacher == null) {
            System.out.println(SpecialColours.RED + "Failed to create a teacher.Please Try again."+ SpecialColours.RESET);
        }
        return createdTeacher;
    }

    /// Find By name
    @Override
    public Optional<TeacherDto> findByName(String name) {
        return teacherDao.findByName(name);
    }

    @Override
    public Optional<TeacherDto> findById(int id) {
        return null;
    }

    /// List
    @Override
    public List<TeacherDto> list() {
        return teacherDao.list();
    }

    /// Update
    @Override
    public Optional<TeacherDto> update(int id, TeacherDto teacherDto) {
        return teacherDao.update(id, teacherDto);
    }

    /// Delete
    @Override
    public Optional<TeacherDto> delete(int id) {
        return teacherDao.delete(id);
    }

    /// Chooise(Switch case)
    @Override
    public void chooise(){
        teacherDao.chooise();
    }
}
