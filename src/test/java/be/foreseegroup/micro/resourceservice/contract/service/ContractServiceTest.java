package be.foreseegroup.micro.resourceservice.contract.service;

import be.foreseegroup.micro.resourceservice.contract.ContractServiceApplication;
import be.foreseegroup.micro.resourceservice.contract.model.Contract;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Kaj on 25/09/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ContractServiceApplication.class)
@WebIntegrationTest
public class ContractServiceTest {

    private static final String ROOT_PATH = "http://localhost:8888";
    private static final String UNIT_PATH = "/contracts";
    private static final String UNIT_RESOURCE = ROOT_PATH + UNIT_PATH;


    private static final Contract CONTRACT_1 = new Contract("unitId1", "consultantId1", "startDate1", "endDate1", "type1");
    private static final Contract CONTRACT_2 = new Contract("unitId2", "consultantId2", "startDate2", "endDate2", "type2");
    private static final String NON_EXISTING_ID = "nonExistingId";

    @Autowired
    private ContractRepository repo;

    private RestTemplate restTemplate = new TestRestTemplate();


    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        repo.deleteAll();
    }

    /** Test case: getExistingPersonShouldReturnPerson
     *
     * Test if a GET result on an existing entry return the entry itself
     * Also, the Http response should have HttpStatus Code: OK (200)
     */

    @Test
    public void getExistingPersonShouldReturnPerson() {
        //Add the Contract that we will try to GET request to the database
        Contract savedContract = repo.save(CONTRACT_1);

        String url = UNIT_RESOURCE + "/" + savedContract.getId();

        //Instantiate the HTTP GET Request
        ResponseEntity<Contract> response = restTemplate.getForEntity(url, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.OK;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNotNull("Http Request response body did not contain a Contract object", response.getBody());


        Contract receivedContract = response.getBody();

        //Finally, match if the values of the received Contract are valid
        assertEquals("ID of the received object is invalid", savedContract.getId(), receivedContract.getId());
        assertEquals("unitId of the received object is invalid", savedContract.getUnitId(), receivedContract.getUnitId());
        assertEquals("consultantId of the received object is invalid", savedContract.getConsultantId(), receivedContract.getConsultantId());
        assertEquals("startDate of the received object is invalid", savedContract.getStartDate(), receivedContract.getStartDate());
        assertEquals("endDate of the received object is invalid", savedContract.getEndDate(), receivedContract.getEndDate());
        assertEquals("type of the received object is invalid", savedContract.getType(), receivedContract.getType());

    }

    /** Test case: getUnexistingPersonShouldReturnHttpNotFoundError
     *
     * Test if a GET result on an unexisting entry return an error
     * It should not contain an object in its body
     * It should return a HttpStatus code: NOT_FOUND (404)
     */

    @Test
    public void getUnexistingPersonShouldReturnHttpNotFoundError() {
        String url = UNIT_RESOURCE + "/" + NON_EXISTING_ID;

        //Instantiate the HTTP GET Request
        ResponseEntity<Contract> response = restTemplate.getForEntity(url, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.NOT_FOUND;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNull("Http Request response body did contain a Contract object", response.getBody());
    }

    /** Test case: getPersonsShouldReturnAllPersons
     *
     * Test if a GET results without specifying an ID results all the entries
     * It should contain all the entries in its body
     * It should return HttpStatus code: OK (200)
     */
    @Test
    public void getPersonsShouldReturnAllPersons() {
        //Add the Contract that we will try to GET request to the database
        Contract savedContract1 = repo.save(CONTRACT_1);
        Contract savedContract2 = repo.save(CONTRACT_2);

        String url = UNIT_RESOURCE;

        //Instantiate the HTTP GET Request
        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.OK;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNotNull("Http Request response body did not contain a Contract object", response.getBody());

        //Add the received entries to an ArrayList (has a .size() method to count the entries)
        ArrayList<Contract> responseList = new ArrayList<>();
        if (response.getBody() != null) {
            for (Contract u : response.getBody()) {
                responseList.add(u);
            }

        }

        //Check if the amount of entries is correct
        assertEquals("Response body size did not match", 2, responseList.size());
    }


    /** Test case: getContractsByConsultantIdShouldReturnContracts
     *
     * Test if a GET result based on ConsultantId returns the Contracts where the specifiec ConsultantId is involved
     * The Http response should have HttpStatus Code: OK (200)
     */

    @Test
    public void getContractsByConsultantIdShouldReturnContracts() {
        //Add the Contracts that we will try to GET request to the database;

        Contract a = new Contract("unitId1","consultantId1","startDate1","endDate1","type1");
        Contract b = new Contract("unitId2","consultantId1","startDate2","endDate2","type2");
        Contract c = new Contract("unitId3","consultantId1","startDate3","endDate3","type3");
        Contract d = new Contract("unitId4","consultantId4","startDate4","endDate4","type4");
        Contract e = new Contract("unitId5","consultantId5","startDate5","endDate5","type5");

        repo.save(a);
        repo.save(b);
        repo.save(c);
        repo.save(d);
        repo.save(e);

        String url = ROOT_PATH + "/contractsbycid/" + a.getConsultantId();

        //Instantiate the HTTP GET Request
        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);


        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.OK;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNotNull("Http Request response body did not contain a Contract object", response.getBody());

        //Add the received entries to an ArrayList (has a .size() method to count the entries)
        ArrayList<Contract> responseList = new ArrayList<>();
        if (response.getBody() != null) {
            for (Contract u : response.getBody()) {
                responseList.add(u);
            }

        }

        //Check if the amount of entries is correct
        assertEquals("Response body size did not match", 3, responseList.size());
    }



    /** Test case: getContractsByUnitIdShouldReturnContracts
     *
     * Test if a GET result based on ConsultantId returns the Contracts where the specifiec ConsultantId is involved
     * The Http response should have HttpStatus Code: OK (200)
     */

    @Test
    public void getContractsByUnitIdShouldReturnContracts() {
        //Add the Contracts that we will try to GET request to the database;

        Contract a = new Contract("unitId1","consultantId1","startDate1","endDate1","type1");
        Contract b = new Contract("unitId1","consultantId2","startDate2","endDate2","type2");
        Contract c = new Contract("unitId1","consultantId3","startDate3","endDate3","type3");
        Contract d = new Contract("unitId4","consultantId4","startDate4","endDate4","type4");
        Contract e = new Contract("unitId5","consultantId5","startDate5","endDate5","type5");

        repo.save(a);
        repo.save(b);
        repo.save(c);
        repo.save(d);
        repo.save(e);

        String url = ROOT_PATH + "/contractsbyuid/" + a.getUnitId();

        //Instantiate the HTTP GET Request
        ParameterizedTypeReference<Iterable<Contract>> responseType = new ParameterizedTypeReference<Iterable<Contract>>() {};
        ResponseEntity<Iterable<Contract>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);


        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.OK;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNotNull("Http Request response body did not contain a Contract object", response.getBody());

        //Add the received entries to an ArrayList (has a .size() method to count the entries)
        ArrayList<Contract> responseList = new ArrayList<>();
        if (response.getBody() != null) {
            for (Contract u : response.getBody()) {
                responseList.add(u);
            }
        }

        //Check if the amount of entries is correct
        assertEquals("Response body size did not match", 3, responseList.size());
    }



    /** Test case: createContractShouldCreateContract
     *
     * Test if a POST result of a Contract instance results in the Contract being saved to the database
     * The Http Request response should return with the HttpStatus code: OK (200)
     */
    @Test
    public void createContractShouldCreateContract() {
        String url = UNIT_RESOURCE;

        //Instantiate the HTTP POST Request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contract> httpEntity = new HttpEntity<>(CONTRACT_1, requestHeaders);
        ResponseEntity<Contract> response = restTemplate.postForEntity(url, httpEntity, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.OK;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNotNull("Http Request response body did not contain a Contract object", response.getBody());

        //Check if the returned object is valid in comparison with the published on
        assertEquals("Returned entry is invalid", CONTRACT_1.getConsultantId(), response.getBody().getConsultantId());
        assertEquals("Returned entry is invalid", CONTRACT_1.getUnitId(), response.getBody().getUnitId());
        assertEquals("Returned entry is invalid", CONTRACT_1.getStartDate(), response.getBody().getStartDate());
        assertEquals("Returned entry is invalid", CONTRACT_1.getEndDate(), response.getBody().getEndDate());
        assertEquals("Returned entry is invalid", CONTRACT_1.getType(), response.getBody().getType());

        //Check if the returned entry contains an ID
        assertNotNull("Returned entry did not contain an ID", response.getBody().getId());

        //Check if the contract was added to the database
        Contract contractFromDb = repo.findOne(response.getBody().getId());

        //Check if the entry that was added is valid
        assertEquals("consultantId did not match",CONTRACT_1.getConsultantId(),contractFromDb.getConsultantId());
        assertEquals("unitId did not match",CONTRACT_1.getUnitId(),contractFromDb.getUnitId());
        assertEquals("startDate did not match",CONTRACT_1.getStartDate(),contractFromDb.getStartDate());
        assertEquals("endDate did not match",CONTRACT_1.getEndDate(),contractFromDb.getEndDate());
        assertEquals("type did not match",CONTRACT_1.getType(),contractFromDb.getType());

        //Check if only 1 entry was added
        assertEquals("More than one record was added to the database", 1, repo.count());
    }

    /** Test case: createContractWithoutBodyShouldNotAddContract
     *
     * Test if a POST request without a body does not result in an entry added to the database
     * Also, the Http Request response should have HttpStatus code: BAD_REQUEST (400)
     */
    @Test
    public void createContractWithoutBodyShouldNotAddContract() {
        String url = UNIT_RESOURCE;

        //Instantiate the HTTP POST Request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contract> httpEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<Contract> response = restTemplate.postForEntity(url, httpEntity, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.BAD_REQUEST;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNotNull("Http Request response body did not contain a Contract object", response.getBody());

        //Check if a Contract was added to the database
        assertEquals("An entry was added to the database", 0, repo.count());
    }

    /** Test case: editContractShouldSaveEditionsAndReturnUpdatedContract
     *
     * Test if a PUT request to edit an entry results in the entry being saved
     * The Http Request should respond with an updated entry
     * Also, the Http Request response should have HttpStatus code: OK (200)
     */
    @Test
    public void editContractShouldSaveEditionsAndReturnUpdatedContract() {
        //Add the Contract that we will try to PUT request to the database
        Contract savedContract = repo.save(CONTRACT_1);

        String url = UNIT_RESOURCE + "/" + savedContract.getId();

        //Update the Contract
        savedContract.setConsultantId("consultantIdEdited");
        savedContract.setUnitId("unitIdEdited");
        savedContract.setStartDate("startDateEdited");
        savedContract.setEndDate("endDateEdited");
        savedContract.setType("typeEdited");


        //Instantiate the HTTP PUT Request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contract> httpEntity = new HttpEntity<>(savedContract, requestHeaders);
        ResponseEntity<Contract> response = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.OK;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNotNull("Http Request response body did not contain a Contract object", response.getBody());

        //Check if the returned entry contains is valid
        assertEquals("Returned entry contained invalid field values", savedContract.getId(), response.getBody().getId());
        assertEquals("Returned entry contained invalid field values", savedContract.getConsultantId(), response.getBody().getConsultantId());
        assertEquals("Returned entry contained invalid field values", savedContract.getUnitId(), response.getBody().getUnitId());
        assertEquals("Returned entry contained invalid field values", savedContract.getStartDate(), response.getBody().getStartDate());
        assertEquals("Returned entry contained invalid field values", savedContract.getEndDate(), response.getBody().getEndDate());
        assertEquals("Returned entry contained invalid field values", savedContract.getType(), response.getBody().getType());

        //Fetch the updated entry from the database
        Contract updatedContract = repo.findOne(savedContract.getId());

        //Check if the update was saved to the database
        assertEquals("Updated entry was not saved to the database", savedContract.getConsultantId(), updatedContract.getConsultantId());
        assertEquals("Updated entry was not saved to the database", savedContract.getUnitId(), updatedContract.getUnitId());
        assertEquals("Updated entry was not saved to the database", savedContract.getStartDate(), updatedContract.getStartDate());
        assertEquals("Updated entry was not saved to the database", savedContract.getEndDate(), updatedContract.getEndDate());
        assertEquals("Updated entry was not saved to the database", savedContract.getType(), updatedContract.getType());
    }

    /** Test case: editUnexistingContractShouldReturnError
     *
     * Test that when we try to update an unexisting entry the Http Request response does not contain an object
     * Also, it should have HttpStatus code: BAD_REQUEST (400)
     */
    @Test
    public void editUnexistingContractShouldReturnError() {
        //Instantiate the HTTP PUT Request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contract> httpEntity = new HttpEntity<>(CONTRACT_1, requestHeaders);
        ResponseEntity<Contract> response = restTemplate.exchange(UNIT_RESOURCE+"/unexistingid", HttpMethod.PUT, httpEntity, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.BAD_REQUEST;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNull("Http Request response body did contain an entry object", response.getBody());
    }

    /** Test case: deleteUnexistingContractShouldReturnError
     *
     * Test that if we try to delete an unexisting entry, this returns the HttpStatus code: BAD_REQUEST (400)
     */
    @Test
    public void deleteUnexistingContractShouldReturnError() {
        String url = UNIT_RESOURCE + "/" + NON_EXISTING_ID;

        //Instantiate the HTTP DELETE Request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contract> httpEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<Contract> response = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.BAD_REQUEST;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNull("Http Request response body did contain an entry object", response.getBody());
    }

    /** Test case: deleteContractShouldReturnError
     *
     * Test if instantiating a DELETE request on an existing entry results in the entry being deleted
     * The Http Request response should have HttpStatus code: NO_CONTENT (204)
     */
    @Test
    public void deleteContractShouldReturnError() {
        //Add the Contract that we will try to GET request to the database
        Contract savedContract = repo.save(CONTRACT_1);

        String url = UNIT_RESOURCE + "/" + savedContract.getId();

        //Instantiate the HTTP DELETE Request
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contract> httpEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<Contract> response = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Contract.class);

        //Check if we received a response
        assertNotNull("Http Request response was null", response);

        //Check if we receive the correct HttpStatus code
        HttpStatus expectedCode = HttpStatus.NO_CONTENT;
        assertEquals("HttpStatus code did not match", expectedCode, response.getStatusCode());

        //Check if the response contained a Contract object in its body
        assertNull("Http Request response body did contain an entry object", response.getBody());

        //Check if the entry was deleted in the database
        assertEquals("Contract was not deleted from the database", 0, repo.count());
    }
}