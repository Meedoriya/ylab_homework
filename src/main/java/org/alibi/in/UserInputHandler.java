package org.alibi.in;

import org.alibi.application.UserService;
import org.alibi.domain.model.User;

import java.util.Scanner;

/**
 * Обработчик ввода для операций с пользователями.
 */
public class UserInputHandler {
    private final UserService userService;
    private final Scanner scanner;

    /**
     * Конструктор.
     *
     * @param userService Сервис для работы с пользователями.
     */
    public UserInputHandler(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Метод для регистрации нового пользователя.
     */
    public void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        userService.registerUser(username, password);
        System.out.println("User registered successfully.");
    }

    /**
     * Метод для авторизации пользователя.
     *
     * @return Авторизованный пользователь.
     */
    public User loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        return userService.loginUser(username, password);
    }
}
