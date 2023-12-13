package ru.konovalov.FirstRestApp3.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

@RestController
@RequestMapping("/raw")
public class RandomRawController {

    private static final String filePath = "src/text.txt";

    private int countRaw() throws IOException {
        try (LineNumberReader count = new LineNumberReader(new BufferedReader(new FileReader(filePath)))) {
            while (count.readLine() != null) ;
            return count.getLineNumber();
        }
    }
    @GetMapping()
    public ResponseEntity<?> returnUserFromDBByLogin() throws IOException {

        Random random = new Random();

        int lineNumber = random.nextInt(countRaw() - 1);
        String line = "";
        String allText = new String(
                Files.readAllBytes(Paths.get(filePath)));
        try {
            line = allText.split("\n")[lineNumber];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(line, HttpStatus.OK);
    }
}
