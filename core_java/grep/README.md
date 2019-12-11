# Java Grep Application

## Introduction
Grep command in Linux is used to search text or searches the given files for lines containing a match to the given strings or words. It outputs the matching lines, by default.
Java Grep Application is a basic implementation of the same in Java. When it is run, it recursively searches all files in the specified directory and traverse line by line to find matches with a given regular expression. The output matched lines are then saved and written in a new file created by user.
The purpose of this project is to thoroughly learn Core Java and Java 8 features, especially Lambda Expressions and Stream APIs. It also introduced us to IntelliJ IDEA IDE and Maven. Maven built and managed this app which is a build automation tool and Project Object Model (POM).


## Usage
To use this application, we need to specify three arguments described below:
* _regex_ - Regular expression which will be searched for in all files in the specified directory.
* _rootPath_ - Tt is a path to the directory that will be searched recursively. All the sub directories of this directory will also be searched.
* _outFile_ - Output file where all the matched lines will be saved.
For example - "SampleRegex" is the text that needs to be searched. The app would search the *rootPath*, in this case, `home/centos/dev`  directory for any lines that contain text SampleRegex (__regex - .*SampleText.*__). Once the matches are found, it would write the list of matched lines to *OutFile*, in this case, `home\centos\grep.out` 
`.*SampleRegex.* home/centos/dev home/centos/grep.out`

## Pseudocode
High level description of working of JavaGrep functions:

```
matchedLines = []
for file in listFilesRecursively(rootDir)
        for line in readLines(file)
        if containsPattern(line)
                matchedLines.add(line)
        writeToFile(matchedLines)
```


Both of our implementations - JavaGrepImp and JavaGrepLambda uses JavaGrep function.

Description of methods in the workfow:
* _listFilesRecursively()_ - `listFiles(String rootDir) { return fileList; }` ->  Traverse the specified root directory recursively and generate list of files in the directory. 
* _readLines()_ - `readLines(File inputFile) { return File; }` -> Takes the 'file' and add all the generated lines to the file.
* _containsPattern()_ - `containsPattern(String line) { return matchedLines; }` -> Verifies if each line in the given file contains the regular expression specified by  argument.
* _writeToFile()_ - `writeToFile(List<String> lines) { return list of String; }` -> If regex is matched, it writes those lines whose pattern is matched to a new output file specified by the user.



## Performance Issue
Our program is good enough for small files and will work efficiently. But on large files, it will work but not very efficiently. The problem with this method is all file lines are kept in the memory - which will quickly lead to OutOfMemoryError if the file is large, say 50GB. The reason is lines of files are stored in the memory. By keeping contents of file in the memory will quickly exhaust the available memory.
1. What we need here is to just be able to iterate through each line of the file, do some processing and let it go, without keeping it in memory. In short, we need to iterate through the lines without holding the memory. This is called Streaming through the file. This solution will iterate through all the lines and process each line, without keeping any references in the memory. This way it consumes very less amount of memory. Hence, we have implemented this solution and we should not run into any memory issue with this implementation.
1. Another solution is Streaming with Apache Commons IO - We use LineIterator, available in Commons IO library. In this also, the entire file is not in the memory and hence very less memory consumption.
1. Splitting the file in chunks.


## Improvements
1. Using third party libraries like Unix4J and Grep4J. Grep4J can be used for remote as well as local files, to obtain grep information from the specified file.It also comes with functionalities to inverse text like we do in linux using '-v'.
1. Specifying line numbers and file names that are matched from which file or sub directory they are taken.
1. Checking for memory to make sure user does not run out of memory.
