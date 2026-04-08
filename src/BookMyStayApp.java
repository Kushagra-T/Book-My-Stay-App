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

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored in history: " + reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void displayBookingHistory() {
        System.out.println("\nBooking History Report:");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }
    }

    public void generateSummaryReport() {
        System.out.println("\nSummary Report by Room Type:");
        Map<String, Integer> summary = new HashMap<>();
        for (Reservation r : history.getAllReservations()) {
            summary.put(r.getRoomType(), summary.getOrDefault(r.getRoomType(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Total Bookings: " + entry.getValue());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v8.1");
        System.out.println("Initializing booking history and reporting...\n");

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Suite Room");
        Reservation r3 = new Reservation("Charlie", "Single Room");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        BookingReportService reportService = new BookingReportService(history);

        reportService.displayBookingHistory();

        reportService.generateSummaryReport();

        System.out.println("\nSystem terminated.");
    }
}