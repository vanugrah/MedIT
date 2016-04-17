package com.medit;

import com.medit.db.Appointment;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
 * Created by matt on 4/4/2016.
 */
public class EmailManager {

    static public void sendEmailReminder(Appointment appointment) {

        String to = appointment.user.emailAddress;

        SimpleDateFormat dateFormater = new SimpleDateFormat("EEEEE, MMMMM d 'at' h:mm aa");

        String messageText = "Hello\n\n" +
                "This is a reminder from MedIT that you have an appointment with Dr. " +
                appointment.doctor.firstName + " " + appointment.doctor.lastName + " at " +
                appointment.clinic.hospital.name + " scheduled for " +
                dateFormater.format(appointment.date) + ".";

        String subject = appointment.clinic.hospital.name + " Appointment Reminder";

        sendEmail(to, messageText, subject);
    }

    static public void sendEmailConfirmation(Appointment appointment) {
        String to = appointment.user.emailAddress;

        SimpleDateFormat dateFormater = new SimpleDateFormat("EEEEE, MMMMM d 'at' h:mm aa");

        String messageText = "Hello\n\n" +
                "This is a reminder from MedIT that you have an appointment with Dr. " +
                appointment.doctor.firstName + " " + appointment.doctor.lastName + " at " +
                appointment.clinic.hospital.name + " scheduled for " +
                dateFormater.format(appointment.date) + ". INSERT CONFIRMATION LINK HERE.";

        String subject = appointment.clinic.hospital.name + " Appointment Confirmation";

        sendEmail(to, messageText, subject);
    }

    static private void sendEmail(String recipient, String messageText, String subject) {

        String from = "MedIT.auto@gmail.com";
        String username = "MedIT.auto";
        String password = "";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }

}
