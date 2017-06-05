/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.util;

/**
 *
 * @author Pang
 */
public class CalBonus {
    public static double calBonus(double amount){
        if(amount < 2000)
            return 0.0;
        return amount * 5/100;
    }
}
