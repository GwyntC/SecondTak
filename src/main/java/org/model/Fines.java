package org.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
@JacksonXmlRootElement(localName = "penalty")

//model class for holding list of models
//used for writing xml
public class Fines {
    @JacksonXmlElementWrapper(localName = "fines")
    @JacksonXmlProperty(localName = "fine")
    private List<Fine> fines = new ArrayList<>();

    public List<Fine> getFines() {
        return fines;
    }

    public void setFines(List<Fine> fines) {
        this.fines = fines;
    }
}
