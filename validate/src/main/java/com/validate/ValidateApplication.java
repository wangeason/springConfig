package com.validate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class ValidateApplication {

    @Value("${allowedSpecial}")
    private String specialChar;

    public static void main(String[] args) {
        SpringApplication.run(ValidateApplication.class, args);
    }

    @GetMapping("/validate")
    public Boolean validate(@RequestParam String pwd)  {
        if (pwd.length() < 8) return false;
        HashSet<Character> allowedSpecial = specialChar.chars().mapToObj(e->(char)e).collect(Collectors.toCollection(HashSet::new));

        boolean containNumber = false;
        boolean containCap = false;
        boolean containSpecial = false;

        //in case the pwd is too long
        Set<Character> charsSet = pwd.chars().parallel()
                .mapToObj(e->(char)e).collect(Collectors.toSet());
        for (Character c: charsSet) {
            if (Character.isDigit(c)) containNumber = true;
            else if (Character.isUpperCase(c)) containCap = true;
            else if (allowedSpecial.contains(c)) containSpecial = true;
            else if (!Character.isAlphabetic(c)) return false;
        }
        return containNumber & containCap & containSpecial;
    }
}
