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
public class UserUsedDiscountDTO implements Serializable {

    private UserDTO user;
    private DiscountDTO discount;

    public UserUsedDiscountDTO() {
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public DiscountDTO getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountDTO discount) {
        this.discount = discount;
    }

}
