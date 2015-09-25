package be.foreseegroup.micro.resourceservice.contract.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by Kaj on 24/09/15.
 */
public class Contract {
    @Id
    private String id;

    private String unitId;
    private String consultantId;

    private String startDate;
    private String endDate;

    private String type;

    public Contract() {
    }

    public Contract(String unitId, String consultantId, String startDate, String endDate, String type) {
        this.unitId = unitId;
        this.consultantId = consultantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public Contract(String id, String unitId, String consultantId, String startDate, String endDate, String type) {
        this.id = id;
        this.unitId = unitId;
        this.consultantId = consultantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
