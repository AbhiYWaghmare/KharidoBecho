package com.spring.jwt.Bike.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.Bike.Entity.Bike;

import com.spring.jwt.Bike.Entity.Bike_booking;
import com.spring.jwt.Bike.Entity.bikeStatus;
import com.spring.jwt.entity.Buyer;
import com.spring.jwt.exception.Bike.BookingNotFoundException;
import com.spring.jwt.exception.Bike.InvalidBikeData;
import com.spring.jwt.exception.Bike.bikeNotFoundException;
import com.spring.jwt.Bike.Repository.Bike_booking_repository;
import com.spring.jwt.Bike.Repository.bikeRepository;
import com.spring.jwt.Bike.dto.Bike_booking_dto;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.BuyerRepository;
import jakarta.transaction.Transactional;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class Bike_booking_serviceimpl implements Bike_booking_service {

    @Autowired
    private Bike_booking_repository bikeBookingRepository;

    @Autowired
    private bikeRepository bikeRepository;
    @Autowired
    private BuyerRepository buyerRepository;



    @Override
    public List<Bike_booking> getBookings() {
        //return bikeBookingRepository.findByStatus(Bike_booking.BookingStatus.PENDING);
    return  bikeBookingRepository.findAll();
    }

    public Bike_booking getBookingById(Long bookingid) {
        return bikeBookingRepository.findById(bookingid)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingid));
    }

    @Override
    @Transactional
    public Bike_booking createBooking(Bike_booking_dto bikeBookingDto) {
        Bike bike = bikeRepository.findById(bikeBookingDto.getBikeId())
                .orElseThrow(() -> new bikeNotFoundException("Bike not found with ID: " + bikeBookingDto.getBikeId()));

        Buyer buyer = buyerRepository.findById(bikeBookingDto.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with ID: " + bikeBookingDto.getBuyerId()));
         // this is cheak the status if the bike that is available or not
        if (!(bike.getStatus() == bikeStatus.ACTIVE ||
                bike.getStatus() == bikeStatus.AVAILABLE ||
                bike.getStatus() == bikeStatus.NEW)) {
            throw new InvalidBikeData("Bike with ID " + bikeBookingDto.getBikeId() +
                    " cannot be booked because it is not active, available, or new.");
        }



        // this is method for cheak if one booking is present with same bike and same buyer then throw error
        List<Bike_booking> existingBookings = bikeBookingRepository.findByBikeAndBuyer(bike, buyer);
        if (!existingBookings.isEmpty()) {
            throw new ResourceNotFoundException("You already created a booking for this bike.");
        }
        Bike_booking booking = new Bike_booking();
        booking.setBike(bike);
        booking.setBuyer(buyer);
        booking.setOnDate(LocalDate.now());
        booking.setStatus(Bike_booking.BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());


        if (bikeBookingDto.getMessage() != null && !bikeBookingDto.getMessage().isEmpty()) {
            String conversationJson = String.format(
                    "[{\"userId\": %d, \"message\": \"%s\", \"timestamp\": \"%s\", \"senderType\": \"BUYER\"}]",
                    buyer.getBuyerId(),
                    bikeBookingDto.getMessage().replace("\"", "\\\""),
                    java.time.OffsetDateTime.now()
            );
            booking.setConversation(conversationJson);
        } else {
            booking.setConversation("[]");
        }

        return bikeBookingRepository.save(booking);
    }













    @Override
    public Bike_booking completeBooking(Long bookingId) {
        //  Find the booking by ID
        Bike_booking booking = bikeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != Bike_booking.BookingStatus.ACCEPTED &&
                booking.getStatus() != Bike_booking.BookingStatus.IN_NEGOTIATION) {
            throw new ResourceNotFoundException("Only ACCEPTED and IN_NEGOTIATION bookings can be Sold.");
        }

        // Mark the booking as completed
        booking.setStatus(Bike_booking.BookingStatus.SOLD);

        Bike bike = booking.getBike();

        // Update bike status as deleted
        bike.setStatus(bikeStatus.DELETED);

        bikeRepository.save(bike);


        //  Save and return updated booking
        return bikeBookingRepository.save(booking);
    }


@Override
public Bike_booking rejectBooking(Long bookingId) {
    Bike_booking booking = bikeBookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

    // Allow rejection only if the booking is APPROVED
    if (booking.getStatus() != Bike_booking.BookingStatus.ACCEPTED&&
            booking.getStatus() != Bike_booking.BookingStatus.IN_NEGOTIATION) {
        throw new ResourceNotFoundException("Only ACCEPTED & IN_NEGOTIATION bookings can be rejected.");
    }

    // Set status
    booking.setStatus(Bike_booking.BookingStatus.PENDING);

    return bikeBookingRepository.save(booking);
}

    @Override
    public Bike_booking deleteBooking(Long bookingId) {
        // First, check if booking exists
        Bike_booking booking = bikeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        // Then delete it
        bikeBookingRepository.delete(booking);

        // Optionally, return deleted booking info
        return booking;
    }


@Override
@Transactional
public Bike_booking addMessage(Long bookingId, Bike_booking_dto newMessage) {
    Bike_booking booking = bikeBookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

    try {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> conversationList;

        if (booking.getConversation() == null || booking.getConversation().isEmpty()) {
            conversationList = new ArrayList<>();
        } else {
            conversationList = mapper.readValue(
                    booking.getConversation(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );
        }

        //  Automatically determine sender type based on userId
        String senderType = determineSenderType(newMessage.getUserId(), booking);

        Map<String, Object> msg = new LinkedHashMap<>();
        msg.put("userId", newMessage.getUserId());
        msg.put("senderType", senderType);
        msg.put("message", newMessage.getMessage());
        msg.put("timestamp", OffsetDateTime.now().toString());

        conversationList.add(msg);
        booking.setConversation(mapper.writeValueAsString(conversationList));

        return bikeBookingRepository.save(booking);

    } catch (Exception e) {
        throw new RuntimeException("Error while adding message: " + e.getMessage());
    }
}





// helper method to determine the message sender
private String determineSenderType(Long senderUserId, Bike_booking booking) {
    if (booking.getBuyer() != null &&
            booking.getBuyer().getUser() != null &&
            booking.getBuyer().getUser().getId().equals(senderUserId)) {
        return "BUYER";
    } else if (booking.getBike() != null &&
            booking.getBike().getSeller() != null &&
            booking.getBike().getSeller().getUser() != null &&
            booking.getBike().getSeller().getUser().getId().equals(senderUserId)) {
        return "SELLER";
    } else {
        return "UNKNOWN";
    }
}

    @Override
    public Bike_booking updateBookingStatus(Long bookingId, Bike_booking.BookingStatus newStatus) {
        Bike_booking booking = bikeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != Bike_booking.BookingStatus.PENDING) {
            throw new ResourceNotFoundException("Only Pending bookings can be updated.");
        }

        if (newStatus != Bike_booking.BookingStatus.ACCEPTED
                && newStatus != Bike_booking.BookingStatus.IN_NEGOTIATION
               // && newStatus != Bike_booking.BookingStatus.SOLD
        ) {
            throw new IllegalArgumentException("Invalid status update. Allowed: ACCEPTED or IN_NEGOTIATION.");
        }


        booking.setStatus(newStatus);
        return bikeBookingRepository.save(booking);
    }





}
