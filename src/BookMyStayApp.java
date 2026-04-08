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

class Service {
    private String serviceName;
    private double serviceCost;

    public Service(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + serviceCost + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addServicesToReservation(Reservation reservation, List<Service> services) {
        reservationServices.put(reservation.getReservationId(), services);
        System.out.println("Services added for Reservation ID: " + reservation.getReservationId());
    }

    public void displayServices(Reservation reservation) {
        List<Service> services = reservationServices.getOrDefault(reservation.getReservationId(), new ArrayList<>());
        System.out.println("\nReservation Details: " + reservation);
        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            System.out.println("Selected Add-On Services:");
            double totalCost = 0;
            for (Service s : services) {
                System.out.println("- " + s);
                totalCost += s.getServiceCost();
            }
            System.out.println("Total Additional Cost: ₹" + totalCost);
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System v7.1");
        System.out.println("Initializing add-on service selection...\n");

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Suite Room");

        Service breakfast = new Service("Breakfast", 500.0);
        Service spa = new Service("Spa Access", 1500.0);
        Service airportPickup = new Service("Airport Pickup", 1000.0);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        serviceManager.addServicesToReservation(r1, Arrays.asList(breakfast, airportPickup));
        serviceManager.addServicesToReservation(r2, Arrays.asList(spa));

        serviceManager.displayServices(r1);
        serviceManager.displayServices(r2);

        System.out.println("\nSystem terminated.");
    }
}