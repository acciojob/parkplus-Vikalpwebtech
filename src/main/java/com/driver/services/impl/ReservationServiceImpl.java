package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;

    private int getWheelerType(SpotType spotType){
        if(spotType.equals(SpotType.TWO_WHEELER)){
            return 2;
        } else if (spotType.equals(SpotType.FOUR_WHEELER)) {
            return 4;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Optional<User> optionalUser = userRepository3.findById(userId);

        if (!optionalUser.isPresent()){
            throw new Exception("Cannot make reservation");
        }
        User user = optionalUser.get();

        Optional<ParkingLot> optionalParkingLot = parkingLotRepository3.findById(parkingLotId);

        if (!optionalParkingLot.isPresent()){
            throw new Exception("Cannot make reservation");
        }
        ParkingLot parkingLot = optionalParkingLot.get();

        //Identify the spottype acc to wheel
        SpotType curspottype = null;

        if(numberOfWheels == 2){
            curspottype = SpotType.TWO_WHEELER;
        }
        else if (numberOfWheels == 4){
            curspottype = SpotType.FOUR_WHEELER;
        }
        else {
            curspottype = SpotType.OTHERS;
        }

        int price = Integer.MAX_VALUE;
        Spot reservedSpot = null;

        for (Spot spot:parkingLot.getSpotList()){
            if (!spot.getOccupied()){
                int priceperhour = spot.getPricePerHour();
                int wheels = getWheelerType(spot.getSpotType());

                if (spot.getPricePerHour() < price && spot.getSpotType().equals(SpotType.valueOf(String.valueOf(curspottype)))){
                    price = spot.getPricePerHour();
                    reservedSpot = spot;
                }
            }
        }

        if (reservedSpot == null){
            throw new Exception("Cannot make reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        user.getReservationList().add(reservation);
        reservation.setUser(user);
        reservedSpot.setOccupied(true);
        reservedSpot.getReservationList().add(reservation);
        reservation.setSpot(reservedSpot);

        parkingLot.getSpotList().add(reservedSpot);

        userRepository3.save(user);
        spotRepository3.save(reservedSpot);
        return reservation;
    }
}
