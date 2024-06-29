package org.alibi.in;

import org.alibi.application.WorkspaceService;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.out.ConsoleOutput;

import java.util.Scanner;

/**
 * Обработчик ввода для операций с рабочими местами.
 */
public class WorkspaceInputHandler {
    private final WorkspaceService workspaceService;
    private final Scanner scanner;
    private final ConsoleOutput consoleOutput;

    /**
     * Конструктор.
     *
     * @param workspaceService Сервис для работы с рабочими местами.
     * @param scanner          Сканнер для чтения ввода пользователя.
     * @param consoleOutput    Объект для вывода на консоль.
     */
    public WorkspaceInputHandler(WorkspaceService workspaceService, Scanner scanner, ConsoleOutput consoleOutput) {
        this.workspaceService = workspaceService;
        this.scanner = scanner;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Метод для добавления нового рабочего места.
     *
     * @param user Пользователь, выполняющий операцию.
     */
    public void addWorkspace(User user) {
        System.out.print("Enter workspace name: ");
        String name = scanner.nextLine();

        Workspace workspace = new Workspace();
        workspace.setName(name);

        try {
            workspaceService.addWorkspace(user, workspace);
            System.out.println("Workspace added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для обновления существующего рабочего места.
     *
     * @param user Пользователь, выполняющий операцию.
     */
    public void updateWorkspace(User user) {
        System.out.print("Enter workspace ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new workspace name: ");
        String name = scanner.nextLine();

        Workspace workspace = new Workspace();
        workspace.setId(id);
        workspace.setName(name);

        try {
            workspaceService.updateWorkspace(user, workspace);
            System.out.println("Workspace updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для удаления рабочего места.
     *
     * @param user Пользователь, выполняющий операцию.
     */
    public void deleteWorkspace(User user) {
        System.out.print("Enter workspace ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());

        try {
            workspaceService.deleteWorkspace(user, id);
            System.out.println("Workspace deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для просмотра всех рабочих мест.
     */
    public void viewAllWorkspaces() {
        consoleOutput.printWorkspaces(workspaceService.getAllWorkspaces());
    }
}
