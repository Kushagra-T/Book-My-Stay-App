import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation [Guest: " + guestName + ", Room Type: " + roomType + "]";
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public synchronized void addRoomType(String roomType, int availableCount) {
        inventory.put(roomType, availableCount);
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public synchronized boolean allocateRoom(String roomType) {
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory State:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}

class BookingProcessor implements Runnable {
    private Reservation reservation;
    private RoomInventory inventory;

    public BookingProcessor(Reservation reservation, RoomInventory inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        synchronized (inventory) {
            if (inventory.allocateRoom(reservation.getRoomType())) {
                System.out.println("Booking Confirmed: " + reservation);
            } else {
                System.out.println("Booking Failed (No Availability): " + reservation);
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v11.1");
        System.out.println("Simulating concurrent booking requests...\n");

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Single Room");
        Reservation r3 = new Reservation("Charlie", "Single Room"); // should fail
        Reservation r4 = new Reservation("Diana", "Double Room");
        Reservation r5 = new Reservation("Ethan", "Double Room");   // should fail

        Thread t1 = new Thread(new BookingProcessor(r1, inventory));
        Thread t2 = new Thread(new BookingProcessor(r2, inventory));
        Thread t3 = new Thread(new BookingProcessor(r3, inventory));
        Thread t4 = new Thread(new BookingProcessor(r4, inventory));
        Thread t5 = new Thread(new BookingProcessor(r5, inventory));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();

        System.out.println("\nSystem terminated safely.");
    }
}