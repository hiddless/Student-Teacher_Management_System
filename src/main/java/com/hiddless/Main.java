package com.hiddless;

import com.hiddless.Controller.StudentController;
import com.hiddless.Controller.TeacherController;
import com.hiddless.utils.SpecialColours;

import java.util.Scanner;

public class Main {
    /// Scanner
    private static final Scanner scanner = new Scanner(System.in);

    /// Chooise
    private static void chooise() {
        while (true) {
            try {
                System.out.println(SpecialColours.CYAN + "\n===== Teacher Management System ====="+ SpecialColours.RESET);
                System.out.println(SpecialColours.BLUE + "Add Teacher Number 1." + SpecialColours.RESET);
                System.out.println(SpecialColours.BLUE + "Add Student Number 1." + SpecialColours.RESET );
                System.out.println(SpecialColours.GREEN + "3- Exit." + SpecialColours.RESET);
                System.out.println(SpecialColours.YELLOW + "\nPlease Select: " + SpecialColours.RESET);

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> teacher();
                    case 2 -> student();
                    case 3 -> {
                        System.out.println(SpecialColours.YELLOW + "Exiting from System...");
                        return;
                    }
                    default -> System.out.println(SpecialColours.RED + "Wrong choice! Please try again!");
                } catch (Exception e) {
                    System.out.println(SpecialColours.RED + "There is an error, Please Try again." + SpecialColours.RESET + e.getmessage());
                    scanner.nextLine();
                }
            }
        }
        /// Student Controller
        private static void student() {
            try {
                StudentController studentController = new StudentController();
                studentController.chooise();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private static void teacher() {
            try {
                TeacherController teacherController= new TeacherController();
                //TeacherDao teacherDao = new TeacherDao();
                //teacherDao.chooise();
                teacherController.chooise();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        chooise();
    }
}