package com.hiddless.dao;

import com.hiddless.dto.RegisterDto;
import com.hiddless.exceptions.RegisterNotFoundException;
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

    /// Constructor without Parameters
    public RegisterDao() {
        this.fileHandler= new FileHandler();
        this.fileHandler.setFilePath("registers.txt");

        registerDtoList = new ArrayList<>();

        this.fileHandler.createFileIfNotExists();

        this.fileHandler.readFile(this.fileHandler.getFilePath());
    }

    /// Random generate new id
    private int generateNewId() {
        return registerDtoList
                .stream()
                .mapToInt(RegisterDto::getId)
                .max()
                .orElse(0) + 1;
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
            if (parts.length < 6) {
                System.err.println("X Wrong CSV" +csvLine);
                return null;
            }
            int id = generateNewId();
            return new RegisterDto(id, parts[0], parts[1], parts[2], parts[3],Boolean.valueOf(parts[4]) , null, null);
        }catch (Exception e) {
            System.out.println(SpecialColours.RED + "Failed to Download from CSV!" + SpecialColours.RESET);
            return null;
        }

    }


    //////////////////////////////////////////////////////////////////////

    /// Create
    @Override
    public Optional<RegisterDto> create(RegisterDto registerDto) {
        registerDtoList.add(registerDto);
        this.fileHandler.writeFile(this.fileHandler.getFilePath());
        return Optional.of(registerDto);
    }

    /// List
    @Override
    public List<RegisterDto> list() {
        if (registerDtoList.isEmpty()) {
            throw new RegisterNotFoundException(SpecialColours.RED + "List is empty" +SpecialColours.RESET);
        }
        return new ArrayList<>(registerDtoList);
    }

    /// Find by nickname
    @Override
    public Optional<RegisterDto> findByName(String nickName) {
        return registerDtoList
                .stream()
                .filter(s -> s.getNickname().equalsIgnoreCase(nickName))
                .findFirst();
    }

    /// Update
    @Override
    public Optional<RegisterDto> update(int id, RegisterDto registerDto) {
        if (registerDto != null) {
            for (int i = 0; i < registerDtoList.size(); i++){
                if (registerDtoList.get(i).getId() == id) {
                    registerDtoList.set(i, registerDto);
                    this.fileHandler.writeFile(this.fileHandler.getFilePath());
                }
            }
        }
        throw new RegisterNotFoundException("Not found any person to update");
    }

    /// Delete
    @Override
    public Optional<RegisterDto> delete(int id) {
        return Optional.empty();
    }

    //////////////////////////////////////////////////////////////////////

    @Override
    public void chooise() {

    }

}
