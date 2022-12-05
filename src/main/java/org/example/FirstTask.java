package org.example;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstTask {
    public static void setText(String fileName) {
        if (fileName == null ) {
            throw new IllegalArgumentException("Filepath and destination is null!");
        } else if (!fileName.contains(".xml")) {
            throw new IllegalArgumentException("Wrong file format!!");
        }
        //represents pattern name="Name" with tab
        // group1 for name="name"
        //group two only for "name"
        Pattern namePattern = Pattern.compile("\\s+(name\\s*=\\s*\"([ІЇА-Яа-яії]*)\")\\s*");
        //represents surname="SurName" with tabulation
        // group1 for surname="surname"
        //group two only for "surname"
        Pattern surnamePattern = Pattern.compile("\\s+(surname\\s*=\\s*\"([ІЇА-Яа-яії]*)\")\\s*");
        //use Scanner to read file partially
        //pattern "<" as border between values
        try (Scanner scanner = new Scanner(new FileReader(fileName)).useDelimiter("(?<=<)")) {
            //creating temp file for result write
            File tempFile = File.createTempFile("temp", ".xml");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            while (scanner.hasNext()) {
                String current = scanner.next();
                Matcher matcherName = namePattern.matcher(current);
                Matcher matcherSurname = surnamePattern.matcher(current);
                if (matcherName.find() && matcherSurname.find()) {
                    // replacing surname="surname" for empty string
                    current = current.replaceAll(matcherSurname.group(1), "");
                    //replace all "name" in name="name"
                    //for name="name surname"
                    current = current.replaceAll(matcherName.group(2),
                            matcherName.group(2) + " "
                                    + matcherSurname.group(2)
                    );
                }
                writer.write(current);
            }
            writer.close();
            //creating output file
            File original=new File("src/main/java/org/out/Task.xml");
            try (FileChannel src = new FileInputStream(tempFile).getChannel()) {
                FileChannel dest = new FileOutputStream(original).getChannel();
                dest.transferFrom(src, 0, src.size());
            }
            tempFile.delete();
            System.out.println("File has successfully read and copied!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}