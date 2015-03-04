package org.playground.admin.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by wsantasiero on 10/13/14.
 */
public class ValidationUtils {

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        try {
            //
            // Create InternetAddress object and validated the supplied
            // address which is this case is an email address.
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (AddressException e) {
        }
        return isValid;
    }
}
