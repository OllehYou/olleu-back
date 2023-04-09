package com.example.olleuback.config.restdocs.config;

import com.example.olleuback.common.olleu_enum.EnumType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("enums")
public class EnumController {

    Logger log = LoggerFactory.getLogger(Logger.class);

    @GetMapping
    public Map<String, Map<String, String>> enums() throws IllegalAccessException {
        Map<String, Map<String, String>> enumMap = new HashMap<>();
        Reflections reflections = new Reflections("com.example.olleuback.common", Scanners.values());
        Set<Class<? extends EnumType>> subTypesOf = reflections.getSubTypesOf(EnumType.class);
        for(var enumType : subTypesOf) {
            Map<String, String> keyValue = new HashMap<>();
            for(var field: enumType.getDeclaredFields()){
                field.setAccessible(true);
                if(field.isEnumConstant()) {
                    keyValue.put(field.getName(), ((EnumType) field.get(enumType)).getDescription());
                }
            }
            enumMap.put(enumType.getSimpleName(), keyValue);
        }
        return enumMap;
    }
}
