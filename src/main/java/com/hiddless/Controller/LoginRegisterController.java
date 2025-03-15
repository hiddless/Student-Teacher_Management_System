package com.hiddless.Controller;

import com.hiddless.dao.RegisterDao;
import com.hiddless.dao.StudentDao;
import com.hiddless.dao.TeacherDao;
import com.hiddless.dto.RegisterDto;
import com.hiddless.utils.SpecialColours;

import java.util.Optional;
import java.util.Scanner;

public class LoginRegisterController {

    /// Field
    private final RegisterDao registerDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final StudentController studentController;
    private final TeacherController teacherController;
    private final Scanner scanner;

    /// Constructor
    public LoginRegisterController() {
        registerDao = new RegisterDao();
        studentDao = new StudentDao();
        teacherDao = new TeacherDao();
        studentController = new StudentController();
        teacherController = new TeacherController();
        scanner = new Scanner(System.in);
    }

    /////////////////////////////////////////////////////////////////

    //Role Method
    public void isUserRole(RegisterDto registerDto) {
        if (registerDto.getRole().equalsIgnoreCase("STUDENT")) {
            studentController.chooise();
        }else if (registerDto.getRole().equalsIgnoreCase("TEACHER")) {
            teacherController.chooise();
        }else if (registerDto.getRole().equalsIgnoreCase("ADMIN")) {
            System.out.println("Welcome to Admin Page");
        }else {
            System.out.println("Authorization is not done, please contact the Admin. phone: 111-111-11-11");
        }
    }

    /// Login
    public void login() {
        while (true) {
            System.out.println("\n==== Entry Page ====");
            String email, password, nickname;

            System.out.println("Email address");
            email = scanner.nextLine().trim();

            System.out.println("Nickname");
            nickname = scanner.nextLine().trim();

            System.out.println("Password");
            password =scanner.nextLine().trim();

            Optional<RegisterDto> findIsEmail = registerDao.findByEmail(email);
            if (findIsEmail.isPresent()) {

                RegisterDto user = findIsEmail.get();

                if (user.validatePassword(password)) {
                    System.out.println(SpecialColours.GREEN + "Succesfully Logged In" + SpecialColours.RESET + SpecialColours.PURPLE + "Welcome" + email + SpecialColours.RESET);
                    isUserRole(user);
                    break;
                }else {
                    System.out.println("Error: Nickname or password is wrong");
                }
            }else {
                System.out.println("You Need To Sign Up First");
                register();
            }
        }
    }

    private void register() {
    }
}
