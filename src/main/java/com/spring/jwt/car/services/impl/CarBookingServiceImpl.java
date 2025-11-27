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
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.exception.car.*;
import com.spring.jwt.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    @Transactional
    public CarBooking createBooking(CarBookingDTO dto) {

        // 1️⃣ Fetch Buyer
        Buyer buyer = buyerRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new BuyerNotFoundException(
                        "Buyer not found with id " + dto.getBuyerId()
                ));

        // 2️⃣ Fetch Car
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new CarNotFoundException(
                        "Car with ID " + dto.getCarId() + " not found"
                ));

        if (!(car.getStatus() == Car.Status.ACTIVE )) {

            throw new DuplicateBookingException(
                    "Car with ID " + dto.getCarId() +
                            " cannot be booked because it is not active, available, or new."
            );
        }


        boolean alreadyBooked = carBookingRepository
                .existsByBuyer_BuyerIdAndCar_CarId(dto.getBuyerId(), dto.getCarId());

        if (alreadyBooked) {
            throw new DuplicateBookingException(
                    "You already created a booking for this car."
            );
        }


        // 5️⃣ Create Car Booking object
        CarBooking booking = new CarBooking();
        booking.setBuyer(buyer);
        booking.setCar(car);
        booking.setBookingDate(OffsetDateTime.now());
        booking.setBookingStatus(CarBooking.Status.PENDING);  // bike logic प्रमाणे default PENDING


        // 6️⃣ Add message to conversation JSON
        if (dto.getMessage() != null && !dto.getMessage().isEmpty()) {

            String conversationJson = String.format(
                    "[{\"userId\": %d, \"message\": \"%s\", \"timestamp\": \"%s\", \"senderType\": \"BUYER\"}]",
                    buyer.getBuyerId(),
                    dto.getMessage().replace("\"", "\\\""),
                    OffsetDateTime.now()
            );

            booking.setConversation(conversationJson);

        } else {
            booking.setConversation("[]");
        }

        // 7️⃣ Save and return
        return carBookingRepository.save(booking);
    }



    @Override
    public List<CarBooking> getPendingBookings() {
        return carBookingRepository.findByBookingStatus(CarBooking.Status.PENDING);
    }

    @Transactional
    @Override
    public CarBooking approveBooking(Long bookingId) {

        // Find the booking by ID
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        // Check valid statuses before selling
        if (booking.getBookingStatus() != CarBooking.Status.CONFIRMED &&
                booking.getBookingStatus() != CarBooking.Status.PENDING) {

            throw new ResourceNotFoundException("Only CONFIRMED and PENDING bookings can be Sold.");
        }

        // Mark booking as SOLD
        booking.setBookingStatus(CarBooking.Status.SOLD);

        // Get associated car
        Car car = booking.getCar();

        // Update car status to DELETED
        car.setStatus(Car.Status.DELETED);

        // Save car
        carRepository.save(car);

        // Save booking
        return carBookingRepository.save(booking);
    }



    @Override
    public CarBooking rejectBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id " + bookingId));

        if (booking.getBookingStatus() != CarBooking.Status.CONFIRMED) {
            throw new InvalidBookingOperationException("Only confirmed bookings can be rejected");
        }

        booking.setBookingStatus(CarBooking.Status.PENDING);
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
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> conversationList;

            // If conversation JSON is empty
            if (booking.getConversation() == null || booking.getConversation().isEmpty()) {
                conversationList = new ArrayList<>();
            } else {
                conversationList = mapper.readValue(
                        booking.getConversation(),
                        new TypeReference<List<Map<String, Object>>>() {}
                );
            }

            // Automatically determine sender
            String senderType = determineSenderType(newMessage.getUserId(), booking);

            // Prepare new message
            Map<String, Object> msg = new LinkedHashMap<>();
            msg.put("userId", newMessage.getUserId());
            msg.put("senderType", senderType);
            msg.put("message", newMessage.getMessage());
            msg.put("timestamp", OffsetDateTime.now().toString());

            // Add message to conversation
            conversationList.add(msg);

            // Convert back to JSON
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
