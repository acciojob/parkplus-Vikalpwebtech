package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Optional<Reservation> optionalReservation = reservationRepository2.findById(reservationId);

        Reservation reservation = optionalReservation.get();

        //Calculate the bill
        int bill = reservation.getNumberOfHours() * reservation.getSpot().getPricePerHour();

        if (amountSent < bill) throw new Exception("Insufficient Amount");
        if (!(mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi"))){
            throw new Exception("Payment mode not detected");
        }

        //Now if everything fine needs to upadate payment
        Payment payment = new Payment();
        payment.setPaymentMode(getPaymentmode(mode));
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);
        reservation.setPayment(payment);

        //Saving to db

        reservationRepository2.save(reservation);

        return payment;
    }
    public static PaymentMode getPaymentmode(String mode){
        if (mode.equalsIgnoreCase("cash")){
            return PaymentMode.CASH;
        }
        else if (mode.equalsIgnoreCase("card")){
            return PaymentMode.CARD;
        }
        return PaymentMode.UPI;
    }
}
