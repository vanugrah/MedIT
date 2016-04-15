package com.medit.demoPanel;

import com.medit.ReminderManager;
import com.medit.db.Appointment;
import com.medit.db.Patient;

import javax.swing.*;
import java.util.Date;

/**
 * Created by matt on 4/14/2016.
 */
public class DemoPanel {
    private JButton triggerReminderButton;
    private JButton triggerConfirmationButton;
    private JPanel contentPane;

    public JPanel getContentPane() {
        return contentPane;
    }

    private ReminderManager rm;
    private Patient p;
    private Appointment a;

    public DemoPanel(ReminderManager rm) {
        this.rm = rm;

        p.patientID = 10000;
        p.firstName = "George";
        p.lastName = "Burdell";
        p.age = 21;
        p.sex = Patient.Sex.Male;
        p.user.username = "gpburdell";
        p.user.firstName = p.firstName;
        p.user.lastName = p.lastName;
        p.user.phoneNumber = "0000000000";
        p.user.emailAddress = "gpb@medit.net";
        p.user.preferences.getsPush = true;
        p.user.preferences.getsSMS = false;
        p.user.preferences.getsEmail = false;

        a.patient = p;
        a.user = p.user;
        a.date = new Date();
        a.clinic.clinicID = 0;
        a.clinic.phoneNumber = "0000000000";
        a.clinic.specialization = "EN&T";
        a.clinic.hospital.name = "Georgia Tech";
        a.clinic.hospital.zipCode = "00000";
        a.clinic.hospital.state = "Georgia";
        a.clinic.hospital.city = "Atlanta";
        a.clinic.hospital.address = "120 North Avenue";
        a.clinic.hospital.branch = "CS";
        a.clinic.hospital.hospitalID = 0;

        triggerConfirmationButton.addActionListener(e -> {
            rm.sendConfirmation(p, a);
        });

        triggerReminderButton.addActionListener(e -> {
            rm.sendReminder(a);
        });


    }
}
