/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.utils;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 *
 * @author hoang
 */
public class Tool {

    public static boolean check(String value, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(value).find();
    }

    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");
}
