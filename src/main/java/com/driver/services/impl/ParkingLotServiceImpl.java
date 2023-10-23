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
            spot.setParkingLot(parkingLot);

            parkingLot.getSpotList().add(spot);
            //Now save this to db
            parkingLotRepository1.save(parkingLot);
            return spot;
        }
        return null;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
       Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
       ParkingLot parkingLot = optionalParkingLot.get();
       Spot spot1 = null;

       for (Spot spot :parkingLot.getSpotList()){
           if (spot.getId()==spotId){
               spot.setPricePerHour(pricePerHour);
               spot1 = spot;
               break;
           }
       }
       spotRepository1.save(spot1);
       return spot1;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
