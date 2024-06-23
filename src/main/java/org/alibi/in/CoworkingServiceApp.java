package org.alibi.in;

import org.alibi.application.*;
import org.alibi.domain.model.User;
import org.alibi.infrastructure.*;
import org.alibi.out.ConsoleOutput;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Главный класс приложения Coworking Service.
 */
public class CoworkingServiceApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Инициализация репозиториев
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryBookingRepository bookingRepository = new InMemoryBookingRepository();
        InMemoryWorkspaceRepository workspaceRepository = new InMemoryWorkspaceRepository();
        InMemoryConferenceRoomRepository conferenceRoomRepository = new InMemoryConferenceRoomRepository();

        // Инициализация сервисов
        UserService userService = new UserService(userRepository);
        AuthorizationService authorizationService = new AuthorizationService();
        BookingService bookingService = new BookingService(bookingRepository, authorizationService, conferenceRoomRepository, workspaceRepository);
        WorkspaceService workspaceService = new WorkspaceService(workspaceRepository, authorizationService);
        ConferenceRoomService conferenceRoomService = new ConferenceRoomService(conferenceRoomRepository, authorizationService);

        // Инициализация обработчиков ввода
        UserInputHandler userInputHandler = new UserInputHandler(userService);
        ConsoleOutput consoleOutput = new ConsoleOutput();
        BookingInputHandler bookingInputHandler = new BookingInputHandler(bookingService, consoleOutput);
        WorkspaceInputHandler workspaceInputHandler = new WorkspaceInputHandler(workspaceService, consoleOutput);
        ConferenceRoomInputHandler conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);

        // Предварительное заполнение данных
        PrepopulateData.prepopulate(userService, workspaceService, conferenceRoomService);

        // Логика приложения
        System.out.println("Welcome to Coworking Service");
        User loggedInUser = null;

        while (loggedInUser == null) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            if (option == 1) {
                userInputHandler.registerUser();
            } else if (option == 2) {
                loggedInUser = userInputHandler.loginUser();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        // Основной цикл приложения
        boolean running = true;
        while (running) {
            System.out.println("1. View available workspaces");
            System.out.println("2. View available conference rooms");
            System.out.println("3. Book a workspace");
            System.out.println("4. Book a conference room");
            System.out.println("5. View all bookings");
            System.out.println("6. View my bookings");
            System.out.println("7. Filter bookings");
            System.out.println("8. Cancel a booking");

            if (authorizationService.isAdmin(loggedInUser)) {
                System.out.println("9. Add workspace");
                System.out.println("10. Update workspace");
                System.out.println("11. Delete workspace");
                System.out.println("12. View all workspaces");
                System.out.println("13. Add conference room");
                System.out.println("14. Update conference room");
                System.out.println("15. Delete conference room");
                System.out.println("16. View all conference rooms");
                System.out.println("17. Exit");
            } else {
                System.out.println("9. Exit");
            }

            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            if (authorizationService.isAdmin(loggedInUser)) {
                switch (option) {
                    case 1:
                        consoleOutput.printWorkspaces(bookingService.getAvailableWorkspaces(LocalDate.now()));
                        break;
                    case 2:
                        consoleOutput.printConferenceRooms(bookingService.getAvailableConferenceRooms(LocalDate.now()));
                        break;
                    case 3, 4:
                        bookingInputHandler.bookResource(loggedInUser);
                        break;
                    case 5:
                        consoleOutput.printBookings(bookingService.getAllBookings(loggedInUser));
                        break;
                    case 6:
                        consoleOutput.printBookings(bookingService.getUserBookings(loggedInUser));
                        break;
                    case 7:
                        bookingInputHandler.filterBookings();
                        break;
                    case 8:
                        bookingInputHandler.cancelBooking(loggedInUser);
                        break;
                    case 9:
                        workspaceInputHandler.addWorkspace(loggedInUser);
                        break;
                    case 10:
                        workspaceInputHandler.updateWorkspace(loggedInUser);
                        break;
                    case 11:
                        workspaceInputHandler.deleteWorkspace(loggedInUser);
                        break;
                    case 12:
                        workspaceInputHandler.viewAllWorkspaces();
                        break;
                    case 13:
                        conferenceRoomInputHandler.addConferenceRoom(loggedInUser);
                        break;
                    case 14:
                        conferenceRoomInputHandler.updateConferenceRoom(loggedInUser);
                        break;
                    case 15:
                        conferenceRoomInputHandler.deleteConferenceRoom(loggedInUser);
                        break;
                    case 16:
                        conferenceRoomInputHandler.viewAllConferenceRooms();
                        break;
                    case 17:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                switch (option) {
                    case 1:
                        consoleOutput.printWorkspaces(bookingService.getAvailableWorkspaces(LocalDate.now()));
                        break;
                    case 2:
                        consoleOutput.printConferenceRooms(bookingService.getAvailableConferenceRooms(LocalDate.now()));
                        break;
                    case 3, 4:
                        bookingInputHandler.bookResource(loggedInUser);
                        break;
                    case 5:
                        consoleOutput.printBookings(bookingService.getAllBookings(loggedInUser));
                        break;
                    case 6:
                        consoleOutput.printBookings(bookingService.getUserBookings(loggedInUser));
                        break;
                    case 7:
                        bookingInputHandler.filterBookings();
                        break;
                    case 8:
                        bookingInputHandler.cancelBooking(loggedInUser);
                        break;
                    case 9:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }

        System.out.println("Thank you for using Coworking Service!");
        scanner.close();
    }
}
