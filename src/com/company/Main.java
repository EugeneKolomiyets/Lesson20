package com.company;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        System.out.println(Main.class.getProtectionDomain().getCodeSource().getLocation());
        while (1==1) {
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
            Boolean dirSearch = false;
            String dirString = scan.nextLine();
            if (dirString.equals("dir")) {
                dirSearch=true;
            }

            System.out.println("regular expressions filter (type 'yes') otherwise no filter");
            Boolean filterReg = false;
            String regularString = scan.nextLine();
            if (regularString.equals("yes")) {
                regularString = scan.nextLine();
                filterReg=true;
            }

            Boolean filterName = false;
            System.out.println("simple filter by name (type 'yes') otherwise no filter");
            String substring="";
            String simpleString = scan.nextLine();
            if (simpleString.equals("yes")) {
                filterName = true;
                System.out.println("input substring");
                substring = scan.nextLine();
            }

            Boolean filterSize = false;
            int minSize=0, maxSize=0;
            System.out.println("filter size filter (type 'yes') otherwise no filter)");
            String sizeString=scan.nextLine();
            if (sizeString.equals("yes")){
                filterSize=true;
                System.out.println("input minimum size in Kb");
                minSize = scan.nextInt();
                System.out.println("input maximum size in Kb");
                maxSize = scan.nextInt();
                scan.nextLine();
            }

            Boolean textFileSearch = false;
            System.out.println("filter text files by keywords (type 'yes') otherwise no filter)");
            String keyWord="";
            String textString=scan.nextLine();
            if (textString.equals("yes")){
                textFileSearch=true;
                System.out.println("input substring");
                keyWord = scan.nextLine();
            }

            if (Files.exists(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    String finalRegularString = regularString;
                    Boolean finalFilterName = filterName;
                    String finalSubstring = substring;
                    Boolean finalDirSearch = dirSearch;
                    Boolean finalFilterReg = filterReg;
                    Boolean finalFilterSize = filterSize;
                    int finalMinSize = minSize;
                    int finalMaxSize = maxSize;
                    Boolean finalTextFileSearch = textFileSearch;
                    String finalKeyWord = keyWord;
                    walk
                           //.filter(Files::isRegularFile)
                            //.filter()
                           .filter(path1 -> (finalFilterReg ? path1.toFile().getName().matches(finalRegularString):true) /* "d.*" */
                           && (finalFilterName ? path1.toString().contains(finalSubstring):true)
                           && (finalDirSearch ? path1.toFile().isDirectory():path1.toFile().isFile())
                           && (finalFilterSize ? (getSizeKb(path1)>= finalMinSize && getSizeKb(path1)<= finalMaxSize):true)
                           && (finalTextFileSearch ? testFile(path1, finalKeyWord):true))
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
