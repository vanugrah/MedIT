package com.medit;


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
 * Created by matt on 4/4/2016.
 */
public class SMSManager {

    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    public static void sendSMSReminder(Patient patient, Appointment appointment) {
        String to = "+1" + patient.phoneNumber;

        SimpleDateFormat dateFormater = new SimpleDateFormat("EEEEE, MMMMM d 'at' h:mm aa");

        String messageText = "Hi! This is a reminder that you have an appointment with " +
                appointment.doctorName + " on " +
                dateFormater.format(appointment.appointmentDate) + ".";

        sendSMS(to, messageText);
    }

    public static void sendSMSConfirmation(Patient patient, Appointment appointment) {
        String to = "+1" + patient.phoneNumber;

        SimpleDateFormat dateFormater = new SimpleDateFormat("EEEEE, MMMMM d 'at' h:mm aa");

        String messageText = "Hi! This is a reminder that you have an appointment with " +
                appointment.doctorName + " on " +
                dateFormater.format(appointment.appointmentDate) + ". INSERT CONFIRMATION LINK HERE.";

        sendSMS(to, messageText);
    }

    private static void sendSMS(String to, String messageText) {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

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
