package by.moon.masterassistant.service.beanservice;

import by.moon.masterassistant.bean.WorkingDay;
import by.moon.masterassistant.enums.Day;
import by.moon.masterassistant.exceptions.WorkingDayNotFoundException;
import by.moon.masterassistant.repository.WorkingDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkingDayService {
    private WorkingDayRepository workingDayRepository;

    public List<WorkingDay> findAll(){
        return workingDayRepository.findAll();
    }

    public WorkingDay findByDay(Day day){
        return workingDayRepository.findByDay(day).orElseThrow(WorkingDayNotFoundException::new);
    }

    public WorkingDay findById(long id){
        return workingDayRepository.findById(id).orElseThrow(WorkingDayNotFoundException::new);
    }

    public WorkingDay save(WorkingDay workingDay){
        return workingDayRepository.save(workingDay);
    }

    public void delete(WorkingDay workingDay){
        workingDayRepository.delete(workingDay);
    }

    public boolean existsByDay(Day day){
        return workingDayRepository.existsByDay(day);
    }

    @Autowired
    public void setWorkingDayRepository(WorkingDayRepository workingDayRepository) {
        this.workingDayRepository = workingDayRepository;
    }
}
