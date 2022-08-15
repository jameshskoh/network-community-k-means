package commkmeans.util;

import commkmeans.exceptions.InvalidFormatException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CacheHelper {
    public static double[][] readPassTimeCache(String cacheName, int numVert) throws FileNotFoundException, InvalidFormatException {
        File file = new File(cacheName);
        Scanner sc = new Scanner(file);

        double[][] cache = new double[numVert][numVert];

        for (int i = 0; i < numVert; i++) {
            String line = sc.nextLine();
            String[] valueStrings = line.split("\t");

            if (valueStrings.length != numVert) {
                throw new InvalidFormatException("Number of values does not match matrix size.");
            }

            for (int j = 0; j < numVert; j++) {
                try {
                    cache[i][j] = Double.parseDouble(valueStrings[j]);
                } catch (NumberFormatException e) {
                    String msg = String.format("Number %s cannot be cast to double.", valueStrings[j]);
                    throw new InvalidFormatException(msg);
                }
            }
        }

        return cache;
    }

    public static void writePassTimeCache(String cacheName, double[][] passTime) throws IOException {
        int numVert = passTime.length;

        FileWriter fw = new FileWriter(cacheName);

        for (int i = 0; i < numVert; i++) {
            for (int j = 0; j < numVert; j++) {
                fw.write(Double.toString(passTime[i][j]));
                if (j != numVert - 1) {
                    fw.write("\t");
                }
            }

            fw.write(System.lineSeparator());
        }

        fw.close();
    }
}
