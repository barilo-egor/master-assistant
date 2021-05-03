package by.moon.masterassistant.service.beanservice;

import by.moon.masterassistant.bean.MasterService;
import by.moon.masterassistant.exceptions.MasterServiceNotFoundException;
import by.moon.masterassistant.repository.MasterServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterServiceService {
    private MasterServiceRepository masterServiceRepository;

    public List<MasterService> findAll(){
        return masterServiceRepository.findAll();
    }

    public MasterService save(MasterService masterService){
        return masterServiceRepository.save(masterService);
    }

    public void delete(MasterService masterService){
        masterServiceRepository.delete(masterService);
    }

    public MasterService findById(long id){
        return masterServiceRepository.findById(id).orElseThrow(MasterServiceNotFoundException::new);
    }

    @Autowired
    public void setMasterServiceRepository(MasterServiceRepository masterServiceRepository) {
        this.masterServiceRepository = masterServiceRepository;
    }
}
