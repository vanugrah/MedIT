package com.medit;


import com.medit.db.Appointment;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides an interface for other parts of the Server to send SMS messages.
 *
 * This implementation uses the Twilio service.
 *
 * Created by matt on 4/4/2016.
 */
public class SMSManager {

    // This should be filled with your Twilio SID
    public static final String ACCOUNT_SID = "";
    // This should be filled with your Twilio authorization token
    public static final String AUTH_TOKEN = "";

    /**
     * Builds a reminder message and sends it to the phone number for the user associated with the given appointment.
     *
     * @param appointment
     */
    public static void sendSMSReminder(Appointment appointment) {
        String to = "+1" + appointment.user.phoneNumber;

        SimpleDateFormat dateFormater = new SimpleDateFormat("EEEEE, MMMMM d 'at' h:mm aa");

        String messageText = "Hello! This is a reminder from MedIT that you have an appointment with Dr. " +
                appointment.doctor.firstName + " " + appointment.doctor.lastName + " at " +
                appointment.clinic.hospital.name + " scheduled for " +
                dateFormater.format(appointment.date) + ".";

        sendSMS(to, messageText);
    }

    /**
     * Builds a confirmation message and sends it to the phone number for the user associated with the given appointment.
     *
     * @param appointment
     */
    public static void sendSMSConfirmation(Appointment appointment) {
        String to = "+1" + appointment.user.phoneNumber;

        SimpleDateFormat dateFormater = new SimpleDateFormat("EEEEE, MMMMM d 'at' h:mm aa");

        String messageText = "Hello! This is a reminder from MedIT that you have an appointment with Dr. " +
                appointment.doctor.firstName + " " + appointment.doctor.lastName + " at " +
                appointment.clinic.hospital.name + " scheduled for " +
                dateFormater.format(appointment.date) + ". Click here to confirm: http://127.0.0.1/Confirm?ID=" + appointment.appointmentID + ".";

        sendSMS(to, messageText);
    }

    /**
     * Sends the given message to the given phone number as a text message.
     *
     * @param to The phone number to send the message to.
     * @param messageText The text of the message to send.
     */
    private static void sendSMS(String to, String messageText) {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("Body", messageText));
        params.add(new BasicNameValuePair("To", to));
        params.add(new BasicNameValuePair("From", "+16786078546"));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        Message message = null;
        try {
            message = messageFactory.create(params);
        } catch (TwilioRestException e) {
            e.printStackTrace();
        }
    }

}
