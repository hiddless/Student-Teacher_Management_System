package com.hiddless.Controller;

import com.hiddless.dao.IDaoGenerics;
import com.hiddless.dao.TeacherDao;
import com.hiddless.dto.ETeacherSubject;
import com.hiddless.dto.TeacherDto;
import com.hiddless.log.LogExecutionTime;


import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class TeacherController implements IDaoGenerics<TeacherDto> {

    /// Injection
    private static final Logger logger = Logger.getLogger(TeacherController.class.getName());
    private final TeacherDao teacherDao;

    public TeacherController() {
        this.teacherDao = new TeacherDao();
    }

    @Override
    @LogExecutionTime
    public Optional<TeacherDto> create(TeacherDto teacherDto) {
        if (teacherDto == null || teacherDao.findByName(teacherDto.name()).isPresent()) {
            logger.warning("Invalid or existing teachers cannot be added");
            return Optional.empty();
        }
        return teacherDao.create(teacherDto);
    }

    @Override
    @LogExecutionTime
    public Optional<TeacherDto> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("You entered an invalid name.");
        }
        return teacherDao.findByName(name).or(() -> {
            logger.warning("No teacher found");
            return Optional.empty();
        });
    }

    @Override
    @LogExecutionTime
    public Optional<TeacherDto> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("You have entered an invalid ID.");
        }
        return teacherDao.findById(id).or(() -> {
            logger.warning("No teacher found");
            return Optional.empty();
        });
    }

    @Override
    @LogExecutionTime
    public List<TeacherDto> list() {
        List<TeacherDto> teacherDtoList = Optional.ofNullable(teacherDao.list()).orElse(Collections.emptyList());
        if (teacherDtoList.isEmpty()) {
            logger.info("There are no registered teachers yet.");
        }
        return teacherDtoList;
    }

    @Override
    @LogExecutionTime
    public Optional<TeacherDto> update(int id, TeacherDto teacherDto) {
        if (id <= 0 || teacherDto == null) {
            throw new IllegalArgumentException("Please enter a valid teacher information for update");
        }
        return teacherDao.update(id, teacherDto);
    }

    @Override
    @LogExecutionTime
    public Optional<TeacherDto> delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Please enter a valid teacher ID to delete");
        }
        return teacherDao.delete(id).or(() -> {
            logger.warning("Deletion failed: Teacher not found");
            return Optional.empty();
        });
    }


    //@LogExecutionTime
    @Override
    public void choose() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n===== TEACHER MANEGEMENT SYSTEM ===== * By Hiddless");
                System.out.println("1. Add Teacher");
                System.out.println("2. List Teacher");
                System.out.println("3. Search Teacher");
                System.out.println("4. Update Teacher");
                System.out.println("5. Delete Teacher");
                System.out.println("6. Pick Random Teacher");
                System.out.println("7. Sort Teachers By Age");
                System.out.println("8. Exit");
                System.out.print("\nMake your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addTeacher();
                    case 2 -> listTeachers();
                    case 3 -> searchTeacher();
                    case 4 -> updateTeacher();
                    case 5 -> deleteTeacher();
                    case 6 -> chooseRandomTeacher();
                    case 7 -> sortTeachersByAge();
                    case 8 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid selection! Please try again.");
                }
            } catch (Exception e) {
                System.out.println(" An unexpected error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    public int generateNewId() {
        return teacherDao.list().isEmpty() ? 1 :
                teacherDao.list().stream().mapToInt(TeacherDto::id).max().orElse(0) + 1;
    }


    public void addTeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Teacher Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Teacher Surname: ");
        String surname = scanner.nextLine().trim();

        System.out.print("Birth Date: ");
        int birthYear = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Select your Profession (MATHEMATICS, CHEMISTRY, BIOLOGY, HISTORY, COMPUTER_SCIENCE, OTHER): ");
        String subjectStr = scanner.nextLine().trim().toUpperCase();

        ETeacherSubject subject;
        try {
            subject = ETeacherSubject.valueOf(subjectStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid progession! OTHER is assigned by default");
            subject = ETeacherSubject.OTHER;
        }

        TeacherDto newTeacher = new TeacherDto(generateNewId(), name, surname, LocalDate.of(birthYear, 1, 1), subject, 0, false, 0.0);
        create(newTeacher);
        System.out.println("New teacher added: " + newTeacher.name());
    }

    public void listTeachers() {
        List<TeacherDto> teachers = list();
        if (teachers.isEmpty()) {
            System.out.println(" Teacher not found.");
        } else {
            teachers.forEach(System.out::println);
        }
    }

    public void searchTeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Teacher name to search: ");
        String name = scanner.nextLine().trim();

        Optional<TeacherDto> teacher = findByName(name);
        teacher.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Teacher not found.")
        );
    }

    public void updateTeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Teacher ID to be updated: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<TeacherDto> existingTeacher = findById(id);
        if (existingTeacher.isPresent()) {
            System.out.print("New Name: ");
            String newName = scanner.nextLine().trim();

            System.out.print("New Surname: ");
            String newSurname = scanner.nextLine().trim();

            System.out.print("New Profession: ");
            String newSubjectStr = scanner.nextLine().trim().toUpperCase();
            ETeacherSubject newSubject = ETeacherSubject.valueOf(newSubjectStr);

            TeacherDto updatedTeacher = new TeacherDto(id, newName, newSurname, existingTeacher.get().birthDate(), newSubject, existingTeacher.get().yearsOfExperience(), existingTeacher.get().isTenured(), existingTeacher.get().salary());
            update(id, updatedTeacher);
            System.out.println("Teacher updated: " + updatedTeacher.name());
        } else {
            System.out.println("No Teacher Found.");
        }
    }
    public void deleteTeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Teacher ID to be deleted: ");
        int id = scanner.nextInt();

        Optional<TeacherDto> deletedTeacher = delete(id);
        deletedTeacher.ifPresentOrElse(
                teacher -> System.out.println("Teacher Deleted: " + teacher.name()),
                () -> System.out.println("Teacher not found")
        );
    }
    public void chooseRandomTeacher() {
        List<TeacherDto> teachers = list();
        if (teachers.isEmpty()) {
            System.out.println("Teacher not found");
            return;
        }
        Random random = new Random();
        TeacherDto randomTeacher = teachers.get(random.nextInt(teachers.size()));
        System.out.println("Selected Teacher: " + randomTeacher.name());
    }

    public void sortTeachersByAge() {
        List<TeacherDto> teachers = list();
        if (teachers.isEmpty()) {
            System.out.println("Teacher not found");
            return;
        }
        teachers.sort(Comparator.comparing(TeacherDto::birthDate));
        teachers.forEach(System.out::println);
    }

}