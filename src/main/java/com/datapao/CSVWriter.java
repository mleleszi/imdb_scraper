package com.datapao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVWriter {

    private String filePath;

    public CSVWriter(String filePath) {
        this.filePath = filePath;
    }

    public void write(List<String> lines) {
        File csvOutputFile = new File(filePath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            lines.forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
