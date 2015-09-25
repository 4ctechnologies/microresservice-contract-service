package be.foreseegroup.micro.resourceservice.contract.service;

import be.foreseegroup.micro.resourceservice.contract.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Kaj on 24/09/15.
 */
public interface ContractRepository extends MongoRepository<Contract, String> {
    List<Contract> findByConsultantId(String consultantId);
    List<Contract> findByUnitId(String unitId);
}
