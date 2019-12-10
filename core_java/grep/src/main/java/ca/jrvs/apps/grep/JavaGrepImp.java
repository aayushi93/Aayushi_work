package ca.jrvs.apps.grep;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JavaGrepImp implements JavaGrep {

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: regex rootPath outFile");
        }

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void process() throws IOException {
        List<String> file_list = new ArrayList<>();
        for (File f : listFiles(getRootPath())) {
            for (String lines : readLines(f)) {
                if (containsPattern(lines)) {
                    file_list.add(lines);
                }

            }

        }
        writeToFile(file_list);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        File dir = new File(rootDir);
        File[] fileList = dir.listFiles();
        List<File> newFileList = new ArrayList<>();

        if (fileList == null) {
            System.out.println("Empty directory");
            return null;
        }

        for (File file : fileList) {
            if (file.isDirectory()) {
                List<File> saveFileDirectory = listFiles(file.getAbsolutePath());
                newFileList.addAll(saveFileDirectory);
            } else {
                newFileList.add(file);
            }
        }

        return newFileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> numLines = new ArrayList<>();
        try {
            BufferedReader brd = new BufferedReader(new FileReader(inputFile));
            String l = brd.readLine();
            while (brd.ready() && l != null) {
                numLines.add(l);
                l = brd.readLine();
            }
            brd.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
        return numLines;
    }

    @Override
    public boolean containsPattern(String line) {
        return line.matches(regex);
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try {
            FileWriter fw = new FileWriter(getOutFile());
            BufferedWriter bwr = new BufferedWriter(fw);
            for (String line : lines) {
                bwr.write(line + "\n");
            }
            bwr.close();
            System.out.println("File writing done");
        } catch (IOException iox) {
            iox.printStackTrace();
        }


    }

    @Override
    public String getRootPath() {
        return null;
    }

    @Override
    public void setRootPath(String rootPath) {

    }

    @Override
    public String getRegex() {
        return null;
    }

    @Override
    public void setRegex(String regex) {

    }

    @Override
    public String getOutFile() {
        return null;
    }

    @Override
    public void setOutFile(String outFile) {

    }
}