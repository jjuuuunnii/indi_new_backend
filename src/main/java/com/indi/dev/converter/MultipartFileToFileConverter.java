package com.indi.dev.converter;

import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.custom.ErrorCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class MultipartFileToFileConverter implements Converter<MultipartFile, File> {

    @Override
    public File convert(MultipartFile source) {
        try {

            File tempFile = File.createTempFile("temp", null);
            source.transferTo(tempFile);
            return tempFile;

        } catch (IOException e) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }
}
