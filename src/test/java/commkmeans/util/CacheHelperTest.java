package commkmeans.util;

import commkmeans.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CacheHelperTest {
    @Test
    void readPassTimeCache_writePassTimeCache_shouldReadAndWriteCorrectly() {
        int numVert = 3;

        double[][] data = {
                {10.0/3.0, 2.50, 3.33333333},
                {1.0, 0.0, -3.33333333},
                {592385.0, 578299.532893, -572395.125905}
        };

        String dataFile = "datatest/ucsd_5000.edge";
        String cacheName = dataFile + ".cache";

        try {
            CacheHelper.writePassTimeCache(cacheName, data);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            CacheHelper.readPassTimeCache(cacheName, numVert);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
