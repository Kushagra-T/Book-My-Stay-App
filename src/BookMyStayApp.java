import java.util.LinkedList;
import java.util.Queue;

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

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }


    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request added: " + reservation);
    }


    public void displayQueue() {
        System.out.println("\nCurrent Booking Request Queue:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }
}


public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v5.1");
        System.out.println("Initializing booking request queue...\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();


        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room"));


        bookingQueue.displayQueue();

        System.out.println("\nSystem terminated.");
    }
}