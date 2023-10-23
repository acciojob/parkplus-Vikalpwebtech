package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        
        parkingLotRepository1.save(parkingLot);

        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
        
        if (optionalParkingLot.isPresent()){

            //Fetching parking lot
            ParkingLot parkingLot = optionalParkingLot.get();
            
            Spot spot = new Spot();
            if (numberOfWheels <= 2){
                spot.setSpotType(SpotType.TWO_WHEELER);
            } else if (numberOfWheels <=4) {
                spot.setSpotType(SpotType.FOUR_WHEELER);
            }
            else {
                spot.setSpotType(SpotType.OTHERS);
            }

            spot.setOccupied(false);
            spot.setPricePerHour(pricePerHour);
            spot.setParkinglot(parkingLot);

            parkingLot.getSpotList().add(spot);
            //Now save this to db
            parkingLotRepository1.save(parkingLot);
            //check
            spotRepository1.save(spot);
            return spot;
        }
        return null;
    }

    @Override
    public void deleteSpot(int spotId) {
        Optional<Spot> optionalSpot = spotRepository1.findById(spotId);

        if (optionalSpot.isPresent()){
            Spot spot = optionalSpot.get();

            //Removing from spotlist
            ParkingLot parkingLot = spot.getParkinglot();
            parkingLot.getSpotList().remove(spot);

            parkingLotRepository1.save(parkingLot);

            spotRepository1.delete(spot);
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
        ParkingLot parkingLot = optionalParkingLot.get();
        Spot spot = parkingLot.getSpotList().get(spotId);

        if (parkingLot != null && spot != null){
            spot.setPricePerHour(pricePerHour);
            spotRepository1.save(spot);
        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);

        if (optionalParkingLot.isPresent()){
            ParkingLot parkingLot = optionalParkingLot.get();
            parkingLotRepository1.delete(parkingLot);
        }
    }
}
