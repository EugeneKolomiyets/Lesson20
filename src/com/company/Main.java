package com.company;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation());
        while (true) {
            System.out.println("Start new search. Input 'end' to stop searching. Otherwise type any symbol");
            Scanner scan = new Scanner(System.in);
            String endString = scan.nextLine();
            if (endString.equals("end"))
                break;

            System.out.println("intup path of searhing (type 'yes') otherwise search in default directory");

            String pathString = scan.nextLine();
            if (pathString.equals("yes"))
                pathString = scan.nextLine();
            else
                pathString = "";//Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Path path = Paths.get(pathString);

            System.out.println("if you want to search directories(type 'dir') otherwise search files");
            final Boolean dirSearch;
            String dirString = scan.nextLine();
            if (dirString.equals("dir"))
                dirSearch=true;
            else
                dirSearch = false;

            System.out.println("regular expressions filter (type 'yes') otherwise no filter");
            final Boolean filterReg;
            final String regularString;
            String regularExpressionFilter = scan.nextLine();
            if (regularExpressionFilter.equals("yes")) {
                regularString = scan.nextLine();
                filterReg=true;
            } else {
                filterReg = false;
                regularString = "";
            }

            final Boolean filterName;
            System.out.println("simple filter by name (type 'yes') otherwise no filter");
            final String subString;
            String simpleString = scan.nextLine();
            if (simpleString.equals("yes")) {
                filterName = true;
                System.out.println("input substring");
                subString = scan.nextLine();
            } else {
                filterName = false;
                subString = "";
            }

            final Boolean filterSize;
            final int minSize, maxSize;
            System.out.println("filter size filter (type 'yes') otherwise no filter)");
            String sizeString=scan.nextLine();
            if (sizeString.equals("yes")){
                filterSize=true;
                System.out.println("input minimum size in Kb");
                minSize = scan.nextInt();
                System.out.println("input maximum size in Kb");
                maxSize = scan.nextInt();
                scan.nextLine();
            } else {
                filterSize = false;
                minSize=0;
                maxSize=0;
            }

            final Boolean textFileSearch;
            System.out.println("filter text files by keywords (type 'yes') otherwise no filter)");
            final String keyWord;
            String textString=scan.nextLine();
            if (textString.equals("yes")){
                textFileSearch=true;
                System.out.println("input substring");
                keyWord = scan.nextLine();
            } else {
                textFileSearch = false;
                keyWord = "";
            }

            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk
                           //.filter(Files::isRegularFile)
                            //.filter()
                           .filter(path1 -> (filterReg ? path1.toFile().getName().matches(regularString):true) /* "d.*" */
                           && (filterName ? path1.toString().contains(subString):true)
                           && (dirSearch ? path1.toFile().isDirectory():path1.toFile().isFile())
                           && (filterSize ? (getSizeKb(path1)>= minSize && getSizeKb(path1)<= maxSize):true)
                           && (textFileSearch ? testFile(path1, keyWord):true))
                            .map(Path::toAbsolutePath)
                            .forEach(System.out::println);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static private int getSizeKb(Path path1){
        return (int)(path1.toFile().length()/1024);
    }

    static private boolean testFile(Path path1,String keyWord){
        //System.out.println(path1);
        try (Stream<String> stream = Files.lines(path1)) {
            return stream.map(String::toLowerCase)
                    .anyMatch(line1 -> line1.contains(keyWord));
        }
        catch (UncheckedIOException | IOException e) {
            // Handle a potential exception
        }

        return false;
    }
}
