package com.hiddless.dto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RegisterDto  {

    private String emailAddress;
    private String password;

    private static final String AES_ALGORITH="AES";
    private static final String SECRET_KEY="MY_SECRET_AES_KEY";

    private StudentDto studentDto;

    public RegisterDto() {
        this.emailAddress = "your_email address";
        this.password = "your password";
        studentDto= new StudentDto();
    }

    private static String encryptPassword(String password) {
        try {
            SecretKey key = generateKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITH);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return new String(encryptedBytes, StandardCharsets.UTF_8);
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Encrption Failed",exception);
        }
    }

    public static String deEncryptPassword(String encryptedPassword) {
        try {
            SecretKey key = generateKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITH);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Encrption Failed" , exception);
        }
    }

    private static SecretKey generateKey() {
        try {
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            return new SecretKeySpec(keyBytes,0,16,AES_ALGORITH);
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Encrption Failed" , exception);
        }
    }

    @Override
    public String toString() {
        return "RegisterDto{" +
                "emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", studentDto=" + studentDto +
                '}';
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password= encryptPassword(password);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StudentDto getStudentDto() {
        return studentDto;
    }

    public void setStudentDto(StudentDto studentDto) {
        this.studentDto = studentDto;
    }


}
