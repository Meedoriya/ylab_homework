package org.alibi.out;

import org.alibi.domain.model.Booking;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.Workspace;

import java.util.List;

public class ConsoleOutput {

    public void printWorkspaces(List<Workspace> workspaces) {
        System.out.println("Available Workspaces:");
        for (Workspace workspace : workspaces) {
            System.out.println("- " + workspace.getName());
        }
    }

    public void printConferenceRooms(List<ConferenceRoom> conferenceRooms) {
        System.out.println("Available Conference Rooms:");
        for (ConferenceRoom conferenceRoom : conferenceRooms) {
            System.out.println("- " + conferenceRoom.getName());
        }
    }

    public void printBookings(List<Booking> bookings) {
        System.out.println("Bookings:");
        for (Booking booking : bookings) {
            System.out.println("- Booking ID: " + booking.getId() + ", User ID: " + booking.getUserId() + ", Resource ID: " + booking.getResourceId() + ", Start Time: " + booking.getStartTime() + ", End Time: " + booking.getEndTime());
        }
    }
}

