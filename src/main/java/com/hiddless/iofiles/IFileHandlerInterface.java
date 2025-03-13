package com.hiddless.iofiles;

public interface IFileHandlerInterface {

    void createFileIfNotExists();

    void writeFile(String data);
    void readFile(String data);

}
