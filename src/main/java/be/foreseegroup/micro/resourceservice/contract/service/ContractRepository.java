package be.foreseegroup.micro.resourceservice.contract.service;

import be.foreseegroup.micro.resourceservice.contract.model.Contract;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Kaj on 24/09/15.
 */
public interface ContractRepository extends CrudRepository<Contract, String> {
}
