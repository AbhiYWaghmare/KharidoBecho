package com.spring.jwt.car.services.impl;

import com.spring.jwt.car.dto.CarBookingDTO;
import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.entity.CarBooking;
import com.spring.jwt.car.repository.CarBookingRepository;
import com.spring.jwt.car.repository.CarRepository;
import com.spring.jwt.car.services.CarBookingService;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.entity.Seller;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.exception.car.*;
import com.spring.jwt.repository.BuyerRepository;
import com.spring.jwt.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CarBookingServiceImpl implements CarBookingService {

    private final CarBookingRepository carBookingRepository;
    private final CarRepository carRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    // -----------------------------------------------------
    //  CREATE BOOKING  (SAVE FIRST MESSAGE)
    // -----------------------------------------------------
    @Override
    @Transactional
    public CarBooking createBooking(CarBookingDTO dto) {

        Buyer buyer = buyerRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found"));

        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new CarNotFoundException("Car not found"));

        Seller seller = car.getSeller();

        if (car.getStatus() != Car.Status.ACTIVE) {
            throw new DuplicateBookingException("Car is not active");
        }

        boolean alreadyBooked = carBookingRepository
                .existsByBuyer_BuyerIdAndCar_CarId(dto.getBuyerId(), dto.getCarId());

        if (alreadyBooked) {
            throw new DuplicateBookingException("You already created a booking");
        }

        CarBooking booking = new CarBooking();
        booking.setBuyer(buyer);
        booking.setCar(car);
        booking.setSeller(seller);
        booking.setBookingDate(OffsetDateTime.now());
        booking.setBookingStatus(CarBooking.Status.PENDING);


        List<Map<String, Object>> conversation = new ArrayList<>();

        if (dto.getMessage() != null && !dto.getMessage().isEmpty()) {

            Map<String, Object> msg = new LinkedHashMap<>();
            msg.put("userId", buyer.getUser().getId());
            msg.put("senderType", "BUYER");
            msg.put("message", dto.getMessage());
            msg.put("timestamp", OffsetDateTime.now().toString());

            conversation.add(msg);
        }

        booking.setConversation(conversation);

        return carBookingRepository.save(booking);
    }


    // -----------------------------------------------------
    //  ADD CHAT MESSAGE
    // -----------------------------------------------------
    @Override
    @Transactional
    public CarBooking addMessage(Long bookingId, Long userId, String message) {

<<<<<<< HEAD
    // -----------------------------------------------------
    //  ADD CHAT MESSAGE
    // -----------------------------------------------------
    @Override
    @Transactional
    public CarBooking addMessage(Long bookingId, CarBookingDTO newMessage) {

        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        List<Map<String, Object>> conversation = booking.getConversation();
        if (conversation == null) conversation = new ArrayList<>();

        String senderType = determineSenderType(newMessage.getUserId(), booking);

        Map<String, Object> msg = new LinkedHashMap<>();
        msg.put("userId", newMessage.getUserId());
        msg.put("senderType", senderType);
        msg.put("message", newMessage.getMessage());
        msg.put("timestamp", OffsetDateTime.now().toString());

        conversation.add(msg);

        booking.setConversation(conversation);

        return carBookingRepository.save(booking);
    }

    private String determineSenderType(Long senderUserId, CarBooking booking) {

        if (booking.getBuyer() != null &&
                booking.getBuyer().getUser().getId().equals(senderUserId))
            return "BUYER";

        if (booking.getCar() != null &&
                booking.getCar().getSeller() != null &&
                booking.getCar().getSeller().getUser().getId().equals(senderUserId))
            return "SELLER";
=======
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        List<Map<String, Object>> oldConversation = booking.getConversation();
        List<Map<String, Object>> newConversation = new ArrayList<>();

        if (oldConversation != null) {
            newConversation.addAll(oldConversation);
        }

        String senderType = determineSenderType(userId, booking);

        Map<String, Object> msg = new LinkedHashMap<>();
        msg.put("userId", userId);
        msg.put("senderType", senderType);
        msg.put("message", message);
        msg.put("timestamp", OffsetDateTime.now().toString());

        newConversation.add(msg);

        booking.setConversation(newConversation);

        return carBookingRepository.save(booking);
    }
    private String determineSenderType(Long senderUserId, CarBooking booking) {

        if (booking.getBuyer() != null &&
                booking.getBuyer().getUser().getId().equals(senderUserId)) {
            return "BUYER";
        }

        if (booking.getCar() != null &&
                booking.getCar().getSeller() != null &&
                booking.getCar().getSeller().getUser().getId().equals(senderUserId)) {
            return "SELLER";
        }
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d

        return "UNKNOWN";
    }

<<<<<<< HEAD
=======




>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d
    // -----------------------------------------------------
    //  OTHER METHODS (NO CHANGE)
    // -----------------------------------------------------
    @Override
    public List<CarBooking> getPendingBookings() {
        return carBookingRepository.findByBookingStatus(CarBooking.Status.PENDING);
    }

    @Override
    public CarBooking acceptBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (booking.getBookingStatus() != CarBooking.Status.PENDING)
            throw new InvalidBookingOperationException("Only pending bookings can be accepted");

        booking.setBookingStatus(CarBooking.Status.CONFIRMED);
        return carBookingRepository.save(booking);
    }

    @Override
    public CarBooking approveBooking(Long bookingId) {
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.setBookingStatus(CarBooking.Status.SOLD);
        return carBookingRepository.save(booking);
    }

    @Override
    public CarBooking rejectBooking(Long bookingId) {
<<<<<<< HEAD

        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (booking.getBookingStatus() != CarBooking.Status.PENDING
                && booking.getBookingStatus() != CarBooking.Status.CONFIRMED) {

            throw new InvalidBookingOperationException(
                    "Only PENDING or CONFIRMED bookings can be rejected"
            );
        }

        booking.setBookingStatus(CarBooking.Status.REJECTED);

        return carBookingRepository.save(booking);
    }



=======
        CarBooking booking = carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        booking.setBookingStatus(CarBooking.Status.PENDING);
        return carBookingRepository.save(booking);
    }

>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d
    @Override
    public List<CarBooking> getBookingsByBuyerId(Long buyerId) {
        return carBookingRepository.findByBuyer_BuyerId(buyerId);
    }

    @Override
    public List<CarBooking> getBookingsBySellerId(Long sellerId) {
        return carBookingRepository.findBySeller_SellerId(sellerId);
    }

    @Override
    public CarBooking getBookingDetails(Long bookingId) {
        return carBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }
}
