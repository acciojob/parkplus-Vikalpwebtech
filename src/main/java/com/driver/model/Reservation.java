package com.driver.model;

import org.springframework.boot.autoconfigure.security.SecurityProperties;

import javax.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int numberOfHours;

    @ManyToOne
    @JoinColumn
    Spot spot;

    @ManyToOne
    @JoinColumn
    User user;

    @OneToOne(mappedBy = "reservation",cascade = CascadeType.ALL)
    Payment payment;

    public Reservation(){}

    public Reservation(int id, int numberOfHours, Spot spot, User user, Payment payment) {
        this.id = id;
        this.numberOfHours = numberOfHours;
        this.spot = spot;
        this.user = user;
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
