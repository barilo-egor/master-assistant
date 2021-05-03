package by.moon.masterassistant.service.beanservice;

import by.moon.masterassistant.bean.MasterContact;
import by.moon.masterassistant.exceptions.MasterContactNotFoundException;
import by.moon.masterassistant.repository.MasterContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterContactService {
    private MasterContactRepository masterContactRepository;

    public List<MasterContact> findAll(){
        return masterContactRepository.findAll();
    }

    public MasterContact findById(long id){
        return masterContactRepository.findById(id).orElseThrow(MasterContactNotFoundException::new);
    }

    public MasterContact save(MasterContact masterContact){
        return masterContactRepository.save(masterContact);
    }

    public void delete(MasterContact masterContact){
        masterContactRepository.delete(masterContact);
    }

    @Autowired
    public void setMasterContactRepository(MasterContactRepository masterContactRepository) {
        this.masterContactRepository = masterContactRepository;
    }
}
