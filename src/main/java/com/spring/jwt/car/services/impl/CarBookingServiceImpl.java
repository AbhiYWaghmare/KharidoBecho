package com.spring.jwt.car.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.car.dto.CarBookingDTO;
import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.car.repository.CarBookingRepository;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.car.services.CarBookingService;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.exception.car.*;
import com.spring.jwt.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarBookingServiceImpl implements CarBookingService {

    private final CarBookingRepository carBookingRepository;
    private final CarRepository carRepository;
    private final BuyerRepository buyerRepository;

//    @Transactional
//    @Override
//    public CarBooking createBooking(CarBookingDTO dto) {
//
//        Buyer buyer = buyerRepository.findById(dto.getBuyerId())
//                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id " + dto.getBuyerId()));
//
//        Car car = carRepository.findById(dto.getCarId())
//                .orElseThrow(() -> new CarNotFoundException("Car with ID " + dto.getCarId() + " not found"));
//
//        // ✅ Check if same buyer already booked the same car
//        boolean alreadyBooked = carBookingRepository
//                .existsByBuyer_BuyerIdAndCar_CarId(dto.getBuyerId(), dto.getCarId());
//
//        if (alreadyBooked) {
//            throw new DuplicateBookingException("Buyer has already booked this car");
//        }
//
//        // ✅ Check if car already has a pending booking
//        boolean hasActiveBooking = carBookingRepository
//                .existsByCar_CarIdAndBookingStatus(car.getCarId(), CarBooking.Status.PENDING);
//
//        CarBooking booking = CarBooking.builder()
//                .buyer(buyer)
//                .car(car)
//                .bookingDate(OffsetDateTime.now())
//                .bookingStatus(hasActiveBooking ? CarBooking.Status.PENDING : CarBooking.Status.ACTIVE)
//                .build();
//
//        return carBookingRepository.save(booking);
//    }
@Override
@Transactional
public CarBooking createBooking(CarBookingDTO dto) {

    Buyer buyer = buyerRepository.findById(dto.getBuyerId())
            .orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id " + dto.getBuyerId()));

    Car car = carRepository.findById(dto.getCarId())
            .orElseThrow(() -> new CarNotFoundException("Car with ID " + dto.getCarId() + " not found"));

    // ✅ Check if car is available for booking
    if (car.getStatus() != Car.Status.ACTIVE) {
        throw new DuplicateBookingException("Car with ID " + dto.getCarId() +
                " cannot be booked because it is not active.");
    }

    // ✅ Check if same buyer already booked the same car
    boolean alreadyBooked = carBookingRepository
            .existsByBuyer_BuyerIdAndCar_CarId(dto.getBuyerId(), dto.getCarId());
    if (alreadyBooked) {
        throw new DuplicateBookingException("Buyer has already booked this car");
    }

    // ✅ Check if car already has a pending booking
    boolean hasActiveBooking = carBookingRepository
            .existsByCar_CarIdAndBookingStatus(car.getCarId(), CarBooking.Status.PENDING);

    // ✅ Create booking object
    CarBooking booking = CarBooking.builder()
            .buyer(buyer)
            .car(car)
            .bookingDate(OffsetDateTime.now())
            .bookingStatus(hasActiveBooking ? CarBooking.Status.PENDING : CarBooking.Status.ACTIVE)
            .build();

    // ✅ Add message in conversation JSON format
    if (dto.getMessage() != null && !dto.getMessage().isEmpty()) {
        String conversationJson = String.format(
                "[{\"userId\": %d, \"message\": \"%s\", \"timestamp\": \"%s\", \"senderType\": \"BUYER\"}]",
                buyer.getBuyerId(),
                dto.getMessage().replace("\"", "\\\""),
                java.time.OffsetDateTime.now()
        );
        booking.setConversation(conversationJson);
    } else {
        booking.setConversation("[]");
    }

    // ✅ Save booking
    return carBookingRepository.save(booking);
}





    @Override
    public List<CarBooking> getPendingBookings() {
        return carBookingRepository.findByBookingStatus(CarBooking.Status.PENDING);
    }

    @Override
    @Transactional
    public CarBooking approveBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        // ✅ Mark the booking as completed (SOLD)
        booking.setBookingStatus(CarBooking.Status.SOLD);

        // ✅ Get associated car
        Car car = booking.getCar();

        // ✅ Update car status as DELETED
        car.setStatus(Car.Status.DELETED);

        // ✅ Save both entities
        carRepository.save(car);
        return carBookingRepository.save(booking);
    }


    @Override
    public CarBooking rejectBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        if (booking.getBookingStatus() != CarBooking.Status.PENDING) {
            throw new InvalidBookingOperationException("Only pending bookings can be rejected");
        }

        booking.setBookingStatus(CarBooking.Status.CANCELLED);
        return carBookingRepository.save(booking);
    }
    @Override
    public CarBooking acceptBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        if (booking.getBookingStatus() != CarBooking.Status.PENDING) {
            throw new InvalidBookingOperationException("Only pending bookings can be accepted");
        }

        booking.setBookingStatus(CarBooking.Status.CONFIRMED);
        return carBookingRepository.save(booking);
    }

    @Override
    @Transactional
    public CarBooking addMessage(Long bookingId, CarBookingDTO newMessage) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> conversationList;

            // Check if conversation JSON field is empty or null
            if (booking.getConversation() == null || booking.getConversation().isEmpty()) {
                conversationList = new ArrayList<>();
            } else {
                conversationList = mapper.readValue(
                        booking.getConversation(),
                        new TypeReference<List<Map<String, Object>>>() {}
                );
            }

            // Automatically determine sender type (BUYER or SELLER)
            String senderType = determineSenderType(newMessage.getUserId(), booking);

            // Prepare message object
            Map<String, Object> msg = new LinkedHashMap<>();
            msg.put("userId", newMessage.getUserId());
            msg.put("senderType", senderType);
            msg.put("message", newMessage.getMessage());
            msg.put("timestamp", OffsetDateTime.now().toString());

            // Add message to list
            conversationList.add(msg);

            // Convert list back to JSON and save
            booking.setConversation(mapper.writeValueAsString(conversationList));

            return carBookingRepository.save(booking);

        } catch (Exception e) {
            throw new RuntimeException("Error while adding message: " + e.getMessage());
        }
    }
    // Helper method to determine who sent the message (Buyer or Seller)
    private String determineSenderType(Long senderUserId, CarBooking booking) {
        if (booking.getBuyer() != null &&
                booking.getBuyer().getUser() != null &&
                booking.getBuyer().getUser().getId().equals(senderUserId)) {
            return "BUYER";
        } else if (booking.getCar() != null &&
                booking.getCar().getSeller() != null &&
                booking.getCar().getSeller().getUser() != null &&
                booking.getCar().getSeller().getUser().getId().equals(senderUserId)) {
            return "SELLER";
        } else {
            return "UNKNOWN";
        }
    }






}
