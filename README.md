# WordTracker

## Description
WordTracker is a Java application designed to process text files, track word occurrences, and generate reports based on the words' appearances in the files. This application utilizes a Binary Search Tree (BST) to efficiently store and retrieve word data, including the files and line numbers where each word appears.

## Features
- **Word Tracking**: Tracks all unique words across multiple text files.
- **Report Generation**: Generates reports based on word occurrences, files, and line numbers.
- **Data Persistence**: Utilizes serialization to maintain state across multiple runs.

## Installation
To run WordTracker, you need to have Java installed on your machine. Download and install Java from [Oracle's official website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

## Usage
1. **Compile the Project**: First, compile the project into a runnable JAR file. This can be done using Eclipse:
   - Right-click on the project > Export > Runnable JAR file > Follow the prompts to specify your launch configuration and export destination.

2. **Running the Application**:
   ```bash
   java -jar WordTracker.jar <input_file> -flag [-f <output_file>]
