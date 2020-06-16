/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.dtos;

import java.io.Serializable;

/**
 *
 * @author hoang
 */
public class BookingDetailsDTO implements Serializable{
    private BookingDTO booking;
    private TourDTO tour;
    private int amount;

    public BookingDetailsDTO() {
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public TourDTO getTour() {
        return tour;
    }

    public void setTour(TourDTO tour) {
        this.tour = tour;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
