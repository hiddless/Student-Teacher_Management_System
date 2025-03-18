package com.hiddless.Controller;

import com.hiddless.dao.IDaoGenerics;
import com.hiddless.dao.TeacherDao;
import com.hiddless.dto.ETeacherSubject;
import com.hiddless.dto.TeacherDto;
import com.hiddless.log.LogExecutionTime;
import com.hiddless.utils.SpecialColours;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class TeacherController implements IDaoGenerics<TeacherDto> {

    /// Injection
    private static final Logger logger= Logger.getLogger(TeacherController.class.getName());
    private final TeacherDao teacherDao;

    /// Constructor without Parameter
    public TeacherController() {
        this.teacherDao = new TeacherDao();
    }

    /// Create
    @Override
    @LogExecutionTime
    public Optional<TeacherDto> create(TeacherDto teacherDto){
        if (teacherDto == null || teacherDao.findById(teacherDto.id()).isPresent()) {
            logger.warning(SpecialColours.RED + "Invalid Teacher Id" + SpecialColours.RESET);
            return Optional.empty();
        }
        return teacherDao.create(teacherDto);
    }

    /// Find By name
    @Override
    @LogExecutionTime
    public Optional<TeacherDto> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        return teacherDao.findByName(name).or(() -> {
            logger.warning("There is no Teacher");
            return Optional.empty();
        });
    }

    /// Find by id
    @Override
    @LogExecutionTime
    public Optional<TeacherDto> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Id");
        }
        return teacherDao.findById(id).or(() -> {
            logger.warning("There is no Teacher");
            return Optional.empty();
        });
    }

    /// List
    @Override
    @LogExecutionTime
    public List<TeacherDto> list() {
        List<TeacherDto> teacherDtoList = Optional.of(teacherDao.list()).orElse(Collections.emptyList());
        if (teacherDtoList.isEmpty()) {
            logger.info(SpecialColours.RED + "There is no Teacher in list" + SpecialColours.RESET);
        }
        return teacherDtoList;
    }

    /// Update
    @Override
    @LogExecutionTime
    public Optional<TeacherDto> update(int id, TeacherDto teacherDto) {
        if (id <= 0 || teacherDto == null) {
            throw new IllegalArgumentException("Please write an valid Teacher");
        }
        return teacherDao.update(id, teacherDto);
    }

    /// Delete
    @Override
    @LogExecutionTime
    public Optional<TeacherDto> delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Please write an valid Teacher ID");
        }
        return teacherDao.delete(id).or(() -> {
            logger.warning("There is no Teacher");
            return Optional.empty();
        });
    }

    /// Chooise(Switch case)
    @Override
    @LogExecutionTime
    public void choose(){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n===== Teacher Management System =====");
                System.out.println("1. Add Teacher");
                System.out.println("2. List Teacher");
                System.out.println("3. Search Teacher");
                System.out.println("4. Update Teacher");
                System.out.println("5. Delete Teacher");
                System.out.println("6. Select Random Teacher");
                System.out.println("7. Sort Teachers By Age");
                System.out.println("8. Exit");
                System.out.println("\nPlease Select: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addTeacher();
                    case 2 -> listTeacher();
                    case 3 -> searchTeacher();
                    case 4 -> updateTeacher();
                    case 5 -> deleteTeacher();
                    case 6 -> chooseRandomTeacher();
                    case 7 -> sortTeachersByAge();
                    case 8 -> {
                        System.out.println("Exiting from the System...");
                        return;
                    }
                    default -> System.out.println("Wrong choice! Please try again.");
                }
            }catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
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
        System.out.println("Teacher name: ");
        String name = scanner.nextLine().trim();

        System.out.println("Teacher surname: ");
        String surname = scanner.nextLine().trim();

        System.out.println("Birth date: ");
        int birthYear = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Your Profession (MATHMETICS, CHEMISTRY, BIOLOGY, HISTORY, COMPUTER_SCIENCE, OTHER): ");
        String subjectStr = scanner.nextLine().trim().toUpperCase();

        ETeacherSubject subject;
        try {
            subject = ETeacherSubject.valueOf(subjectStr);
        }catch (IllegalAccessException e) {
            System.out.println("Wrong type of Choice. System will write as OTHER.");
            subject = ETeacherSubject.OTHER;
        }

        TeacherDto newTeacher = new TeacherDto(generateNewId(), name, surname, LocalDate.of(birthYear, 1, 1), subject,0 , false,0.0);
        create(newTeacher);
        System.out.println("Teacher created: "+ newTeacher.name());
    }

    public void listTeacher() {
        List<TeacherDto> teachers = list();
        if (teachers.isEmpty()) {
            System.out.println("List is Empty");
        }else {
            teachers.forEach(System.out::println);
        }
    }

    public void searchTeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Name of the Teacher: ");
        String name = scanner.nextLine().trim();

        Optional<TeacherDto> teacher = findByName(name);
        teacher.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Teacher is not Found.")
        );
    }

    public void updateTeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Teacher id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<TeacherDto> existingTeacher = findById(id);
        if (existingTeacher.isPresent()) {
            System.out.println("New name: ");
            String newName= scanner.nextLine().trim();

            System.out.println("New surname: ");
            String newSurname= scanner.nextLine().trim();

            System.out.println("New Profession: ");
            String newSubjectStr = scanner.nextLine().trim().toUpperCase();
            ETeacherSubject newSubject = ETeacherSubject.valueOf(newSubjectStr);

            TeacherDto updatedTeacher = new TeacherDto(id, newName, newSurname, existingTeacher.get().birthDate(), newSubject, existingTeacher.get().yearsOfExperience(), existingTeacher.isTenured(), existingTeacher.get().salary());
            update(id, updatedTeacher);
            System.out.println("Teacher updated: " + updatedTeacher.name());
        }else {
            System.out.println("There is no Teacher");
        }
    }

    public void deleteTeacher() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Teacher id: ");
        int id = scanner.nextInt();

        Optional<TeacherDto> deletedTeacher = delete(id);
        deletedTeacher.ifPresentOrElse(
                teacher -> System.out.println("Teacher deleted: " + teacher.name(),
                        () -> System.out.println("Teacher not found."))
        );
    }

    public void chooseRandomTeacher(){
        List<TeacherDto> teachers = list();
        if (teachers.isEmpty()) {
            System.out.println("List is empty");
            return;
        }
        Random random = new Random();
        TeacherDto randomTeacher = teachers.get(random.nextInt(teachers.size()));
        System.out.println("Random Teacher name: " + randomTeacher.name());

    }

    public void sortTeachersByAge() {
        List<TeacherDto> teachers = list();
        if (teachers.isEmpty()){
            System.out.println("List is Empty");
            return;
        }
        teachers.sort(Comparator.comparing(TeacherDto::birthDate));
        teachers.forEach(System.out::println);
    }
}
