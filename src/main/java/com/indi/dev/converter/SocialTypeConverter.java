package com.indi.dev.converter;

import com.indi.dev.entity.SocialType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SocialTypeConverter implements Converter<String, SocialType> {

    @Override
    public SocialType convert(String value) {
        return SocialType.fromName(value);
    }

}
