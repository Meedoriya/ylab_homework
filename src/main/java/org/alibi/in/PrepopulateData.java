package org.alibi.in;

import org.alibi.application.ConferenceRoomService;
import org.alibi.application.UserService;
import org.alibi.application.WorkspaceService;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;

import static org.alibi.domain.model.Role.ADMIN;
import static org.alibi.domain.model.Role.USER;

/**
 * Класс для предварительного заполнения данных.
 */
public class PrepopulateData {
    /**
     * Предварительно заполняет данные в репозиториях.
     *
     * @param userService           Сервис для работы с пользователями.
     * @param workspaceService      Сервис для работы с рабочими местами.
     * @param conferenceRoomService Сервис для работы с конференц-залами.
     */
    public static void prepopulate(
            UserService userService,
            WorkspaceService workspaceService,
            ConferenceRoomService conferenceRoomService) {
        // Создание учетных записей админа и пользователя
        userService.registerUser("admin", "adminPass", ADMIN);
        userService.registerUser("user", "userPass", USER);

        // Получение учетной записи админа для операций с рабочими местами и конференц-залами
        User adminUser = userService.loginUser("admin", "adminPass");

        // Создание рабочих мест
        workspaceService.addWorkspace(adminUser, new Workspace(1L, "Workspace 1", true));
        workspaceService.addWorkspace(adminUser, new Workspace(2L, "Workspace 2", true));

        // Создание конференц-залов
        conferenceRoomService.addConferenceRoom(adminUser, new ConferenceRoom(1L, "Conference Room 1", true));
        conferenceRoomService.addConferenceRoom(adminUser, new ConferenceRoom(2L, "Conference Room 2", true));
    }
}
