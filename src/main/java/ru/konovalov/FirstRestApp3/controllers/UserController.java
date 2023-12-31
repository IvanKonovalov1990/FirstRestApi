package ru.konovalov.FirstRestApp3.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import ru.konovalov.FirstRestApp3.models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final String url = "jdbc:postgresql://192.168.0.105:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "postgrespassword";
    private static final String filePath = "src/user.txt";

    @GetMapping()
    public ResponseEntity<?> returnUserFromDBByLogin(@RequestBody User selectedUser) throws JsonProcessingException {
        String query = "select p.login, p.password, p.date, i.email " +
                "from people p join info i on p.login = i.login where p.login='" + selectedUser.getLogin() + "'";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                selectedUser.setPassword(resultSet.getString("password"));
                selectedUser.setDate(resultSet.getDate("date"));
                selectedUser.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(selectedUser.getPassword()==null && selectedUser.getEmail()==null) {
            return new ResponseEntity<>("This login does not exist!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        //File fileForUsersInfo = new File("filePath");
        //objectMapper.writeValue(fileForUsersInfo, selectedUser);
        /*String jsonString = objectMapper.writeValueAsString(selectedUser);
        String newRaw = "\n";
        try {
            Files.write(Paths.get(filePath), newRaw.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(filePath), jsonString.getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/

        return new ResponseEntity<>(selectedUser, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> insertUserIntoDB(@RequestBody User userInsertIntoDB) {

        String query = "insert into people (login, password, date) values (?, ?, ?);" +
                "insert into info (login, email) values (?, ?)";

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, userInsertIntoDB.getLogin());
            preparedStatement.setString(2, userInsertIntoDB.getPassword());
            preparedStatement.setDate(3, userInsertIntoDB.getDate());
            preparedStatement.setString(4, userInsertIntoDB.getLogin());
            preparedStatement.setString(5, userInsertIntoDB.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("User add into DB", HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<?> handleException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Bad request!", HttpStatus.BAD_REQUEST);
    }
}
