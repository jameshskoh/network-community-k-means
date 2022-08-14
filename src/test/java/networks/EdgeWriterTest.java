package networks;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class EdgeWriterTest {
    @Test
    void write_shouldWriteToCorrectFileWithCorrectOrder() {
        String testFileName = "datatest/write_test.edge";

        Map<Integer, Set<Integer>> neighborSets = new HashMap<>();
        neighborSets.put(0, Stream.of(3, 5, 10, 12).collect(Collectors.toSet()));
        neighborSets.put(10, Stream.of(0, 3, 5).collect(Collectors.toSet()));
        neighborSets.put(3, Stream.of(0, 10).collect(Collectors.toSet()));
        neighborSets.put(5, Stream.of(0, 10, 12).collect(Collectors.toSet()));
        neighborSets.put(12, Stream.of(0, 5).collect(Collectors.toSet()));

        List<String> lines = Stream.of(
                "0 10",
                "0 12",
                "0 3",
                "0 5",
                "10 0",
                "10 3",
                "10 5",
                "12 0",
                "12 5",
                "3 0",
                "3 10",
                "5 0",
                "5 10",
                "5 12"
                ).collect(Collectors.toList());

        EdgeWriter.write(neighborSets, testFileName, 0);

        Scanner sc;

        try {
            sc = new Scanner(new File(testFileName));

            for (String line : lines) {
                String myLine = sc.nextLine();
                assertEquals(line, myLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Should not throw FNFE!");
        }
    }
}
