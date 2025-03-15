package com.hiddless.iofiles;

import com.hiddless.utils.SpecialColours;

import java.io.*;

public class FileHandler implements IFileHandlerInterface {

    /// Field
    private String filePath="";

    /// Constructor
    public FileHandler() {
        filePath="isnotfilename.txt";
    }

    /// Methods

    @Override
    public synchronized void createFileIfNotExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println(SpecialColours.GREEN + filePath + " is Created." +SpecialColours.RESET);
                }
            }catch (IOException e) {
                System.out.println(SpecialColours.RED + "There is an error in creating the file!" + SpecialColours.RESET);
                e.printStackTrace();
            }
        }
    }

    /// Write file
    @Override
    public synchronized void writeFile(String data){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath,true))) {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /// Read File
    @Override
    public synchronized void readFile(String data) {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line="";
            while ((line= bufferedReader.readLine())!=null) {
                System.out.println("Read from file."+line);
            }
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        if (filePath==null || filePath.isEmpty() || filePath.isBlank()){
            this.filePath="isnotfilename.txt";
        }
        this.filePath = filePath;
    }

}
