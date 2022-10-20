package com.datapao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVWriter {

    private String filePath;

    public CSVWriter(String filePath) {
        this.filePath = filePath;
    }

    public void write(List<String> lines, String header) {
        File csvOutputFile = new File(filePath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(header);
            lines.forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
