package com.hiddless.dto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RegisterDto {

    private int id;
    private String nickname;
    private String emailAddress;
    private String password;

    /// role
    private String role;

    /// composition
    private StudentDto studentDto;
    private TeacherDto teacherDto;

    /// Aes encrypted
    private static final String AES_ALGORITHM = "AES";
    private static final String SECRET_KEY="MY_SECRET_AES_KEY";

    /// constructor without parameters
    public RegisterDto() {
        this.id = 0;
        this.nickname= "your_nick_name";
        this.emailAddress= "your_email.address";
        this.role = "is not roles";
        studentDto =null;
        teacherDto= null;
    }

    /// constructor with parameters
    public RegisterDto(int id, String _nickname, String emailAddress, String password, String role, StudentDto studentDto, TeacherDto teacherDto) {
        this.id = id;
        nickname= _nickname;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
        this.studentDto = studentDto;
        this.teacherDto = teacherDto;
    }

    /// Creating key
    private static SecretKey generateKey() {
        try {
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            return new SecretKeySpec(keyBytes,0,16,AES_ALGORITHM);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new RuntimeException("Encrption Failed", exception);
        }
    }

    /// Methods

    // AES
    private static String encryptPassword(String password) {
        try {
            SecretKey key = generateKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Encrption Failed", exception);
        }
    }


    /// AES password solver
    public static String deEncryptPassword(String encryptedPassword) {
        try {
            SecretKey key = generateKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE , key);

            byte[] decodeBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decryptedBytes = cipher.doFinal(decodeBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Encrption Failed", exception);
        }
    }

    /// To String
    @Override
    public String toString() {
        return "RegisterDto{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", role='" + role + '\'' +
                ", studentDto=" + studentDto +
                ",teacherDto=" + teacherDto +
                '}';
    }

    /// Getter And Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname.toLowerCase();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress.concat("@gmail.com");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = encryptPassword(password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StudentDto getStudentDto() {
        return studentDto;
    }

    public void setStudentDto(StudentDto studentDto) {
        this.studentDto = studentDto;
    }

    public TeacherDto getTeacherDto() {
        return teacherDto;
    }

    public void setTeacherDto(TeacherDto teacherDto) {
        this.teacherDto = teacherDto;
    }
}