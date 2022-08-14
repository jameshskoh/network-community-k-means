package commkmeans.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static commkmeans.util.Constants.EPSILON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SAHelperTest {
    static Stream<Arguments> values() {
        int randomSeed = 0;

        int numSet1 = 3;
        int numVert1 = 11;
        int numEdge1 = 34;

        Map<Integer, Set<Integer>> neighborsSet1 = new HashMap<>();
        neighborsSet1.put(0, Stream.of(1, 4, 5).collect(Collectors.toSet()));
        neighborsSet1.put(1, Stream.of(0, 2, 4).collect(Collectors.toSet()));
        neighborsSet1.put(2, Stream.of(1, 3, 4).collect(Collectors.toSet()));
        neighborsSet1.put(3, Stream.of(2, 4).collect(Collectors.toSet()));
        neighborsSet1.put(4, Stream.of(0, 1, 2, 3, 5).collect(Collectors.toSet()));
        neighborsSet1.put(5, Stream.of(0, 4, 6, 7).collect(Collectors.toSet()));
        neighborsSet1.put(6, Stream.of(5, 7, 8, 9, 10).collect(Collectors.toSet()));
        neighborsSet1.put(7, Stream.of(5, 6).collect(Collectors.toSet()));
        neighborsSet1.put(8, Stream.of(6, 9).collect(Collectors.toSet()));
        neighborsSet1.put(9, Stream.of(6, 8, 10).collect(Collectors.toSet()));
        neighborsSet1.put(10, Stream.of(6, 9).collect(Collectors.toSet()));

        double[][] lambda1 = {
                {0.000000000, 1.117317834, 1.564419192, 1.492118532, 0.847628235, 2.388651194, 5.486139204, 4.031402543, 6.100796761, 6.312600139, 6.100796761},
                {1.117317834, 0.000000000, 0.770968488, 0.609903641, 0.459012839, 3.281495300, 6.294036902, 4.880861271, 6.882481246, 7.089249305, 6.882481246},
                {1.564419192, 0.770968488, 0.000000000, 0.287834482, 0.839432228, 3.670426322, 6.618398059, 5.231007292, 7.195111887, 7.398953540, 7.195111887},
                {1.492118532, 0.609903641, 0.287834482, 0.000000000, 0.831822935, 3.590660038, 6.534038763, 5.144027128, 7.108408051, 7.313500116, 7.108408051},
                {0.847628235, 0.459012839, 0.839432228, 0.831822935, 0.000000000, 3.027807727, 6.054134367, 4.635295413, 6.654442982, 6.861049354, 6.654442982},
                {2.388651194, 3.281495300, 3.670426322, 3.590660038, 3.027807727, 0.000000000, 3.461724873, 1.879789265, 4.157644553, 4.382555205, 4.157644553},
                {5.486139204, 6.294036902, 6.618398059, 6.534038763, 6.054134367, 3.461724873, 0.000000000, 1.787013049, 0.984446953, 1.195413044, 0.984446953},
                {4.031402543, 4.880861271, 5.231007292, 5.144027128, 4.635295413, 1.879789265, 1.787013049, 0.000000000, 2.541426319, 2.780924144, 2.541426319},
                {6.100796761, 6.882481246, 7.195111887, 7.108408051, 6.654442982, 4.157644553, 0.984446953, 2.541426319, 0.000000000, 0.451335467, 0.000000000},
                {6.312600139, 7.089249305, 7.398953540, 7.313500116, 6.861049354, 4.382555205, 1.195413044, 2.780924144, 0.451335467, 0.000000000, 0.451335467},
                {6.100796761, 6.882481246, 7.195111887, 7.108408051, 6.654442982, 4.157644553, 0.984446953, 2.541426319, 0.000000000, 0.451335467, 0.000000000}
        };
        double[][] expWeight1 = {
                {0.132352941, 0.132352941, 0.132352941, 0.088235294, 0.220588235, 0.176470588, 0.220588235, 0.088235294, 0.088235294, 0.132352941, 0.088235294},
                {0.132352941, 0.132352941, 0.132352941, 0.088235294, 0.220588235, 0.176470588, 0.220588235, 0.088235294, 0.088235294, 0.132352941, 0.088235294},
                {0.132352941, 0.132352941, 0.132352941, 0.088235294, 0.220588235, 0.176470588, 0.220588235, 0.088235294, 0.088235294, 0.132352941, 0.088235294},
                {0.088235294, 0.088235294, 0.088235294, 0.058823529, 0.147058824, 0.117647059, 0.147058824, 0.058823529, 0.058823529, 0.088235294, 0.058823529},
                {0.220588235, 0.220588235, 0.220588235, 0.147058824, 0.367647059, 0.294117647, 0.367647059, 0.147058824, 0.147058824, 0.220588235, 0.147058824},
                {0.176470588, 0.176470588, 0.176470588, 0.117647059, 0.294117647, 0.235294118, 0.294117647, 0.117647059, 0.117647059, 0.176470588, 0.117647059},
                {0.220588235, 0.220588235, 0.220588235, 0.147058824, 0.367647059, 0.294117647, 0.367647059, 0.147058824, 0.147058824, 0.220588235, 0.147058824},
                {0.088235294, 0.088235294, 0.088235294, 0.058823529, 0.147058824, 0.117647059, 0.147058824, 0.058823529, 0.058823529, 0.088235294, 0.058823529},
                {0.088235294, 0.088235294, 0.088235294, 0.058823529, 0.147058824, 0.117647059, 0.147058824, 0.058823529, 0.058823529, 0.088235294, 0.058823529},
                {0.132352941, 0.132352941, 0.132352941, 0.088235294, 0.220588235, 0.176470588, 0.220588235, 0.088235294, 0.088235294, 0.132352941, 0.088235294},
                {0.088235294, 0.088235294, 0.088235294, 0.058823529, 0.147058824, 0.117647059, 0.147058824, 0.058823529, 0.058823529, 0.088235294, 0.058823529}
        };

        Map<Integer, Set<Integer>> randomSets1 = new HashMap<>();
        randomSets1.put(2, Stream.of(2, 10, 7, 8).collect(Collectors.toSet()));
        randomSets1.put(6, Stream.of(6, 1, 9, 0).collect(Collectors.toSet()));
        randomSets1.put(3, Stream.of(3, 5, 4).collect(Collectors.toSet()));

        Map<Integer, Set<Integer>> recenteredSets1 = Map.of(
                8, Stream.of(2, 7, 8, 10).collect(Collectors.toSet()),
                // 10, Stream.of(2, 7, 8, 10).collect(Collectors.toSet()),
                0, Stream.of(0, 1, 6, 9).collect(Collectors.toSet()),
                4, Stream.of(3, 4, 5).collect(Collectors.toSet())
        );

        double energy1 = -0.031574395;

        int numSet2 = 4;
        int numVert2 = 19;
        double[][] lambda2 = {
                {0.000000000, 0.629280777, 0.557205848, 0.741034494, 1.216028100, 1.664658856, 2.055648056, 3.866458718, 3.895935072, 6.386815481, 4.168396283, 6.412764581, 4.300195625, 4.227719475, 4.207236424, 3.722348142, 2.929263131, 3.198820006, 2.598014966},
                {0.629280777, 0.000000000, 0.648760893, 0.969041861, 0.951992351, 1.633739221, 2.195194988, 3.988816969, 4.017394820, 6.467600073, 4.248326483, 6.493225821, 4.322981186, 4.216152582, 4.213027228, 3.651020218, 2.784006107, 3.054978202, 2.415959579},
                {0.557205848, 0.648760893, 0.000000000, 0.533085923, 1.024165196, 1.279708782, 1.727372462, 3.608306521, 3.630175304, 6.176712288, 3.872970123, 6.197854629, 3.999036847, 3.923285704, 3.899310681, 3.398344283, 2.596939169, 2.878226912, 2.287729350},
                {0.741034494, 0.969041861, 0.533085923, 0.000000000, 1.354715120, 1.310852971, 1.532771888, 3.390643697, 3.417628837, 6.015067672, 3.700219653, 6.038881669, 3.891883638, 3.859153978, 3.813798642, 3.413919757, 2.720005385, 2.996194108, 2.480853504},
                {1.216028100, 0.951992351, 1.024165196, 1.354715120, 0.000000000, 1.271623731, 2.147542243, 3.908837882, 3.933853996, 6.351021009, 4.053799987, 6.374558186, 4.013961202, 3.844470370, 3.869573008, 3.161532341, 2.167557260, 2.453993508, 1.813525322},
                {1.664658856, 1.633739221, 1.279708782, 1.310852971, 1.271623731, 0.000000000, 1.281920247, 3.085510108, 3.107866053, 5.688929241, 3.160764172, 5.710140560, 3.158026056, 3.039053189, 3.025392660, 2.440188177, 1.722930678, 1.965526015, 1.536173738},
                {2.055648056, 2.195194988, 1.727372462, 1.532771888, 2.147542243, 1.281920247, 0.000000000, 2.224083202, 2.233572410, 5.134319993, 2.502180520, 5.148482489, 2.889643434, 2.987316215, 2.860526956, 2.754803263, 2.456711327, 2.699817367, 2.499933731},
                {3.866458718, 3.988816969, 3.608306521, 3.390643697, 3.908837882, 3.085510108, 2.224083202, 0.000000000, 0.242535625, 4.692412342, 2.177417554, 4.709658973, 2.935218806, 3.264768613, 3.040795934, 3.468577709, 3.676359736, 3.808902051, 3.895521224},
                {3.895935072, 4.017394820, 3.630175304, 3.417628837, 3.933853996, 3.107866053, 2.233572410, 0.242535625, 0.000000000, 4.687667500, 2.157572182, 4.692412342, 2.946470176, 3.282646272, 3.051489455, 3.485470331, 3.696323611, 3.832210779, 3.918519336},
                {6.386815481, 6.467600073, 6.176712288, 6.015067672, 6.351021009, 5.688929241, 5.134319993, 4.692412342, 4.687667500, 0.000000000, 3.539202107, 0.242535625, 4.376243130, 4.828140813, 4.571647025, 5.325963200, 5.851801360, 5.879966667, 6.126329853},
                {4.168396283, 4.248326483, 3.872970123, 3.700219653, 4.053799987, 3.160764172, 2.502180520, 2.177417554, 2.157572182, 3.539202107, 0.000000000, 3.541645464, 1.626907648, 2.171217798, 1.840216055, 2.733423906, 3.375227445, 3.438795276, 3.736166637},
                {6.412764581, 6.493225821, 6.197854629, 6.038881669, 6.374558186, 5.710140560, 5.148482489, 4.709658973, 4.692412342, 0.242535625, 3.541645464, 0.000000000, 4.395567257, 4.850909883, 4.590036495, 5.346651909, 5.873167545, 5.903849858, 6.149386650},
                {4.300195625, 4.322981186, 3.999036847, 3.891883638, 4.013961202, 3.158026056, 2.889643434, 2.935218806, 2.946470176, 4.376243130, 1.626907648, 4.395567257, 0.000000000, 0.833689708, 0.419071277, 1.829656013, 2.915502728, 2.885327691, 3.384330117},
                {4.227719475, 4.216152582, 3.923285704, 3.859153978, 3.844470370, 3.039053189, 2.987316215, 3.264768613, 3.282646272, 4.828140813, 2.171217798, 4.850909883, 0.833689708, 0.000000000, 0.447165993, 1.351629067, 2.560548728, 2.488354721, 3.059622122},
                {4.207236424, 4.213027228, 3.899310681, 3.813798642, 3.869573008, 3.025392660, 2.860526956, 3.040795934, 3.051489455, 4.571647025, 1.840216055, 4.590036495, 0.419071277, 0.447165993, 0.000000000, 1.546125935, 2.675636504, 2.632072677, 3.162633074},
                {3.722348142, 3.651020218, 3.398344283, 3.413919757, 3.161532341, 2.440188177, 2.754803263, 3.468577709, 3.485470331, 5.325963200, 2.733423906, 5.346651909, 1.829656013, 1.351629067, 1.546125935, 0.000000000, 1.541280941, 1.418500999, 2.092505791},
                {2.929263131, 2.784006107, 2.596939169, 2.720005385, 2.167557260, 1.722930678, 2.456711327, 3.676359736, 3.696323611, 5.851801360, 3.375227445, 5.873167545, 2.915502728, 2.560548728, 2.675636504, 1.541280941, 0.000000000, 0.466125859, 0.835629820},
                {3.198820006, 3.054978202, 2.878226912, 2.996194108, 2.453993508, 1.965526015, 2.699817367, 3.808902051, 3.832210779, 5.879966667, 3.438795276, 5.903849858, 2.885327691, 2.488354721, 2.632072677, 1.418500999, 0.466125859, 0.000000000, 1.036451594},
                {2.598014966, 2.415959579, 2.287729350, 2.480853504, 1.813525322, 1.536173738, 2.499933731, 3.895521224, 3.918519336, 6.126329853, 3.736166637, 6.149386650, 3.384330117, 3.059622122, 3.162633074, 2.092505791, 0.835629820, 1.036451594, 0.000000000}
        };
        double[][] expWeight2 = {
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.145161290, 0.145161290, 0.290322581, 0.145161290, 0.145161290, 0.145161290, 0.241935484, 0.048387097, 0.145161290, 0.048387097, 0.241935484, 0.096774194, 0.145161290, 0.145161290, 0.193548387, 0.193548387, 0.193548387, 0.145161290, 0.145161290},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.120967742, 0.120967742, 0.241935484, 0.120967742, 0.120967742, 0.120967742, 0.201612903, 0.040322581, 0.120967742, 0.040322581, 0.201612903, 0.080645161, 0.120967742, 0.120967742, 0.161290323, 0.161290323, 0.161290323, 0.120967742, 0.120967742},
                {0.024193548, 0.024193548, 0.048387097, 0.024193548, 0.024193548, 0.024193548, 0.040322581, 0.008064516, 0.024193548, 0.008064516, 0.040322581, 0.016129032, 0.024193548, 0.024193548, 0.032258065, 0.032258065, 0.032258065, 0.024193548, 0.024193548},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.024193548, 0.024193548, 0.048387097, 0.024193548, 0.024193548, 0.024193548, 0.040322581, 0.008064516, 0.024193548, 0.008064516, 0.040322581, 0.016129032, 0.024193548, 0.024193548, 0.032258065, 0.032258065, 0.032258065, 0.024193548, 0.024193548},
                {0.120967742, 0.120967742, 0.241935484, 0.120967742, 0.120967742, 0.120967742, 0.201612903, 0.040322581, 0.120967742, 0.040322581, 0.201612903, 0.080645161, 0.120967742, 0.120967742, 0.161290323, 0.161290323, 0.161290323, 0.120967742, 0.120967742},
                {0.048387097, 0.048387097, 0.096774194, 0.048387097, 0.048387097, 0.048387097, 0.080645161, 0.016129032, 0.048387097, 0.016129032, 0.080645161, 0.032258065, 0.048387097, 0.048387097, 0.064516129, 0.064516129, 0.064516129, 0.048387097, 0.048387097},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.096774194, 0.096774194, 0.193548387, 0.096774194, 0.096774194, 0.096774194, 0.161290323, 0.032258065, 0.096774194, 0.032258065, 0.161290323, 0.064516129, 0.096774194, 0.096774194, 0.129032258, 0.129032258, 0.129032258, 0.096774194, 0.096774194},
                {0.096774194, 0.096774194, 0.193548387, 0.096774194, 0.096774194, 0.096774194, 0.161290323, 0.032258065, 0.096774194, 0.032258065, 0.161290323, 0.064516129, 0.096774194, 0.096774194, 0.129032258, 0.129032258, 0.129032258, 0.096774194, 0.096774194},
                {0.096774194, 0.096774194, 0.193548387, 0.096774194, 0.096774194, 0.096774194, 0.161290323, 0.032258065, 0.096774194, 0.032258065, 0.161290323, 0.064516129, 0.096774194, 0.096774194, 0.129032258, 0.129032258, 0.129032258, 0.096774194, 0.096774194},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645},
                {0.072580645, 0.072580645, 0.145161290, 0.072580645, 0.072580645, 0.072580645, 0.120967742, 0.024193548, 0.072580645, 0.024193548, 0.120967742, 0.048387097, 0.072580645, 0.072580645, 0.096774194, 0.096774194, 0.096774194, 0.072580645, 0.072580645}
        };

        Map<Integer, Set<Integer>> randomSets2 = new HashMap<>();
        randomSets2.put(12, Stream.of(12, 18, 14, 13, 3).collect(Collectors.toSet()));
        randomSets2.put(8, Stream.of(8, 11, 4, 7, 16).collect(Collectors.toSet()));
        randomSets2.put(1, Stream.of(1, 0, 2, 5, 6).collect(Collectors.toSet()));
        randomSets2.put(17, Stream.of(17, 10, 15, 9).collect(Collectors.toSet()));

        return Stream.of(
                Arguments.arguments(randomSeed, numSet1, numVert1, numEdge1, lambda1, expWeight1,
                        neighborsSet1, randomSets1, recenteredSets1, energy1)
                //Arguments.arguments(randomSeed, numSet2, numVert2, lambda2, expWeight2,
                //        randomSets2)
        );
    }


    @ParameterizedTest
    @MethodSource("values")
    void calcRandomSets_shouldGiveCorrectSets(
            int randomSeed, int numSet, int numVert, int numEdge, double[][] lambda, double[][] expWeight,
            Map<Integer, Set<Integer>> neighborsSet, Map<Integer, Set<Integer>> randomSets,
            Map<Integer, Set<Integer>> recenteredSets, double energy) {
        Random random = new Random(randomSeed);

        Map<Integer, Set<Integer>> myRandomSets = SAHelper.calcRandomSets(numSet, numVert, random);

        assertEquals(randomSets.size(), myRandomSets.size());

        for (int key : randomSets.keySet()) {
            assertTrue(myRandomSets.containsKey(key));

            for (int node : randomSets.get(key)) {
                assertTrue(myRandomSets.get(key).contains(node));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void calcRecenter_shouldGiveCorrectCenter(
            int randomSeed, int numSet, int numVert, int numEdge, double[][] lambda, double[][] expWeight,
            Map<Integer, Set<Integer>> neighborsSet, Map<Integer, Set<Integer>> randomSets,
            Map<Integer, Set<Integer>> recenteredSets, double energy) {
        Map<Integer, Set<Integer>> myRecenteredSets = SAHelper.calcRecenter(randomSets, lambda);

        assertEquals(recenteredSets.size(), myRecenteredSets.size());

        for (int key : myRecenteredSets.keySet()) {
            assertTrue(recenteredSets.containsKey(key));

            for (int node : myRecenteredSets.get(key)) {
                assertTrue(recenteredSets.get(key).contains(node));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void calcEnergy_shouldGiveCorrectEnergy(
            int randomSeed, int numSet, int numVert, int numEdge, double[][] lambda, double[][] expWeight,
            Map<Integer, Set<Integer>> neighborsSet, Map<Integer, Set<Integer>> randomSets,
            Map<Integer, Set<Integer>> recenteredSets, double energy) {
        double myEnergy = SAHelper.calcEnergy(randomSets, neighborsSet, expWeight, numEdge);

        assertEquals(energy, myEnergy, EPSILON);
    }

    public static void main(String[] args) {
        System.out.println("Set 1:");
        List<Integer> candidates1 = Stream.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        ).collect(Collectors.toList());
        Random random1 = new Random(0);
        Collections.shuffle(candidates1, random1);

        for (int i = 0; i < candidates1.size(); i++) {
            System.out.print(candidates1.get(i) + "\t");
        }

        System.out.println();

        Map<Integer, Set<Integer>> neighborsSet1 = new HashMap<>();
        neighborsSet1.put(0, Stream.of(1, 4, 5).collect(Collectors.toSet()));
        neighborsSet1.put(1, Stream.of(0, 2, 4).collect(Collectors.toSet()));
        neighborsSet1.put(2, Stream.of(1, 3, 4).collect(Collectors.toSet()));
        neighborsSet1.put(3, Stream.of(2, 4).collect(Collectors.toSet()));
        neighborsSet1.put(4, Stream.of(0, 1, 2, 3, 5).collect(Collectors.toSet()));
        neighborsSet1.put(5, Stream.of(0, 4, 6, 7).collect(Collectors.toSet()));
        neighborsSet1.put(6, Stream.of(5, 7, 8, 9, 10).collect(Collectors.toSet()));
        neighborsSet1.put(7, Stream.of(5, 6).collect(Collectors.toSet()));
        neighborsSet1.put(8, Stream.of(6, 9).collect(Collectors.toSet()));
        neighborsSet1.put(9, Stream.of(6, 8, 10).collect(Collectors.toSet()));
        neighborsSet1.put(10, Stream.of(6, 9).collect(Collectors.toSet()));

        int numVert1 = neighborsSet1.size();

        double[] vecUnity1 = new double[numVert1];

        for (int i = 0; i < numVert1; i++) {
            vecUnity1[i] = 1.0;
        }

        InitHelper.vecUnity = vecUnity1;

        int numEdge1 = 34;
        int[] nodeDegrees1 = InitHelper.calcNodeDegrees(neighborsSet1);
        double[][] negPassProbs1 = InitHelper.calcNegPassProbs(neighborsSet1, nodeDegrees1);
        double[][] passTime1 = InitHelper.calcPassTime(negPassProbs1);
        double[][] lambda1 = InitHelper.calcLambda(passTime1);
        double[][] expWeight1 = InitHelper.calcExpWeight(nodeDegrees1, numEdge1);

        printDoubleLoops("Lambda 1:", lambda1);
        printDoubleLoops("Weight 1:", expWeight1);

        System.out.println("\n\n\n");
        System.out.println("Set 2:");
        List<Integer> candidates2 = Stream.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18
        ).collect(Collectors.toList());
        Random random2 = new Random(0);
        Collections.shuffle(candidates2, random2);

        for (int i = 0; i < candidates2.size(); i++) {
            System.out.print(candidates2.get(i) + "\t");
        }

        System.out.println();

        Map<Integer, Set<Integer>> neighborsSet2 = new HashMap<>();
        neighborsSet2.put(0, Stream.of(1, 2, 3).collect(Collectors.toSet()));
        neighborsSet2.put(1, Stream.of(0, 2, 4).collect(Collectors.toSet()));
        neighborsSet2.put(2, Stream.of(0, 1, 3, 4, 5, 6).collect(Collectors.toSet()));
        neighborsSet2.put(3, Stream.of(0, 2, 6).collect(Collectors.toSet()));
        neighborsSet2.put(4, Stream.of(1, 2, 18).collect(Collectors.toSet()));
        neighborsSet2.put(5, Stream.of(2, 6, 16).collect(Collectors.toSet()));
        neighborsSet2.put(6, Stream.of(2, 3, 5, 8, 10).collect(Collectors.toSet()));
        neighborsSet2.put(7, Stream.of(8).collect(Collectors.toSet()));
        neighborsSet2.put(8, Stream.of(6, 7, 10).collect(Collectors.toSet()));
        neighborsSet2.put(9, Stream.of(11).collect(Collectors.toSet()));
        neighborsSet2.put(10, Stream.of(6, 8, 11, 12, 14).collect(Collectors.toSet()));
        neighborsSet2.put(11, Stream.of(9, 10).collect(Collectors.toSet()));
        neighborsSet2.put(12, Stream.of(10, 13, 14).collect(Collectors.toSet()));
        neighborsSet2.put(13, Stream.of(12, 14, 15).collect(Collectors.toSet()));
        neighborsSet2.put(14, Stream.of(10, 12, 13, 15).collect(Collectors.toSet()));
        neighborsSet2.put(15, Stream.of(13, 14, 16, 17).collect(Collectors.toSet()));
        neighborsSet2.put(16, Stream.of(5, 15, 17, 18).collect(Collectors.toSet()));
        neighborsSet2.put(17, Stream.of(15, 16, 18).collect(Collectors.toSet()));
        neighborsSet2.put(18, Stream.of(4, 16, 17).collect(Collectors.toSet()));

        int numVert2 = neighborsSet2.size();

        double[] vecUnity2 = new double[numVert2];

        for (int i = 0; i < numVert1; i++) {
            vecUnity2[i] = 1.0;
        }

        InitHelper.vecUnity = vecUnity2;

        int numEdge2 = 62;
        int[] nodeDegree2 = InitHelper.calcNodeDegrees(neighborsSet2);
        double[][] negPassProbs2 = InitHelper.calcNegPassProbs(neighborsSet2, nodeDegree2);
        double[][] passTime2 = InitHelper.calcPassTime(negPassProbs2);
        double[][] lambda2 = InitHelper.calcLambda(passTime2);
        double[][] expWeight2 = InitHelper.calcExpWeight(nodeDegree2, numEdge2);

        printDoubleLoops("Lambda 2:", lambda2);
        printDoubleLoops("Weight 2:", expWeight2);
    }

    public static void printDoubleLoops(String name, double[][] obj) {

        System.out.println(name);
        for (int i = 0; i < obj.length; i++) {
            for (int j = 0; j < obj.length; j++) {
                String msg = String.format("%.9f\t", obj[i][j]);
                System.out.print(msg);
            }
            System.out.println();
        }
    }
}
