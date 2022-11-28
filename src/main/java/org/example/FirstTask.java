package org.example;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstTask {
    public static void setText(String fileName, String dest) {
        if (fileName == null || dest == null) {
            throw new IllegalArgumentException("Filepath and destination is null!");
        }
        Pattern namePattern = Pattern.compile("\\s+(name\\s*=\\s*\"([ІЇА-Яа-яії]*)\")\\s*");//represents name="Name" with tabulation
        Pattern surnamePattern = Pattern.compile("\\s+(surname\\s*=\\s*\"([ІЇА-Яа-яії]*)\")\\s*");//represents surname="SurName" with tabulation
        try (Scanner scanner = new Scanner(new FileReader(fileName)).useDelimiter("(?<=<)")) {//chunks border is tag (<)
            File tempFile = File.createTempFile("temp", ".xml");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            while (scanner.hasNext()) {
                String current = scanner.next();
                Matcher matcherName = namePattern.matcher(current);
                Matcher matcherSurname = surnamePattern.matcher(current);
                if (matcherName.find() && matcherSurname.find()) {
                    current = current.replaceAll(matcherSurname.group(1), "");
                    current = current.replaceAll(matcherName.group(2),
                            matcherName.group(2) + " "
                                    + matcherSurname.group(2)
                    );
                }
                writer.write(current);
            }
            writer.close();
            replaceFile(tempFile, dest);
            tempFile.delete();
            System.out.println("File has successfully read and copied!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void replaceFile(File source, String destination) {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(destination)) {
            byte[] buffer = new byte[8 * 1024];
            int n;
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
