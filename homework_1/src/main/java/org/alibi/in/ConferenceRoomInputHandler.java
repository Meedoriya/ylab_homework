package org.alibi.in;

import org.alibi.application.ConferenceRoomService;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.out.ConsoleOutput;

import java.util.Scanner;

/**
 * Обработчик ввода для операций с конференц-залами.
 */
public class ConferenceRoomInputHandler {
    private final ConferenceRoomService conferenceRoomService;
    private final Scanner scanner;
    private final ConsoleOutput consoleOutput;

    /**
     * Конструктор.
     *
     * @param conferenceRoomService Сервис для работы с конференц-залами.
     * @param consoleOutput         Объект для вывода на консоль.
     */
    public ConferenceRoomInputHandler(ConferenceRoomService conferenceRoomService, ConsoleOutput consoleOutput) {
        this.conferenceRoomService = conferenceRoomService;
        this.scanner = new Scanner(System.in);
        this.consoleOutput = consoleOutput;
    }

    /**
     * Метод для добавления нового конференц-зала.
     *
     * @param user Пользователь, выполняющий операцию.
     */
    public void addConferenceRoom(User user) {
        System.out.print("Enter conference room name: ");
        String name = scanner.nextLine();

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName(name);

        try {
            conferenceRoomService.addConferenceRoom(user, conferenceRoom);
            System.out.println("Conference room added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для обновления существующего конференц-зала.
     *
     * @param user Пользователь, выполняющий операцию.
     */
    public void updateConferenceRoom(User user) {
        System.out.print("Enter conference room ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new conference room name: ");
        String name = scanner.nextLine();

        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setId(id);
        conferenceRoom.setName(name);

        try {
            conferenceRoomService.updateConferenceRoom(user, conferenceRoom);
            System.out.println("Conference room updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для удаления конференц-зала.
     *
     * @param user Пользователь, выполняющий операцию.
     */
    public void deleteConferenceRoom(User user) {
        System.out.print("Enter conference room ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());

        try {
            conferenceRoomService.deleteConferenceRoom(user, id);
            System.out.println("Conference room deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для просмотра всех конференц-залов.
     */
    public void viewAllConferenceRooms() {
        consoleOutput.printConferenceRooms(conferenceRoomService.getAllConferenceRooms());
    }
}
