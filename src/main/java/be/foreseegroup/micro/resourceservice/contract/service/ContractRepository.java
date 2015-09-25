package be.foreseegroup.micro.resourceservice.contract.service;

import be.foreseegroup.micro.resourceservice.contract.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Kaj on 24/09/15.
 */
public interface ContractRepository extends MongoRepository<Contract, String> {
    Iterable<Contract> findByConsultantId(String consultantId);
    Iterable<Contract> findByUnitId(String unitId);
}
