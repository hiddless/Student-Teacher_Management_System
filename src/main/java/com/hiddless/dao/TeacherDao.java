package com.hiddless.dao;

import com.hiddless.dto.ETeacherSubject;
import com.hiddless.dto.TeacherDto;
import com.hiddless.exceptions.TeacherNotFoundException;
import com.hiddless.iofiles.FileHandler;
import com.hiddless.utils.SpecialColours;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TeacherDao implements IDaoGenerics<TeacherDto> {

    /// Field
    private final Scanner scanner = new Scanner(System.in);
    private final List<TeacherDto> teacherList;
    private static final Random random = new Random();
    private int maxId = 0;

    /// Inner class
    private final InnerFileHandler innerClass = new InnerFileHandler();

    /// Constructor Without Parameter
    public TeacherDao() {
        teacherList = new ArrayList<>();
        innerClass.createFileIfNotExists();
        innerClass.readFile();
        updateMaxId();
    }

    static {
        System.out.println(SpecialColours.RED+ " Static: TeacherDao" +SpecialColours.RESET);
    }

    /// Inner class

    private class InnerFileHandler {
        private final FileHandler fileHandler;

        /// Constructor
        private InnerFileHandler() {
            this.fileHandler = new FileHandler();
            fileHandler.setFilePath("teachers.txt");
        }

        private void createFileIfNotExists() {
            fileHandler.createFileIfNotExists();
        }

        private void writeFile() {
            StringBuilder data = new StringBuilder();
            for (TeacherDto teacher : teacherList) {
                data.append(teacherToCsv(teacher)).append("\n");
            }
            fileHandler.writeFile(data.toString());
        }

        public void readFile() {

        }
    }

    //max Id
    private void updateMaxId() {
        maxId = teacherList.stream()
                .mapToInt(TeacherDto::id)
                .max()
                .orElse(0);
    }

    /// csv
    private String teacherToCsv(TeacherDto teacher) {
        return teacher.id() + "," + teacher.name() + "," + teacher.subject() + "," +
                teacher.birthDate() + "," + teacher.subject() + "," +
                teacher.yearsOfExperience() + "," + teacher.isTenured() + "," + teacher.salary();
    }

    private TeacherDto csvToTeacher(String csvLine) {
        try {
            String [] parts = csvLine.split(",");
            if (parts.length != 8) {
                System.err.println("Wrong CSV format: " + csvLine);
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
            LocalDate birthDate= null;
            try {
                if (!parts[3].isBlank()) {
                    birthDate = LocalDate.parse(parts[3] , formatter);
                }
            }catch (DateTimeParseException e) {
                System.err.println("Wrong Date Format: " + parts[3] + "Correct Date format is: dd-mm-yyyy");
                return null;
            }
            return new TeacherDto(
                    Integer.parseInt(parts[0]),
                    parts[1],
                    parts[2],
                    birthDate,
                    ETeacherSubject.valueOf(parts[4]),
                    Integer.parseInt(parts[5]),
                    Boolean.parseBoolean(parts[6]),
                    Double.parseDouble(parts[7])
            );
        }catch (Exception e) {
            System.err.println("Error in CSV: " + e.getMessage());
            return null;
        }
    }

    /// Adding Teacher
    @Override
    public Optional<TeacherDto> create(TeacherDto teacher) {
        teacher = new TeacherDto(
                ++maxId,
                teacher.name(),
                teacher.surname(),
                teacher.birthDate(),
                teacher.subject(),
                teacher.yearsOfExperience(),
                teacher.isTenured(),
                teacher.salary()
        );
        teacherList.add(teacher);
        innerClass.writeFile();
        return Optional.of(teacher);
    }

    /// Teacher List
    @Override
    public List<TeacherDto> list() {
        return new ArrayList<>(teacherList);
    }

    /// Find By name
    @Override
    public Optional<TeacherDto> findByName(String name) {
        return teacherList.stream().filter(t -> t.name().equalsIgnoreCase(name)).findFirst();
    }

    /// Find By ID
    @Override
    public Optional<TeacherDto> findById(int id) {
        return teacherList.stream().filter(t -> t.id() == id).findFirst();
    }

    /// Teacher Update
    @Override
    public Optional<TeacherDto> update(int id, TeacherDto updatedTeacher) {
        for (int i = 0; i < teacherList.size(); i++) {
            if (teacherList.get(i).id() == id) {
                teacherList.set(i, updatedTeacher);
                innerClass.writeFile();
                return Optional.of(updatedTeacher);
            }
        }
        throw new TeacherNotFoundException(SpecialColours.RED + "There is no Teacher to Update" +SpecialColours.RESET);
    }

    /// Delete Teacher
    @Override
    public Optional<TeacherDto> delete(int id) {
        Optional<TeacherDto> teacher = findById(id);
        teacher.ifPresentOrElse(
                teacherList::remove,
                () -> {
                    throw new TeacherNotFoundException("There is no Teacher with this id" + id);
                }
        );
        innerClass.writeFile();
        return teacher;
    }

    /// Enum
    public ETeacherSubject teacherTypeMethod() {
        System.out.println("\n" + SpecialColours.GREEN + "Select Teacher Type. \n1-)History\n2-)Biology\n3-)Chemistry\n4-)Computer Science\n5-)Mathematics \n6-)Other" +SpecialColours.RESET);
        int typeChooise = scanner.nextInt();
        ETeacherSubject switchcaseTeacher = switch (typeChooise) {
            case 1 -> ETeacherSubject.HISTORY;
            case 2 -> ETeacherSubject.BIOLOGY;
            case 3 -> ETeacherSubject.CHEMISTRY;
            case 4 -> ETeacherSubject.COMPUTER_SCIENCE;
            case 5 -> ETeacherSubject.MATHMETICS;
            case 6 -> ETeacherSubject.OTHER;
            default -> ETeacherSubject.OTHER;
        };
        return switchcaseTeacher;
    }

    /// Console
    @Override
    public void chooise() {
        while (true) {
            try {
                System.out.println("\n====== TEACHER MANAGEMENT SYSTEM ======");
                System.out.println("1. Add Teacher");
                System.out.println("1. List Teacher");
                System.out.println("3. Search Teacher");
                System.out.println("4. Update Teacher");
                System.out.println("5. Delete Teacher");
                System.out.println("6. Select Random Teacher");
                System.out.println("7. Sort Teachers by Age");
                System.out.println("8. Exit System");
                System.out.println("\nPlease Select: ");

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
                        System.out.println("Exiting from the System...");
                        return;
                    }
                    default -> System.out.println("Invalid Number! Please Try Again.");
                }
            }catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void addTeacher() {
        int id = ++maxId;

        System.out.println("Name: ");
        String name = scanner.nextLine();

        System.out.println("Surname: ");
        String surname = scanner.nextLine();

        System.out.println("Birth date (dd-mm-yyyy): ");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine(),DateTimeFormatter.ofPattern("dd-mm-yyyy"));

        System.out.println("Profession: ");
        ETeacherSubject subject = teacherTypeMethod();

        System.out.println("Years Of Experience: ");
        int yearsOfExperience = scanner.nextInt();

        System.out.println("is Tenured? (true/false): ");
        boolean isTenured = scanner.nextBoolean();

        System.out.println("Salary: ");
        double salary = scanner.nextDouble();

        TeacherDto teacher = new TeacherDto(id,name,surname,birthDate,subject,yearsOfExperience,isTenured,salary);
        teacherList.add(teacher);
        innerClass.writeFile();

        System.out.println("Teacher added. Id: " + id);
    }

    private void listTeachers() {
        if (teacherList.isEmpty()) {
            System.out.println("Teacher list is empty");
            return;
        }
        System.out.println(SpecialColours.PURPLE + "\n=== Teacher List ===" + SpecialColours.RESET);
        teacherList.forEach(t -> System.out.println(t.fullName() + " - " + t.subject()));
    }

    private void searchTeacher() {
        listTeachers();
        System.out.println("Teacher name: ");
        String name = scanner.nextLine();

        findByName(name).ifPresentOrElse(
                teacher -> System.out.println("Found Teacher: " + teacher.fullName() + " - " + teacher.subject()),
                () -> System.out.println("No Teacher Found.")
        );
    }
    
    private void updateTeacher() {
        listTeachers();
        System.out.println("Teacher id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        try {
            TeacherDto existingTeacher = findById(id).orElseThrow(() -> new TeacherNotFoundException(id + " No Teacher Found."));

            System.out.println("New name (Old Name: " + existingTeacher.name() + "): ");
            String name = scanner.nextLine();
            System.out.println("New surname (Old Surname: " + existingTeacher.surname()+ "): ");
            String surname = scanner.nextLine();
            System.out.println("New Salary (Old Salary: " + existingTeacher.salary() + "): ");
            double salary = scanner.nextDouble();
            
            TeacherDto updatedTeacher = new TeacherDto(
                    existingTeacher.id(),
                    name.isBlank() ? existingTeacher.name() : name,
                    surname.isBlank() ? existingTeacher.surname() : surname,
                    existingTeacher.birthDate(),
                    existingTeacher.subject(),
                    existingTeacher.yearsOfExperience(),
                    existingTeacher.isTenured(),
                    salary > 0 ? salary : existingTeacher.salary()
            );
            
            update(id, updatedTeacher);
            System.out.println("Teacher Succesfully Updated");
        }catch (TeacherNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void deleteTeacher() {
        listTeachers();
        System.out.println("Teacher id: ");
        int id = scanner.nextInt();
        try {
            delete(id);
            System.out.println("Teacher deleted.");
        }catch (TeacherNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void chooseRandomTeacher() {
        if (teacherList.isEmpty()) {
            System.out.println("Teacher List is Empty");
            return;
        }
        TeacherDto teacher = teacherList.get(random.nextInt(teacherList.size()));
        System.out.println("Selected Random teacher: " + teacher.fullName());
    }
    
    private void sortTeachersByAge() {
        teacherList.sort(Comparator.comparing(TeacherDto::birthDate));
        System.out.println("Teachers sorted by Age");
        listTeachers();
    }
}


