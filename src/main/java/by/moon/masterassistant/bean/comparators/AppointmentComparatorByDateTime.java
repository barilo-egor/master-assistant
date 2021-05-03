package by.moon.masterassistant.bean.comparators;

import by.moon.masterassistant.bean.Appointment;

import java.util.Comparator;

public class AppointmentComparatorByDateTime implements Comparator<Appointment> {

    @Override
    public int compare(Appointment appointment, Appointment t1) {
        if(appointment.getDate().equals(t1.getDate())){
            return appointment.getTime().compareTo(t1.getTime());
        }
        return appointment.getDate().compareTo(t1.getDate());
    }
}
