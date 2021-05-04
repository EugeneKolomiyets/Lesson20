package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation());
        while (1==1) {
            System.out.println("Start new search. Input 'end' to stop searching. Otherwise type any symbol");
            Scanner scan = new Scanner(System.in);
            String s = scan.nextLine();
            if (s.equals("end"))
                break;
            System.out.println("intup path of searhing (type 'yes') otherwise search in default directory");

            s = scan.nextLine();
            if (s.equals("yes"))
                s = scan.nextLine();
            else
                s = "";//Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Path path = Paths.get(s);

            System.out.println("intup filter(type 'yes') otherwise no filter");
            Boolean filterReg = false;
            String s1 = scan.nextLine();
            if (s1.equals("yes")) {
                s1 = scan.nextLine();
                filterReg=true;
            }

            Boolean filterName = false;
            System.out.println("simple filter by name(type 'yes') otherwise no filter");
            String substring="";
            s = scan.nextLine();
            if (s.equals("yes")) {
                filterName = true;
                System.out.println("input substring");
                substring = scan.nextLine();
            }

            Boolean f=true;
            //path..d;
            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    String finalS = s1;
                    Boolean finalFilterName = filterName;
                    String finalSubstring = substring;
                    walk
                           .filter(Files::isRegularFile)
                            //.filter()
                           .filter(path1 -> path1.toFile().getName().matches(finalS))//"d.*"
                           .filter(path4 -> finalFilterName ?path4.toString().contains(finalSubstring):true)
                            .map(Path::toAbsolutePath)
                            .forEach(System.out::println);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
