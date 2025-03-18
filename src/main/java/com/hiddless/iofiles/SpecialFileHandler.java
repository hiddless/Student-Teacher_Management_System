package com.hiddless.iofiles;

import com.hiddless.utils.SpecialColours;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpecialFileHandler {

    private static final Logger logger = Logger.getLogger(SpecialFileHandler.class.getName());
    private String filePath;

    public SpecialFileHandler() {
        this.filePath = "default.txt";
    }

    public void createFileIfNotExists() {
        File file = new File(filePath);
        try {
            if (file.exists()) {
                logger.info("File is already created." + filePath);
            } else {
                if (file.createNewFile()) {
                    logger.info("File created succesfully." + filePath);
                }else {
                    logger.warning("Error file didn't created" + filePath);
                }
            }
        }catch (IOException e) {
            logger.log(Level.SEVERE, "File creation error: " + e.getMessage(), e);
        }
    }

    public void writeFile(String data) {
        if (data == null || data.trim().isEmpty()) {
            logger.warning("Empty data cannot be written.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            writer.write(data);
            writer.newLine();
            logger.info("Data succesfully written in file: " + filePath);
        }catch (IOException e) {
            logger.log(Level.SEVERE, "Error in writing to file" + e.getMessage(), e);
        }
    }

    public List<String> readFile() {
        File file = new File(filePath);
        List<String> fileLines = new ArrayList<>();

        if (!file.exists()) {
            logger.warning("⚠️ There is no File to read: " + filePath);
            return fileLines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            logger.info("📖 Reading from File...");
            while ((line = reader.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "File reading error: " + e.getMessage(), e);
        }

        if (fileLines.isEmpty()) {
            logger.warning("File is empty");
        } else {
            logger.info("From File " + fileLines.size() + " satır başarıyla okundu.");
        }

        return fileLines;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.warning("Wrong File path, Setting it to default: default.txt");
            this.filePath = "default.txt";
        } else {
            this.filePath = filePath;
        }
    }
}
