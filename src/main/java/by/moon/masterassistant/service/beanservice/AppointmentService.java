package by.moon.masterassistant.service.beanservice;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.exceptions.AppointmentNotFoundException;
import by.moon.masterassistant.repository.AppointmentRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;

    public List<Appointment> findAll(){
        return appointmentRepository.findAll();
    }

    public List<Appointment> findByDate(LocalDate date){
        return appointmentRepository.findByDate(date);
    }

    public Appointment save(Appointment appointment){
        return appointmentRepository.save(appointment);
    }

    public void delete(Appointment appointment){
        appointmentRepository.delete(appointment);
    }

    public Appointment findById(long id){
        return appointmentRepository.findById(id).orElseThrow(AppointmentNotFoundException::new);
    }

    public List<Appointment> findByUser(User user){
        return appointmentRepository.findByUser(user);
    }

    @Autowired
    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
}
