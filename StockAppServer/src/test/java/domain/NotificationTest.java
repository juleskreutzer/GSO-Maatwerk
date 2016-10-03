package domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 30-09-16
 * |
 * | Project Info:
 * | Project Name: StockAppServer
 * | Project Package Name: domain
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class NotificationTest {

    Notification noti;
    String c = "FB";
    String e = "jules@nujules.nl";
    double min = 0.5;
    double max = 1.5;

    @Before
    public void setup() {
        noti = new Notification(c, e, min, max);
    }

    @Test
    public void ctor_test() throws Exception {
        String code = "AAPL";
        String email = "juleskreutzer@me.com";
        double minimum = 1.0;
        double maximum = 2.0;

        Notification notification = new Notification(code, email, minimum, maximum);
        assertEquals(notification.getCode(), code);
        assertEquals(notification.getEmail(), email);
        assertEquals(notification.getMaximum(), maximum, 0.0);
        assertEquals(notification.getMinimum(), minimum, 0.0);

        try{
            Notification n = new Notification("", email, minimum, maximum);
            fail("Not allowed to set an empty code");
        } catch (IllegalArgumentException e) { }

        try{
            Notification n = new Notification(code, "", minimum, maximum);
            fail("Not allowed to set empty mail address");
        } catch (IllegalArgumentException e) { }

        try{
            Notification n = new Notification(code, email, maximum, minimum);
            fail("Minimum can't be higher than maximum");
        } catch (IllegalArgumentException e) { }

    }

    @Test
    public void getter_setter_test() throws Exception {
        String code = "AAPL";
        String email = "juleskreutzer@me.com";
        double minimum = 1.0;
        double maximum = 2.0;
        double minimumWrong = 3.0;
        double maximumWrong = -1.0;


        // Test code
        assertEquals(noti.getCode(), c);
        noti.setCode(code);
        assertEquals(noti.getCode(), code);

        try{
            noti.setCode("");
            fail("Not allowed to set an empty code");
        } catch (IllegalArgumentException e) { }

        // Test Email
        assertEquals(noti.getEmail(), e);
        noti.setEmail(email);
        assertEquals(noti.getEmail(), email);

        try{
            noti.setEmail("");
            fail("Not allowed to set empty email");
        } catch (IllegalArgumentException e) { }

        // Test minimum
        assertEquals(noti.getMinimum(), min, 0.0);
        noti.setMinimum(minimum);
        assertEquals(noti.getMinimum(), minimum, 0.0);

        try{
            noti.setMinimum(minimumWrong);
            fail("Not allowed to set a minimum that's higher than the maximum");
        } catch (IllegalArgumentException e) { }

        // Test maximum
        assertEquals(noti.getMaximum(), max, 0.0);
        noti.setMaximum(maximum);
        assertEquals(noti.getMaximum(), maximum, 0.0);

        try{
            noti.setMaximum(maximumWrong);
            fail("Not allowed to set a maximum that's lower than the minimum");
        } catch (IllegalArgumentException e) { }

    }


    @Test
    public void sendMail() throws Exception {

    }

}