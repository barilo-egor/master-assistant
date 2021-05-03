package by.moon.masterassistant.service.beanservice;

import by.moon.masterassistant.bean.BalanceChange;
import by.moon.masterassistant.exceptions.BalanceChangeExceptionNotFound;
import by.moon.masterassistant.repository.BalanceChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceChangeService {
    private BalanceChangeRepository balanceChangeRepository;

    public List<BalanceChange> findAll(){
        return balanceChangeRepository.findAll();
    }

    public BalanceChange save(BalanceChange balanceChange){
        return balanceChangeRepository.save(balanceChange);
    }

    public BalanceChange findById(long id){
        return balanceChangeRepository.findById(id).orElseThrow(BalanceChangeExceptionNotFound::new);
    }

    @Autowired
    public void setBalanceChangeRepository(BalanceChangeRepository balanceChangeRepository) {
        this.balanceChangeRepository = balanceChangeRepository;
    }
}
