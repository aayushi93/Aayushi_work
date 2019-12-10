package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        try (Stream<String> stream = Files.lines(Paths.get(inputFile.toURI()))) {
            linesRead = stream
                    .collect(Collectors.toList());
            stream.forEach(linesRead :: add);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return linesRead;
    }
}
