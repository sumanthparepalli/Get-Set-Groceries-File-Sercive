package com.example.demo.services;

import com.example.demo.exceptions.FileNotFoundInDB;
import com.example.demo.exceptions.FileStorageException;
import com.example.demo.models.DBFile;
import com.example.demo.repositories.DBFileRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Service
public class DBFileService {

    private final DBFileRepo dbFileRepo;

    public DBFileService(DBFileRepo dbFileRepo) {
        this.dbFileRepo = dbFileRepo;
    }

    public DBFile storeFile(MultipartFile file) throws FileStorageException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Invalid path sequence!" + fileName);
            }
            DBFile dbFile=new DBFile(fileName, file.getContentType(), file.getBytes());
            dbFile.setBase64Data(Base64.getEncoder().encodeToString(file.getBytes()));
            return dbFileRepo.save(dbFile);
        }
        catch (IOException e) {
            throw new FileStorageException("Could not store file: " + fileName +", please try again!");
        }
    }

    public DBFile getFile(String id)
    {
        return dbFileRepo.findById(id).orElseThrow(() -> new FileNotFoundInDB("File not found with Id " + id));
    }

}
