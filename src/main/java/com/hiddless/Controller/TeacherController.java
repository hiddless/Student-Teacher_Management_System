package com.hiddless.Controller;

import com.hiddless.dao.IDaoGenerics;
import com.hiddless.dao.TeacherDao;
import com.hiddless.dto.TeacherDto;
import com.hiddless.log.LogExecutionTime;
import com.hiddless.utils.SpecialColours;

import java.util.Collections;
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
        if (teacherDto == null || teacherDao.findById(teacherDto.id()).isPresent()) {
            System.out.println(SpecialColours.RED + "Invalid Teacher Id" + SpecialColours.RESET);
            return Optional.empty();
        }
        return teacherDao.create(teacherDto);
    }

    /// Find By name
    @Override
    public Optional<TeacherDto> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        return teacherDao.findByName(name);
    }

    /// Find by id
    @Override
    public Optional<TeacherDto> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Id");
        }
        return teacherDao.findById(id);
    }

    /// List
    @Override
    @LogExecutionTime
    public List<TeacherDto> list() {
        List<TeacherDto> teacherDtoList = Optional.of(teacherDao.list()).orElse(Collections.emptyList());
        if (teacherDtoList.isEmpty()) {
            System.out.println(SpecialColours.RED + "There is no Teacher in list" + SpecialColours.RESET);
        }
        return teacherDao.list();
    }

    /// Update
    @Override
    public Optional<TeacherDto> update(int id, TeacherDto teacherDto) {
        if (id <= 0 || teacherDto == null) {
            throw new IllegalArgumentException("Please write an valid Teacher");
        }
        return teacherDao.update(id, teacherDto);
    }

    /// Delete
    @Override
    public Optional<TeacherDto> delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Please write an valid Teacher ID");
        }
        return teacherDao.delete(id);
    }

    /// Chooise(Switch case)
    @Override
    @LogExecutionTime
    public void chooise(){
        teacherDao.chooise();
    }

    public static void main(String[] args) {

    }
}
