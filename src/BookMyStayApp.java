import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
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

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public boolean allocateRoom(String roomType) {
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

class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored: " + reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }

    public void displayHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

class PersistenceService {
    public static void saveState(RoomInventory inventory, BookingHistory history, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nSystem state saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public static Object[] loadState(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("\nSystem state loaded from file: " + filename);
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
            return new Object[]{new RoomInventory(), new BookingHistory()};
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v12.1");
        String filename = "system_state.dat";

        Object[] state = PersistenceService.loadState(filename);
        RoomInventory inventory = (RoomInventory) state[0];
        BookingHistory history = (BookingHistory) state[1];

        if (history.getAllReservations().isEmpty()) {
            System.out.println("\nNo previous state found. Initializing fresh system...");
            inventory.addRoomType("Single Room", 2);
            inventory.addRoomType("Double Room", 1);

            Reservation r1 = new Reservation("Alice", "Single Room");
            Reservation r2 = new Reservation("Bob", "Double Room");

            if (inventory.allocateRoom(r1.getRoomType())) history.addReservation(r1);
            if (inventory.allocateRoom(r2.getRoomType())) history.addReservation(r2);
        }

        inventory.displayInventory();
        history.displayHistory();

        PersistenceService.saveState(inventory, history, filename);

        System.out.println("\nSystem terminated safely with persistence.");
    }
}