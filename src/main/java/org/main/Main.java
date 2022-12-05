package org.main;

import org.example.FirstTask;
import org.example.SecondTask;

//program entry point
public class Main {
    public static void main(String[] args){
        FirstTask.setText("src/main/java/org/xmlinput/Input.xml");
        SecondTask.jsonFilesToXml("src/main/java/org/jsonfilesinput");
    }
}
