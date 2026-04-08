import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.reservationId = UUID.randomUUID().toString();
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation [ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType + "]";
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int availableCount) {
        inventory.put(roomType, availableCount);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public boolean decrementAvailability(String roomType) {
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            return true;
        }
        return false;
    }

    public void incrementAvailability(String roomType) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) + 1);
        }
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored: " + reservation);
    }

    public void removeReservation(Reservation reservation) {
        history.remove(reservation);
        System.out.println("Reservation cancelled: " + reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        rollbackStack = new Stack<>();
    }

    public void cancelReservation(Reservation reservation) {
        if (history.getAllReservations().contains(reservation)) {
            rollbackStack.push(reservation.getReservationId());

            inventory.incrementAvailability(reservation.getRoomType());

            history.removeReservation(reservation);

            System.out.println("Cancellation successful. Room rolled back for: " + reservation.getGuestName());
        } else {
            System.out.println("Cancellation failed: Reservation not found or already cancelled.");
        }
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (recent cancellations): " + rollbackStack);
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v10.1");
        System.out.println("Initializing cancellation and rollback...\n");

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 1);

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        history.addReservation(r1);
        history.addReservation(r2);

        inventory.decrementAvailability(r1.getRoomType());
        inventory.decrementAvailability(r2.getRoomType());

        CancellationService cancellationService = new CancellationService(inventory, history);

        cancellationService.cancelReservation(r1);

        cancellationService.displayRollbackStack();
        inventory.displayInventory();

        System.out.println("\nSystem terminated.");
    }
}