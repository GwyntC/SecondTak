package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.model.Fine;
import org.model.Fines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SecondTask {

    public static void jsonFilesToXml(String folderPath) {
        GenericExtFilter filter = new GenericExtFilter("json");
        File folder = new File(folderPath);
        if (folder.listFiles() == null) {
            throw new IllegalArgumentException("Current directory contains no files!");
        } else if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Directory does not exists!");
        } else if (Objects.requireNonNull(folder.list(filter)).length == 0) {
            throw new IllegalArgumentException("Provided folder contains no .json files!");
        }

        Map<String, Double> map = readJson(folderPath);
        jacksonAnnotation2Xml(map);
        System.out.println("It is succeeded!");
    }

    private static void jacksonAnnotation2Xml(Map<String, Double> map) {
        //using XmlMapper and model object to write xml
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            xmlMapper.writeValue(new File("./src/main/java/org/out/penalties.xml"), getFines(map));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Double> readJson(String folderPath) {
        String type;
        Double fineAmount;
        Map<String, Double> fineMap = new HashMap<>();
        File dir = new File(folderPath);
        //getting files in directory
        File[] listOfFiles = dir.listFiles();
        try {
            assert listOfFiles != null;
            //reading one file at a time
            for (File file : listOfFiles) {
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                ObjectMapper mapper = new ObjectMapper();
                //using this setting to prevent fail from reading fields that are not in model
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //getting list from array from json
                Fine[] fines = mapper.readValue(fileReader, Fine[].class);
                for (Fine fine : fines) {
                    type = fine.getType();
                    fineAmount = fine.getFineAmount();
                    if (!fineMap.containsKey(type)) {
                        fineMap.put(type, fineAmount);
                    } else {
                        fineMap.put(type, fineMap.get(type) + fineAmount);
                    }
                }
                fileReader.close();
            }
            //sorting our map reversed order(biggest on top)
            fineMap = fineMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            return fineMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Fines getFines(Map<String, Double> map) {
        //getting object fom map to write xml in xmlMapper
        Fines fines = new Fines();
        Fine fine;
        //looping throw map and adding keys and values to model
        for (var entry : map.entrySet()) {
            fine = new Fine();
            fine.setFineAmount(entry.getValue());
            fine.setType(entry.getKey());
            fines.getFines().add(fine);
        }
        return fines;
    }
}
