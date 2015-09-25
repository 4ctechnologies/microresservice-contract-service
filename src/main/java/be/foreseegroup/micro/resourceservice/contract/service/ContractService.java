package be.foreseegroup.micro.resourceservice.contract.service;

import be.foreseegroup.micro.resourceservice.contract.model.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Kaj on 24/09/15.
 */

@RestController
public class ContractService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractService.class);

    @Autowired
    ContractRepository repo;

    @RequestMapping(method = RequestMethod.GET, value = "/contracts")
    public ResponseEntity<Iterable<Contract>> getAll() {
        Iterable<Contract> contracts = repo.findAll();
        LOG.info("/contracts getAll method called, response size: {}", repo.count());
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/contracts/{id}")
    public ResponseEntity<Contract> getById(@PathVariable String id) {
        LOG.info("/contracts getById method called");
        Contract contract = repo.findOne(id);
        if (contract == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/contractsbycid/{consultantId}")
    public ResponseEntity<Iterable<Contract>> getByConsultantId(@PathVariable String consultantId) {
        LOG.info("/contracts getByConsultantId method called");
        Iterable<Contract> contracts = repo.findByConsultantId(consultantId);
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/contractsbyuid/{unitId}")
    public ResponseEntity<Iterable<Contract>> getByUnitId(@PathVariable String unitId) {
        LOG.info("/contracts getByUnitId method called");
        Iterable<Contract> contracts = repo.findByUnitId(unitId);
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/contracts")
    public ResponseEntity<Contract> create(@RequestBody Contract contract) {
        LOG.info("/contracts create method called");
        Contract createdContract = repo.save(contract);
        return new ResponseEntity<>(createdContract, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/contracts/{id}")
    public ResponseEntity<Contract>update(@PathVariable String id, @RequestBody Contract contract) {
        LOG.info("/contracts update method called");
        Contract update = repo.findOne(id);
        if (update == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        update.setConsultantId(contract.getConsultantId());
        update.setUnitId(contract.getUnitId());
        update.setStartDate(contract.getStartDate());
        update.setEndDate(contract.getEndDate());
        update.setType(contract.getType());
        Contract updatedContract = repo.save(update);
        return new ResponseEntity<>(updatedContract, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/contracts/{id}")
    public ResponseEntity<Contract> delete(@PathVariable String id) {
        LOG.info("/contracts delete method called");
        Contract contract = repo.findOne(id);
        if (contract == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        repo.delete(contract);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
