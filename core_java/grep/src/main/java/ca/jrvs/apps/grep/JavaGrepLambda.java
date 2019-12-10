package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Supplier;

public class JavaGrepLambda extends JavaGrepImp {
    public static void main(String[] args) {
        JavaGrepLambda javaGrepLambda = new JavaGrepLambda();
        javaGrepLambda.setRegex(args[0]);
        javaGrepLambda.setRootPath(args[1]);
        javaGrepLambda.setOutFile(args[2]);

        try {
            javaGrepLambda.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<File> listFiles(String rootDir) {
        List<File> list_files = new ArrayList<>();
        try {
            list_files = Files.walk(Paths.get(rootDir))
                    .filter(f -> Files.isRegularFile(f))
                    .map(Path :: toFile)
                    .collect(Collectors.toList());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return list_files;
    }

    public List<String> readLines(File inputFile) {
        List<String> linesRead = new ArrayList<>();
//
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            linesRead = br.lines().collect(Collectors.toList());
            br.close();


        } catch (IOException io) {
            io.printStackTrace();
        }
        return linesRead;
    }
}
