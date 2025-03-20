package com.hiddless.Controller;

import com.hiddless.dao.RegisterDao;
import com.hiddless.dao.StudentDao;
import com.hiddless.dao.TeacherDao;
import com.hiddless.dto.*;
import com.hiddless.utils.SpecialColours;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

public class LoginRegisterController {

    /// Field
    private static final Logger logger = Logger.getLogger(LoginRegisterController.class.getName());
    private final RegisterDao registerDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final StudentController studentController;
    private final TeacherController teacherController;
    private final Scanner scanner = new Scanner(System.in);

    public LoginRegisterController() {
        registerDao = new RegisterDao();
        studentDao = new StudentDao();
        teacherDao = new TeacherDao();
        studentController= new StudentController();
        teacherController= new TeacherController();
    }

    public void isUserRole(RegisterDto registerDto) {
        switch (registerDto.getRole()) {
            case "STUDENT" -> studentController.choose();
            case "TEACHER" -> teacherController.choose();
            default -> System.out.println("Authorization is not done, please contact the admin.");
        }
    }

    public void login() {
        int maxAttempts = 3;
        Map<String, Integer> loginAttemts = new HashMap<>();

        while (true) {
            System.out.println("\n==== Login ==== *By Hiddless");
            System.out.println("Email: ");
            String email = scanner.nextLine().trim();
            System.out.println("Password: ");
            String password = scanner.nextLine().trim();

            Optional<RegisterDto> findIsEmail = registerDao.findByEmail(email);
            if (findIsEmail.isPresent()) {
                RegisterDto user = findIsEmail.get();

                if (user.isLocked()) {
                    System.out.println("Your Account is locked");
                    return;
                }
                if (user.validatePassword(password)) {
                    System.out.println(SpecialColours.GREEN + "You Logged in" + SpecialColours.RESET +
                            SpecialColours.PURPLE + "Welcome " + email +SpecialColours.RESET);
                    isUserRole(user);
                    break;
                }else {
                    loginAttemts.put(email, loginAttemts.getOrDefault(email, 0) + 1);
                    int remaining = maxAttempts - loginAttemts.get(email);
                    System.out.println("Error: Wrong nickname or password. Remaining Chances: " + remaining);

                    if (remaining == 0) {
                        user.setLocked(true);
                        registerDao.update(user.getId(), user);
                        System.out.println("Error: Your account is locked due to 3 wrong attemps");
                        return;
                    }
                }
            }else {
                System.out.println("User is not found! You need to Sign Up first");
                register();
            }
        }
    }

    private void register() {
        System.out.println("\n" + SpecialColours.PURPLE + "New user " + SpecialColours.RESET);
        String name, surname, email, nickname, password;
        LocalDate birthDate;
        ERole role = null;

        System.out.println("Name: ");
        name = scanner.nextLine().trim();
        System.out.println("Surname: ");
        surname = scanner.nextLine().trim();
        System.out.println("Nickname: ");
        nickname = scanner.nextLine().trim();
        System.out.println("Email: ");
        email = scanner.nextLine().trim();
        System.out.println("Password: ");
        password = scanner.nextLine().trim();

        while (true) {
            try {
                System.out.println("Birth Date (MM-DD-YYYY): ");
                birthDate = LocalDate.parse(scanner.nextLine().trim());
                break;
            }catch (DateTimeException e) {
                System.out.println("Wrong Date Format! Please try again (MM-DD-YYYY)");
            }
        }

        int generatedId = registerDao.generateNewId();
        RegisterDto register;

        if (role == ERole.STUDENT) {
            System.out.println("Your Student Type (UNDERGRAUDUATE, GRADUATE, PHD, OTHER: ");
            EStudentType studentType = EStudentType.valueOf(scanner.nextLine().trim().toUpperCase());
            StudentDto student = new StudentDto(generatedId, name, surname, birthDate, studentType, role);
            register = new RegisterDto(generatedId, nickname, email, password, "STUDENT" , false , student, null);
            studentDao.create(student);
        }else {
            System.out.println("Your Profession (MATHEMATICS, CHEMISTRY, BIOLOGY, HISTORY, COMPUTER_SCIENCE, OTHER): ");
            ETeacherSubject eTeacherSubject = ETeacherSubject.valueOf(scanner.nextLine().trim().toUpperCase());
            System.out.println("Years Of Experience: ");
            int yearsOfExperience = scanner.nextInt();
            System.out.println("Salary: ");
            double salary = scanner.nextDouble();
            scanner.nextLine();

            TeacherDto teacher = new TeacherDto(generatedId, name , surname , birthDate, eTeacherSubject, yearsOfExperience, false, salary);
            register = new RegisterDto(generatedId, nickname, email, password, "TEACHER", false, null, teacher);
            teacherDao.create(teacher);
        }

        registerDao.create(register);
        System.out.println("You are in!");
    }

}
