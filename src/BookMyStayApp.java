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

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}

class BookingService {
    private Queue<Reservation> requestQueue;
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        requestQueue = new LinkedList<>();
        allocatedRooms = new HashMap<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request queued: " + reservation);
    }

    public void processRequests() {
        System.out.println("\nProcessing Booking Requests...");
        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.poll();
            String roomType = reservation.getRoomType();

            if (inventory.getAvailability(roomType) > 0) {
                String roomId = UUID.randomUUID().toString();

                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomId);

                inventory.decrementAvailability(roomType);

                System.out.println("Confirmed: " + reservation.getGuestName() +
                        " booked " + roomType +
                        " | Room ID: " + roomId);
            } else {
                System.out.println("Failed: " + reservation.getGuestName() +
                        " requested " + roomType +
                        " but no availability.");
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Room IDs: " + entry.getValue());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v6.1");
        System.out.println("Initializing booking service...\n");

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingService bookingService = new BookingService(inventory);


        bookingService.addRequest(new Reservation("Alice", "Single Room"));
        bookingService.addRequest(new Reservation("Bob", "Double Room"));
        bookingService.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingService.addRequest(new Reservation("Diana", "Single Room"));
        bookingService.addRequest(new Reservation("Ethan", "Double Room")); // should fail

        bookingService.processRequests();

        bookingService.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nSystem terminated.");
    }
}