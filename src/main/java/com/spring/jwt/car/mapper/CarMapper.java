package com.spring.jwt.car.mapper;

import com.spring.jwt.car.dto.CarRequestDTO;
import com.spring.jwt.car.dto.CarResponseDTO;
import com.spring.jwt.car.entity.Car;
import com.spring.jwt.car.entity.CarImage;

import java.util.Collections;
import java.util.stream.Collectors;

public class CarMapper {

    public static CarResponseDTO toDTO(Car car) {
        if (car == null) return null;

        CarResponseDTO dto = new CarResponseDTO();

        dto.setCarId(car.getCarId());
        dto.setTitle(car.getTitle());
        dto.setDescription(car.getDescription());
        dto.setPrice(car.getPrice());
        dto.setCondition(car.getCondition() != null ? car.getCondition().name() : null);
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setColor(car.getColor());
        dto.setYearOfPurchase(car.getYearOfPurchase());
        dto.setFuelType(car.getFuelType() != null ? car.getFuelType().name() : null);
        dto.setTransmission(car.getTransmission() != null ? car.getTransmission().name() : null);
        dto.setStatus(car.getStatus() != null ? car.getStatus().name() : null);
        dto.setCreatedAt(car.getCreatedAt());
        dto.setUpdatedAt(car.getUpdatedAt());
        dto.setSellerId(car.getSeller() != null ? car.getSeller().getSellerId() : null);

        // Features
        dto.setAirbag(car.getAirbag());
        dto.setAbs(car.getAbs());
        dto.setButtonStart(car.getButtonStart());
        dto.setSunroof(car.getSunroof());
        dto.setChildSafetyLocks(car.getChildSafetyLocks());
        dto.setAcFeature(car.getAcFeature());
        dto.setMusicFeature(car.getMusicFeature());
        dto.setCarInsurance(car.getCarInsurance());
        dto.setCarInsuranceDate(car.getCarInsuranceDate());
        dto.setCarInsuranceType(car.getCarInsuranceType());
        dto.setPowerWindowFeature(car.getPowerWindowFeature());
        dto.setRearParkingCameraFeature(car.getRearParkingCameraFeature());
        dto.setKmDriven(car.getKmDriven());
        dto.setNumberOfOwners(car.getNumberOfOwners());
        dto.setCity(car.getCity());
        dto.setState(car.getState());
        dto.setVariant(car.getVariant());
        dto.setNegotiable(car.getNegotiable());
        dto.setAddress(car.getAddress());
        dto.setPincode(car.getPincode());



        // Images
        dto.setImages(
                car.getImages() == null ? Collections.emptyList() :
                        car.getImages().stream().map(CarImage::getImageUrl).collect(Collectors.toList())
        );

        return dto;
    }

    public static void updateFromRequest(Car car, CarRequestDTO req) {
        if (req.getTitle() != null) car.setTitle(req.getTitle());
        if (req.getDescription() != null) car.setDescription(req.getDescription());
        if (req.getPrice() != null) car.setPrice(req.getPrice());
        if (req.getCondition() != null) car.setCondition(Car.Condition.valueOf(req.getCondition().toUpperCase()));
        if (req.getBrand() != null) car.setBrand(req.getBrand());
        if (req.getModel() != null) car.setModel(req.getModel());
        if (req.getColor() != null) car.setColor(req.getColor());
        if (req.getYearOfPurchase() != null) car.setYearOfPurchase(req.getYearOfPurchase());
        if (req.getFuelType() != null) car.setFuelType(Car.FuelType.valueOf(req.getFuelType().toUpperCase()));
        if (req.getTransmission() != null)
            car.setTransmission(Car.Transmission.valueOf(req.getTransmission().toUpperCase()));

        if (req.getAirbag() != null) car.setAirbag(req.getAirbag());
        if (req.getAbs() != null) car.setAbs(req.getAbs());
        if (req.getButtonStart() != null) car.setButtonStart(req.getButtonStart());
        if (req.getSunroof() != null) car.setSunroof(req.getSunroof());
        if (req.getChildSafetyLocks() != null) car.setChildSafetyLocks(req.getChildSafetyLocks());
        if (req.getAcFeature() != null) car.setAcFeature(req.getAcFeature());
        if (req.getMusicFeature() != null) car.setMusicFeature(req.getMusicFeature());
        if (req.getCarInsurance() != null) car.setCarInsurance(req.getCarInsurance());
        if (req.getCarInsuranceDate() != null) car.setCarInsuranceDate(req.getCarInsuranceDate());
        if (req.getCarInsuranceType() != null) car.setCarInsuranceType(req.getCarInsuranceType());
        if (req.getPowerWindowFeature() != null) car.setPowerWindowFeature(req.getPowerWindowFeature());
        if (req.getRearParkingCameraFeature() != null) car.setRearParkingCameraFeature(req.getRearParkingCameraFeature());
        if (req.getKmDriven() != null) car.setKmDriven(req.getKmDriven());
        if (req.getNumberOfOwners() != null) car.setNumberOfOwners(req.getNumberOfOwners());
        if (req.getCity() != null) car.setCity(req.getCity());
        if (req.getState() != null) car.setState(req.getState());
        if (req.getVariant() != null) car.setVariant(req.getVariant());
        if (req.getNegotiable() != null) car.setNegotiable(req.getNegotiable());
        if (req.getAddress() != null) car.setAddress(req.getAddress());
        if (req.getPincode() != null) car.setPincode(req.getPincode());

    }
}
