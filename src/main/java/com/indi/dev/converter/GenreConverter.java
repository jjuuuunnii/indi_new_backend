package com.indi.dev.converter;

import com.indi.dev.entity.Genre;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenreConverter implements Converter<String, Genre> {

    @Override
    public Genre convert(String source) {
        return Genre.fromName(source);
    }
}
