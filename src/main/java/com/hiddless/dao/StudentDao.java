package com.hiddless.dao;

import com.hiddless.dto.ERole;
import com.hiddless.dto.EStudentType;
import com.hiddless.dto.StudentDto;
import com.hiddless.exceptions.StudentNotFoundException;
import com.hiddless.iofiles.SpecialFileHandler;
import com.hiddless.utils.SpecialColours;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

public class StudentDao implements IDaoGenerics<StudentDto> {

    // Logger
    private static final Logger logger = Logger.getLogger(StudentDao.class.getName());

    // Field
    private final List<StudentDto> studentDtoList;

    // **📌 Scanner Nesnesini En Üste Tanımladık**
    private final Scanner scanner = new Scanner(System.in);

    // SpecialFileHandler
    private SpecialFileHandler fileHandler;

    // File dosyasına eklenen en büyük ID alıp yeni eklenecek file için 1 artır
    int maxId=0;

    ///////////////////////////////////////////////////////////////////////
    // static
    static {
        System.out.println(SpecialColours.RED+" Static: StudentDao"+ SpecialColours.RESET);
    }

    public StudentDao() {
        this.fileHandler = new SpecialFileHandler();
        this.fileHandler.setFilePath("students.txt");

        studentDtoList = new ArrayList<>();
        this.fileHandler.createFileIfNotExists();

        List<String> fileLines = this.fileHandler.readFile();
        for (String line : fileLines) {
            StudentDto student = csvToStudent(line);
            if (student != null) {
                studentDtoList.add(student);
            } else {
                System.out.println("Incorrect line skipped: " + line);
            }
        }

        System.out.println("✅ " + studentDtoList.size() + " student successfully loaded from file!");
    }

    private String studentToCsv(StudentDto student) {
        return student.getId() + "," +
                student.getName() + "," +
                student.getSurname() + "," +
                student.getMidTerm() + "," +
                student.getFinalTerm() + "," +
                student.getResultTerm() + "," +
                student.getStatus() + "," +
                student.getBirthDate() + "," +
                student.getEStudentType() + "," +
                student.getERole();
    }

    private StudentDto csvToStudent(String csvLine) {
        try {
            String[] parts = csvLine.split(",");

            if (parts.length < 10) {
                System.out.println(SpecialColours.RED + "Row skipped due to missing data: " + csvLine + SpecialColours.RESET);
                return null;
            }

            Integer id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String surname = parts[2];
            Double midTerm = Double.parseDouble(parts[3]);
            Double finalTerm = Double.parseDouble(parts[4]);
            LocalDate birthDate = LocalDate.parse(parts[7]);


            EStudentType studentType;
            try {
                studentType = EStudentType.valueOf(parts[8]);
            } catch (IllegalArgumentException e) {
                System.out.println(SpecialColours.RED + "Wrong Type of Student: " + parts[8] + " Skipping to default" + SpecialColours.RESET);
                studentType = EStudentType.OTHER;
            }

            ERole role;
            try {
                role = ERole.valueOf(parts[9]);
            } catch (IllegalArgumentException e) {
                System.out.println(SpecialColours.RED + "Wrong type of role: " + parts[9] + "Skipping to default " + SpecialColours.RESET);
                role = ERole.STUDENT;
            }

            return new StudentDto(id, name, surname, birthDate, midTerm, finalTerm, studentType, role);

        } catch (Exception e) {
            System.out.println(SpecialColours.RED + "Error loading students from CSV: " + e.getMessage() + SpecialColours.RESET);
            return null;
        }
    }

    @Override
    @Deprecated
    public Optional<StudentDto> create(StudentDto studentDto) {
        if (studentDto == null || findByName(studentDto.getName()).isPresent()) {
            logger.warning("Invalid or existing students cannot be added.");
            return Optional.empty();
        }
        try {
            validateStudent(studentDto);
            maxId = studentDtoList
                    .stream()
                    .mapToInt(StudentDto::getId)
                    .max()
                    .orElse(0);

            studentDto.setId(maxId+1);
            studentDtoList.add(studentDto);
            this.fileHandler.writeFile(studentToCsv(studentDto));

            System.out.println(studentDto+ SpecialColours.GREEN + "Student added successfully!" + SpecialColours.RESET);
            logger.info("Student added successfully!: " + studentDto.getName());
            return Optional.of(studentDto);

        } catch (IllegalArgumentException e) {
            System.out.println(SpecialColours.RED + "Error: " + e.getMessage() + SpecialColours.RESET);
        }
        return Optional.of(studentDto);
    }

    private void validateStudent(StudentDto studentDto) {
        if (studentDto == null) {
            throw new IllegalArgumentException("Öğrenci nesnesi boş olamaz.");
        }

        if (studentDto.getName() == null || studentDto.getName().trim().isEmpty() ||
                !studentDto.getName().matches("^[a-zA-ZığüşöçİĞÜŞÖÇ]+$")) {
            throw new IllegalArgumentException("The name must contain only letters and cannot be blank.");
        }

        if (studentDto.getSurname() == null || studentDto.getSurname().trim().isEmpty() ||
                !studentDto.getSurname().matches("^[a-zA-ZığüşöçİĞÜŞÖÇ]+$")) {
            throw new IllegalArgumentException("The surname must contain only letters and cannot be blank..");
        }

        if (studentDto.getBirthDate() == null || studentDto.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be greater than today.");
        }

        if (studentDto.getMidTerm() == null || studentDto.getMidTerm() < 0 || studentDto.getMidTerm() > 100) {
            throw new IllegalArgumentException("The midterm score must be between 0 and 100.");
        }

        if (studentDto.getFinalTerm() == null || studentDto.getFinalTerm() < 0 || studentDto.getFinalTerm() > 100) {
            throw new IllegalArgumentException("The final grade must be between 0 and 100.");
        }

        if (studentDto.getEStudentType() == null) {
            throw new IllegalArgumentException("Student type cannot be blank.");
        }

        if (studentDto.getERole() == null) {
            throw new IllegalArgumentException("Student role cannot be empty.");
        }
    }

    @Override
    @SuppressWarnings("unchecked") // Derleyici uyarılarını bastırmak için kullanılır.
    public List<StudentDto> list() {
        if (studentDtoList.isEmpty()) {
            System.out.println(SpecialColours.YELLOW + "Student list is currently empty, loading from file..." + SpecialColours.RESET);

            List<String> fileLines = this.fileHandler.readFile();
            for (String line : fileLines) {
                StudentDto student = csvToStudent(line);
                if (student != null) {
                    studentDtoList.add(student);
                }
            }

            if (studentDtoList.isEmpty()) {
                System.out.println(SpecialColours.RED + "No student data found in the file!" + SpecialColours.RESET);
            } else {
                System.out.println(SpecialColours.GREEN + "✅ " + studentDtoList.size() + " Student successfully uploaded!" + SpecialColours.RESET);
            }
        }

        studentDtoList.forEach(System.out::println);
        return new ArrayList<>(studentDtoList);
    }

    @Override
    public Optional<StudentDto> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println(SpecialColours.RED+ "You entered an invalid name.."+SpecialColours.RESET);
        }
        return studentDtoList.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }
    @Override
    public Optional<StudentDto> findById(int id) {
        if (id <= 0) {
            System.out.println(SpecialColours.RED+ " You entered an invalid ID"+SpecialColours.RESET);
        }
        return studentDtoList.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .or(() -> {
                    logger.warning("Student not found, ID: " + id);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<StudentDto> update(int id, StudentDto studentDto) {
        if (id <= 0 || studentDto == null) {
            System.out.println(SpecialColours.RED+ "Please enter a valid student information for update."+SpecialColours.RESET);
        }
        try{
            for (int temp = 0; temp < studentDtoList.size(); temp++) {
                if (studentDtoList.get(temp).getId() == id) {
                    studentDtoList.set(temp, studentDto);
                    logger.info("Student updated: " + studentDto.getName());
                    System.out.println(SpecialColours.BLUE + temp + " Öğrenci Bilgileri Güncellendi" + SpecialColours.RESET);
                    this.fileHandler.writeFile(studentToCsv(studentDto));
                    return Optional.of(studentDto);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new StudentNotFoundException("Student not found.");
        }

        System.out.println(SpecialColours.RED+ "Please enter a valid student information for update."+SpecialColours.RESET);
        return Optional.empty();
    }
    @Override
    public Optional<StudentDto> delete(int id) {
        Optional<StudentDto> studentToDelete = findById(id);
        if (studentToDelete.isPresent()) {
            studentDtoList.remove(studentToDelete.get());
            logger.info("Student deleted, ID: " + id);
            System.out.println(SpecialColours.BLUE + "Student Deleted" + SpecialColours.RESET);
            // Silinen Öğrenciyi dosyaya kaydet
            this.fileHandler.writeFile(studentToCsv(studentToDelete.get()));
            return studentToDelete;
        } else {
            logger.warning("No student to be deleted found, ID: " + id);
            return Optional.empty();
        }
    }

    public EStudentType studentTypeMethod() {
        System.out.println("\n"+SpecialColours.GREEN+"Select the student type.\n1-)Undergraduate\n2-)Master's\n3-)Doctorate"+SpecialColours.RESET);
        int typeChooise = scanner.nextInt();
        EStudentType swichCaseStudent = switch (typeChooise) {
            case 1 -> EStudentType.UNDERGRADUATE;
            case 2 -> EStudentType.GRADUATE;
            case 3 -> EStudentType.PHD;
            default -> EStudentType.OTHER;
        };
        return swichCaseStudent;
    }

    @Override
    public void choose() {
        while (true) {
            try {
                System.out.println("\n"+SpecialColours.BLUE+"===== STUDENT MANAGEMENT SYSTEM====="+SpecialColours.RESET);
                System.out.println(SpecialColours.YELLOW+"1. Add Student");
                System.out.println("2. List Student");
                System.out.println("3. Search Studet");
                System.out.println("4. Update Student");
                System.out.println("5. Delete Student");
                System.out.println("6. Total Number of Students");
                System.out.println("7. Randomly Select Student");
                System.out.println("8. Calculate Student Grade Point Average");
                System.out.println("9. Highest & Lowest Scoring Student");
                System.out.println("10.Sort Students by Date of Birth");
                System.out.println("11.Show Student Pass/Fail Status");
                System.out.println("12. Exit"+SpecialColours.RESET);
                System.out.print("\n"+SpecialColours.PURPLE+"Make your choice: "+SpecialColours.RESET);

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> chooiseStudentAdd();

                    case 2 -> chooiseStudentList();

                    case 3 -> chooiseStudenSearch();

                    case 4 -> chooiseStudenUpdate();

                    case 5 -> chooiseStudenDelete();

                    case 6 -> chooiseSumCounter();

                    case 7 -> chooiseRandomStudent();

                    case 8 -> chooiseStudentNoteAverage();

                    case 9 -> chooiseStudentNoteMinAndMax();

                    case 10 -> chooiseStudentBirthdaySortedDate();

                    case 11 -> listStudentsWithStatus();

                    case 12 -> chooiseExit();

                    default -> System.out.println("Invalid selection! Please try again.");
                }
            } catch (Exception e) {
                System.out.println(SpecialColours.RED + "An unexpected error occurred: " + e.getMessage() + SpecialColours.RESET);
                scanner.nextLine();
            }
        }
    }

    public void chooiseStudentAdd() {
        while (true) {
            try {
                String name;
                while (true) {
                    System.out.print("Öğrenci Adı: ");
                    name = scanner.nextLine().trim();
                    if (name.matches("^[a-zA-ZığüşöçİĞÜŞÖÇ]+$")) break;
                    System.out.println(SpecialColours.RED + "Invalid name! Enter letters only." + SpecialColours.RESET);
                }

                String surname;
                while (true) {
                    System.out.print("Öğrenci Soyadı: ");
                    surname = scanner.nextLine().trim();
                    if (surname.matches("^[a-zA-ZığüşöçİĞÜŞÖÇ]+$")) break;
                    System.out.println(SpecialColours.RED + "Invalid surname! Please enter only letters." + SpecialColours.RESET);
                }

                LocalDate birthDate;
                while (true) {
                    System.out.print("Date of Birth (YYYY-MM-DD): ");
                    String input = scanner.nextLine().trim();
                    try {
                        birthDate = LocalDate.parse(input);
                        if (!birthDate.isAfter(LocalDate.now())) break;
                        System.out.println(SpecialColours.RED + "Invalid date of birth! Cannot be in the future." + SpecialColours.RESET);
                    } catch (Exception e) {
                        System.out.println(SpecialColours.RED + "Invalid format! Please enter as YYYY-MM-DD" + SpecialColours.RESET);
                    }
                }

                double midTerm;
                while (true) {
                    System.out.print("Vize Notu (0-100): ");
                    String input = scanner.nextLine().trim();
                    try {
                        midTerm = Double.parseDouble(input);
                        if (midTerm >= 0 && midTerm <= 100) break;
                        System.out.println(SpecialColours.RED + "Invalid midterm score! Please enter between 0-100." + SpecialColours.RESET);
                    } catch (NumberFormatException e) {
                        System.out.println(SpecialColours.RED + "Invalid entry! Please enter a number." + SpecialColours.RESET);
                    }
                }

                double finalTerm;
                while (true) {
                    System.out.print("Midterm (0-100): ");
                    String input = scanner.nextLine().trim();
                    try {
                        finalTerm = Double.parseDouble(input);
                        if (finalTerm >= 0 && finalTerm <= 100) break;
                        System.out.println(SpecialColours.RED + "Invalid final term! Enter between 0-100." + SpecialColours.RESET);
                    } catch (NumberFormatException e) {
                        System.out.println(SpecialColours.RED + "Invalid entry! Please enter a number." + SpecialColours.RESET);
                    }
                }
                StudentDto newStudent = new StudentDto(maxId, name, surname,birthDate, midTerm, finalTerm, studentTypeMethod(),ERole.STUDENT);
                Optional<StudentDto> createdStudent = create(newStudent);

                if (createdStudent != null) {
                    break;
                } else {
                    System.out.println(SpecialColours.RED + "An error occurred while adding the student. Please try again." + SpecialColours.RESET);
                }
            } catch (Exception e) {
                System.out.println(SpecialColours.RED + "An unexpected error occurred: " + e.getMessage() + SpecialColours.RESET);
                scanner.nextLine();
            }
        }
    }

    public void chooiseStudentList() {
        try {
            List<StudentDto> students = list();
            if (students.isEmpty()) {
                System.out.println(SpecialColours.RED + " Student list is empty." + SpecialColours.RESET);
            } else {
                System.out.println(SpecialColours.GREEN + "Student List:" + SpecialColours.RESET);
            }
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void chooiseStudenSearch() {
        list();
        System.out.print("Student Name to Search:");
        String searchName = scanner.nextLine();
        try {
            System.out.println(findByName(searchName));
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /// Student Update
    public void chooiseStudenUpdate() {
        list();
        System.out.print("Student ID to be updated: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("New Name: ");
        String nameUpdate = scanner.nextLine();

        System.out.print("New Surname: ");
        String surnameUpdate = scanner.nextLine();

        System.out.print("Birth Date (YYYY-MM-DD): ");
        LocalDate birthDateUpdate = LocalDate.parse(scanner.nextLine());

        System.out.print("New midterm: ");
        double midTermUpdate = scanner.nextDouble();

        System.out.print("New final term: ");
        double finalTermUpdate = scanner.nextDouble();

        StudentDto studentUpdate = new StudentDto(id, nameUpdate, surnameUpdate,birthDateUpdate, midTermUpdate, finalTermUpdate, studentTypeMethod(), ERole.STUDENT);
        try {
            update(id, studentUpdate);
            System.out.println("The student was updated successfully.");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /// Student Delete
    public void chooiseStudenDelete() {
        list();
        System.out.print("Student ID to be deleted: ");
        int deleteID = scanner.nextInt();
        try {
            delete(deleteID);
            System.out.println("The student was deleted successfully.");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /// Student Sum Counter
    public void chooiseSumCounter() {
        System.out.println("Total Number of Students: " + studentDtoList.size());
    }

    /// Student Random
    public void chooiseRandomStudent() {
        if (!studentDtoList.isEmpty()) {
            StudentDto randomStudent = studentDtoList.get((int) (Math.random() * studentDtoList.size()));
            System.out.println("Randomly Selected Student: " + randomStudent);
        } else {
            System.out.println("There are no students in the system.");
        }
    }

    /// Öğrenci Not Ortalaması Hesapla
    public void chooiseStudentNoteAverage() {
        if (!studentDtoList.isEmpty()) {
            double avg = studentDtoList.stream()
                    .mapToDouble(StudentDto::getResultTerm)
                    .average()
                    .orElse(0.0);
            System.out.println("Student Grade Point Average: " + avg);
        } else {
            System.out.println("Student list is empty.");
        }
    }

    /// En Yüksek & En Düşük Not Alan Öğrenci
    public void chooiseStudentNoteMinAndMax() {
        if (!studentDtoList.isEmpty()) {
            StudentDto maxStudent = studentDtoList.stream()
                    .max((s1, s2) -> Double.compare(s1.getResultTerm(), s2.getResultTerm()))
                    .orElse(null);

            StudentDto minStudent = studentDtoList.stream()
                    .min((s1, s2) -> Double.compare(s1.getResultTerm(), s2.getResultTerm()))
                    .orElse(null);

            System.out.println("Student with the Highest Score: " + maxStudent);
            System.out.println("Student with the Lowest Score: " + minStudent);
        } else {
            System.out.println("Student list is empty.");
        }
    }


    public void chooiseStudentBirthdaySortedDate() {
        studentDtoList.stream()
                .sorted((s1, s2) -> s1.getBirthDate().compareTo(s2.getBirthDate()))
                .forEach(System.out::println);
    }

    private List<StudentDto> listStudentsWithStatus() {
        List<StudentDto> studentDtostatus = list();
        return studentDtostatus;
    }

    // Exit
    public void chooiseExit() {
        System.out.println("Exiting the system...");
        scanner.close();
        return;
    }

    // TEST
    public static void main(String[] args) {

    }
}
