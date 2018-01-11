package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.UIEntity.SensorDTO;

import java.util.Date;

public class EventPhaseDTO {
    private String phaseType;
    private SensorDTO phaseSensor;
    private String phaseObservationID;
    private Date startTime;
    private Date endTime;

    public String getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(String phaseType) {
        this.phaseType = phaseType;
    }

    public SensorDTO getPhaseSensor() {
        return phaseSensor;
    }

    public void setPhaseSensor(SensorDTO phaseSensor) {
        this.phaseSensor = phaseSensor;
    }

    public String getPhaseObservationID() {
        return phaseObservationID;
    }

    public void setPhaseObservationID(String phaseObservationID) {
        this.phaseObservationID = phaseObservationID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
