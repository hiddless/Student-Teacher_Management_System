package com.hiddless.dao;

import com.hiddless.dto.RegisterDto;
import com.hiddless.iofiles.FileHandler;
import com.hiddless.utils.SpecialColours;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegisterDao implements IDaoGenerics<RegisterDto> {

    /// Field
    private final List<RegisterDto> registerDtoList;

    /// File Handler
    private FileHandler fileHandler;
    private String filePath;


    /// static
    static {
        System.out.println(SpecialColours.RED + " Static: RegisterDao" + SpecialColours.RESET);
    }

    /// Constructor with Parameters
    public RegisterDao() {
        this.fileHandler= new FileHandler();
        this.fileHandler.setFilePath("registers.txt");

        registerDtoList = new ArrayList<>();

        this.fileHandler.createFileIfNotExists();

        this.fileHandler.readFile(this.fileHandler.getFilePath());
    }

    /// register to CSV

    private String registerToCsv(RegisterDto registerDto) {
        return
                registerDto.getId() +"," +
                        registerDto.getNickname() + "," +
                        registerDto.getEmailAddress() + "," +
                        registerDto.getPassword() + "," +
                        registerDto.getRole();
    }


    /// studentdto to CSV
    private RegisterDto csvToRegister(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length < 5)
            {
                System.err.println("X Wrong CSV" +csvLine);
                return null;
            }
            int id = generateNewId();
            return new RegisterDto(id, parts[0], parts[1], parts[2], parts[3], null, null);
        }catch (Exception e) {
            System.out.println(SpecialColours.RED + "Failed to Download from CSV!" + SpecialColours.RESET);
            return null;
        }

    }

    /// Random generate new id
    private int generateNewId() {
        return 0;
    }

    //////////////////////////////////////////////////////////////////////

    @Override
    public Optional

}
