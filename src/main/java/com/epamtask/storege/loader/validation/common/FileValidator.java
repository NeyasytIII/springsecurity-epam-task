package com.epamtask.storege.loader.validation.common;

import com.epamtask.aspect.annotation.Loggable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileValidator {

    @Loggable
    public static boolean isFileValid(Resource resource, String filePath) {
        if (resource == null || !resource.exists()) {
            System.err.println("Resource is null or file not found: " + filePath);
            return false;
        }
        try (InputStream inputStream = resource.getInputStream()) {
            if (inputStream == null || inputStream.available() == 0) {
                System.err.println("File is empty: " + filePath);
                return false;
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error checking file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
}