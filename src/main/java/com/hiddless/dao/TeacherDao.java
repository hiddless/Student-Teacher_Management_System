package com.hiddless.dao;

import com.hiddless.dto.ETeacherSubject;
import com.hiddless.dto.TeacherDto;
import com.hiddless.exceptions.TeacherNotFoundException;
import com.hiddless.iofiles.SpecialFileHandler;
import com.hiddless.utils.SpecialColours;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Logger;

public class TeacherDao implements IDaoGenerics<TeacherDto> {

    // Logger
    private static final Logger logger = Logger.getLogger(TeacherDao.class.getName());

    // Field
    private final Scanner scanner = new Scanner(System.in);
    private final List<TeacherDto> teacherDtoList;
    private static final Random random = new Random();
    private int maxId = 0;

    // Inner Class
    private final InnerFileHandler innerClass;

    // static
    static {
        System.out.println(SpecialColours.RED + " Static: TeacherDao" + SpecialColours.RESET);
    }


    public TeacherDao() {
        this.teacherDtoList = new ArrayList<>();
        innerClass = new InnerFileHandler();
    }

    /// /////////////////////////////////////////////////////////////
    // INNER CLASS
    private class InnerFileHandler {
        private final SpecialFileHandler fileHandler;

        // Constructor
        private InnerFileHandler() {
            this.fileHandler = new SpecialFileHandler();
            fileHandler.setFilePath("teachers.txt");
        }

        private void createFileIfNotExists() {
            fileHandler.createFileIfNotExists();
        }

        private void writeFile() {
            StringBuilder data = new StringBuilder();
            for (TeacherDto teacher : teacherDtoList) {
                data.append(teacherToCsv(teacher)).append("\n");
            }
            fileHandler.writeFile(data.toString());
        }

        private void readFile() {
            teacherDtoList.clear();
            fileHandler.readFile();
        }
    }

    private void updateMaxId() {
        maxId = teacherDtoList.stream()
                .mapToInt(TeacherDto::id)
                .max()
                .orElse(0);
    }

    private String teacherToCsv(TeacherDto teacher) {
        return teacher.id() + "," + teacher.name() + "," + teacher.surname() + "," +
                teacher.birthDate() + "," + teacher.subject() + "," +
                teacher.yearsOfExperience() + "," + teacher.isTenured() + "," + teacher.salary();
    }

    // 📌 CSV formatındaki satırı TeacherDto nesnesine çevirme
    private TeacherDto csvToTeacher(String csvLine) {
        try {
            if (csvLine.trim().isEmpty()) {
                System.out.println(SpecialColours.YELLOW + "Blank line skipped!" + SpecialColours.RESET);
                return null;
            }

            String[] parts = csvLine.split(",");

            if (parts.length != 8) {
                System.err.println(SpecialColours.RED + "Wrong CSV format! Expected 8 columns, but " + parts.length + " column found." + SpecialColours.RESET);
                System.err.println("Incorrect line: " + csvLine);
                return null;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yy");
            LocalDate birthDate = null;
            try {
                if (!parts[3].isBlank()) {
                    birthDate = LocalDate.parse(parts[3], formatter);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format: " + parts[3] + " (Expected format: dd-mm-yyyy)");
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

        } catch (Exception e) {
            System.out.println(SpecialColours.RED + "Error loading teachers from CSV: " + e.getMessage() + SpecialColours.RESET);
            return null;
        }
    }

    @Override
    public Optional<TeacherDto> create(TeacherDto teacherDto) {
        if (teacherDto == null || findByName(teacherDto.name()).isPresent()) {
            logger.warning("Invalid or existing teachers cannot be added.");
            return Optional.empty();
        }
        teacherDtoList.add(teacherDto);
        logger.info("New teacher added: " + teacherDto.name());
        innerClass.writeFile();
        return Optional.of(teacherDto);
    }

    @Override
    public List<TeacherDto> list() {
        if (teacherDtoList.isEmpty()) {
            logger.warning("⚠️ Kayıtlı öğretmen bulunamadı!");
            System.out.println(SpecialColours.RED + "Teacher list is empty" + SpecialColours.RESET);
        }
        return new ArrayList<>(teacherDtoList);
    }

    @Override
    public Optional<TeacherDto> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("You entered an invalid name.");
        }
        return teacherDtoList.stream()
                .filter(t -> t.name().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Optional<TeacherDto> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("You entered an invalid ID.");
        }
        return teacherDtoList.stream()
                .filter(t -> t.id().equals(id))
                .findFirst()
                .or(() -> {
                    logger.warning("Teacher not found, ID: " + id);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<TeacherDto> update(int id, TeacherDto teacherDto) {
        if (id <= 0 || teacherDto == null) {
            throw new IllegalArgumentException("Please enter valid teacher information for update.");
        }
        for (int i = 0; i < teacherDtoList.size(); i++) {
            if (teacherDtoList.get(i).id().equals(id)) {
                teacherDtoList.set(i, teacherDto);
                logger.info("Teacher updated: " + teacherDto.name());
                return Optional.of(teacherDto);
            }
        }
        System.out.println(SpecialColours.RED + "No teacher found to update, ID: " + id + SpecialColours.RESET);
        return null;
    }

    @Override
    public Optional<TeacherDto> delete(int id) {
        Optional<TeacherDto> teacherToDelete = findById(id);
        if (teacherToDelete.isPresent()) {
            teacherDtoList.remove(teacherToDelete.get());
            logger.info("Teacher deleted, ID: " + id);
            return teacherToDelete;
        } else {
            logger.warning(" No teacher found to delete, ID: " + id);
            return Optional.empty();
        }
    }

    public ETeacherSubject teacherTypeMethod() {
        System.out.println("\n" + SpecialColours.GREEN + "Select Teacher Profession\n1-)History\n2-)Biology\n3-)Chemistry\n4-)Computer Science\n5-)Other" + SpecialColours.RESET);
        int typeChooise = scanner.nextInt();
        ETeacherSubject swichcaseTeacher = switch (typeChooise) {
            case 1 -> ETeacherSubject.HISTORY;
            case 2 -> ETeacherSubject.BIOLOGY;
            case 3 -> ETeacherSubject.CHEMISTRY;
            case 4 -> ETeacherSubject.COMPUTER_SCIENCE;
            case 5 -> ETeacherSubject.MATHEMETICS;
            default -> ETeacherSubject.OTHER;
        };
        return swichcaseTeacher;
    }

    @Override
    public void choose() {
        logger.info("Redirected to the teacher operations screen");
        while (true) {
            try {
                System.out.println("\n===== TEACHER MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Teacher");
                System.out.println("2. List Teachers");
                System.out.println("3. Search Teacher");
                System.out.println("4. Update Teacher");
                System.out.println("5. Delete Teacher");
                System.out.println("6. Select Random Teacher");
                System.out.println("7. Sort Teachers by Age");
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
                        System.out.println("Signing out...");
                        return;
                    }
                    default -> System.out.println("Invalid selection! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private void addTeacher() {
        int id = ++maxId;

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Surname: ");
        String surname = scanner.nextLine();

        System.out.print("Birth Date (dd-mm-yyyy): ");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-mm-yyyy"));

        System.out.print("Profession: ");
        ETeacherSubject subject = teacherTypeMethod();

        System.out.print("Years Of Experience: ");
        int yearsOfExperience = scanner.nextInt();

        System.out.print("Is Tenured? (true/false): ");
        boolean isTenured = scanner.nextBoolean();

        System.out.print("Salary: ");
        double salary = scanner.nextDouble();

        TeacherDto teacher = new TeacherDto(id, name, surname, birthDate, subject, yearsOfExperience, isTenured, salary);
        teacherDtoList.add(teacher);
        innerClass.writeFile();

        System.out.println("Teacher added successfully. Assigned ID: " + id);
    }

    private void listTeachers() {
        if (teacherDtoList.isEmpty()) {
            System.out.println(SpecialColours.YELLOW + "Teacher list is empty, loading from file..." + SpecialColours.RESET);

            List<String> fileLines = innerClass.fileHandler.readFile();
            for (String line : fileLines) {
                TeacherDto teacher = csvToTeacher(line);
                if (teacher != null) {
                    teacherDtoList.add(teacher);
                } else {
                    System.out.println(SpecialColours.RED + " The line with errors is skipped: " + line + SpecialColours.RESET);
                }
            }
            if (teacherDtoList.isEmpty()) {
                System.out.println(SpecialColours.RED + "No teacher data found in the file!" + SpecialColours.RESET);
            } else {
                System.out.println(SpecialColours.GREEN + "✅ " + teacherDtoList.size() + " Teacher successfully uploaded!" + SpecialColours.RESET);
            }
        }

        if (!teacherDtoList.isEmpty()) {
            System.out.println("\n=== Teacher List ===");
            for (TeacherDto teacher : teacherDtoList) {
                System.out.println(teacher.fullName() + " - " + teacher.subject());
            }
        }
    }


    private void searchTeacher() {
        listTeachers();
        System.out.print("Name of teacher to be searched: ");
        String name = scanner.nextLine();

        findByName(name).ifPresentOrElse(
                teacher -> System.out.println("Teacher Found: " + teacher.fullName() + " - " + teacher.subject()),
                () -> System.out.println("Teacher not found.")
        );
    }

    private void updateTeacher() {
        listTeachers();
        System.out.print("ID of the teacher to be updated: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            TeacherDto existingTeacher = findById(id).orElseThrow(() -> new TeacherNotFoundException( " No teacher with ID found." + id));

            System.out.print("New Name (Current: " + existingTeacher.name() + "): ");
            String name = scanner.nextLine();
            System.out.print("New Surname (Current: " + existingTeacher.surname() + "): ");
            String surname = scanner.nextLine();
            System.out.print("New Salary (Current: " + existingTeacher.salary() + "): ");
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
            System.out.println("The teacher has been updated successfully.");
        } catch (TeacherNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteTeacher() {
        // Öncelikle Listele
        listTeachers();
        System.out.print("ID of the teacher to be deleted: ");
        int id = scanner.nextInt();
        try {
            delete(id);
            System.out.println("The teacher was deleted successfully.");
        } catch (TeacherNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void chooseRandomTeacher() {
        if (teacherDtoList.isEmpty()) {
            System.out.println("There are no registered teachers.");
            return;
        }
        TeacherDto teacher = teacherDtoList.get(random.nextInt(teacherDtoList.size()));
        System.out.println("Random Teacher Selected: " + teacher.fullName());
    }

    private void sortTeachersByAge() {
        teacherDtoList.sort(Comparator.comparing(TeacherDto::birthDate));
        System.out.println("Teachers are sorted by age.");
        listTeachers();
    }


    public static void main(String[] args) {

    }

}


