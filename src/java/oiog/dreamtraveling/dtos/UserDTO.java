/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.dtos;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class UserDTO implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UserDTO.class);

    public static final String REGEX_USERNAME = "^[a-zA-Z0-9._]{5,20}$";
    public static final String REGEX_FULLNAME
            = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ"
            + "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ"
            + "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s.]{3,30}$";
    public static final String REGEX_PASSWORD = "^[A-z0-9]{6,30}$";

    public static final String WARNING_VALID_USERNAME = "Username must have between 5 and 20 characters";
    public static final String WARNING_VALID_FULLNAME = "Invalid Name";
    public static final String WARNING_VALID_PASSWORD = "Password must have between 6 and 30 characters";

    private String username;
    private String password;
    private String name;
    private String role;
    private String status;

    public UserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getValidSHA256Password(String iPassword) {
//        Pattern pattern_password = Pattern.compile(REGEX_PASSWORD, Pattern.CASE_INSENSITIVE);
//        if (!pattern_password.matcher(iPassword).find()) {
//            return null;
//        }
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(iPassword.getBytes(StandardCharsets.UTF_8));
            result = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
