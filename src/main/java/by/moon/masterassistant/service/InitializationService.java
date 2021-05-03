package by.moon.masterassistant.service;

import by.moon.masterassistant.service.reminders.AppointmentReminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializationService {
    private AppointmentReminder appointmentReminder;

    public void initialize(){
        appointmentReminder.run();
    }

    @Autowired
    public void setAppointmentReminder(AppointmentReminder appointmentReminder) {
        this.appointmentReminder = appointmentReminder;
    }
}
