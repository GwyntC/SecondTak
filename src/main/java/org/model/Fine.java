package org.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect

//model class
//presents one json object with fields
//used for read json ,as array unit
public class Fine {
    @JsonProperty("fine_amount")
    private Double fineAmount;
    @JsonProperty("type")
    private String type;

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
