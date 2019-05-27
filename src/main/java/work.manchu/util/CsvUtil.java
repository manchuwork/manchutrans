package work.manchu.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.charset.Charset;

public class CsvUtil {

    public static CSVReader createCSVReader(File file) throws IOException {
        CSVReader csvReader = new CSVReader(new BufferedReader(new FileReader(file, Charset.forName("utf-8"))));

        return csvReader;
    }

    public static CSVWriter createCSVWriter(File file) throws IOException {

        CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(file, Charset.forName("utf-8"))));

        return csvWriter;
    }

}
