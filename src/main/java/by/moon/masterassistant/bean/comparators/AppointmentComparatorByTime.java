package by.moon.masterassistant.bean.comparators;

import by.moon.masterassistant.bean.Appointment;

import java.util.Comparator;

public class AppointmentComparatorByTime implements Comparator<Appointment> {
    @Override
    public int compare(Appointment appointment, Appointment t1) {
        return appointment.getTime().compareTo(t1.getTime());
    }
}
