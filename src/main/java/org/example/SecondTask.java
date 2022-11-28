package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.model.Fine;
import org.model.Fines;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SecondTask {

    public static void jsonFilesToXml(String folderPath) {
        GenericExtFilter filter = new GenericExtFilter("jsom");
        File folder = new File(folderPath);
        if (folder.listFiles() == null) {
            throw new IllegalArgumentException("Current directory contains no files!");
        } else if (folderPath == null) {
            throw new IllegalArgumentException("Invalid path,input path was null!");
        } else if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Directory does not exists!");
        } else if (folder.list(filter).length == 0) {
            throw new IllegalArgumentException("Provided folder contains no .json files!");
        }

        Map<String, Double> map = readJson(folderPath);
        jacksonAnnotation2Xml(map);
        System.out.println("It is succeeded!");
    }

    private static void jacksonAnnotation2Xml(Map<String, Double> map) {
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
        File[] listOfFiles = dir.listFiles();
        try {
            assert listOfFiles != null;
            for (File file : listOfFiles) {
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode items = mapper.readValue(fileReader, ArrayNode.class);
                for (JsonNode node : items) {
                    type = node.get("type").toString();
                    fineAmount = Double.parseDouble(node.get("fine_amount").toString());
                    if (!fineMap.containsKey(type)) {
                        fineMap.put(type, fineAmount);
                    } else {
                        fineMap.put(type, fineMap.get(type) + fineAmount);
                    }
                }
            }
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
        Fines fines = new Fines();
        Fine fine;
        for (var entry : map.entrySet()) {
            fine = new Fine();
            fine.setFineAmount(entry.getValue());
            fine.setType(entry.getKey());
            fines.getFines().add(fine);
        }
        return fines;
    }
}
