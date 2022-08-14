package commkmeans.util;

import org.junit.jupiter.api.Test;

import static commkmeans.util.Constants.EPSILON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParametersTest {
    @Test
    void ctor_shouldCreateWithCorrectValues() {
        double T_max = 100.0;
        double T_min = 0.5;
        int N_max = 100000;
        int N_min = 2;
        double alpha = 0.5;
        int R = 1000;
        int seed = 300;

        Parameters p = new Parameters(T_max, T_min, N_max, N_min, alpha, R, seed);

        assertEquals(T_max, p.T_max, EPSILON);
        assertEquals(T_min, p.T_min, EPSILON);
        assertEquals(N_max, p.N_max);
        assertEquals(N_min, p.N_min);
        assertEquals(alpha, p.alpha, EPSILON);
        assertEquals(R, p.R);
        assertEquals(seed, p.seed);
    }

    @Test
    void ctor_shouldCreateWithCorrectValues2() {
        double T_max = 100.0;
        double T_min = 0.5;
        int N_max = 100000;
        int N_min = 2;
        double alpha = 0.5;
        int R = 1000;

        Parameters p = new Parameters(T_max, T_min, N_max, N_min, alpha, R);

        assertEquals(T_max, p.T_max, EPSILON);
        assertEquals(T_min, p.T_min, EPSILON);
        assertEquals(N_max, p.N_max);
        assertEquals(N_min, p.N_min);
        assertEquals(alpha, p.alpha, EPSILON);
        assertEquals(R, p.R);
        assertEquals(-1, p.seed);
    }

    @Test
    void ctor_nonPegativeTShouldThrowIAE() {
        int N_max = 100000;
        int N_min = 2;
        double alpha = 0.5;
        int R = 1000;

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(-1.0, 1.0, N_max, N_min, alpha, R));

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(1.0, -1.0, N_max, N_min, alpha, R));

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(0.0, 1.0, N_max, N_min, alpha, R));

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(1.0, 0.0, N_max, N_min, alpha, R));
    }

    @Test
    void ctor_TminLargerThanEqualToTmaxShouldThrowIAE() {
        int N_max = 100000;
        int N_min = 2;
        double alpha = 0.5;
        int R = 1000;

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(1.0, 2.0, N_max, N_min, alpha, R));
    }

    @Test
    void ctor_nonPositiveNShouldThrowIAE() {
        double T_max = 2.0;
        double T_min = 1.0;
        double alpha = 0.5;
        int R = 1000;

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, -1, -5, alpha, R));

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, 2, -1, alpha, R));

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, 2, 0, alpha, R));
    }

    @Test
    void ctor_NminLargerThanNmaxShouldThrowIAE() {
        double T_max = 2.0;
        double T_min = 1.0;
        double alpha = 0.5;
        int R = 1000;

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, 2, 3, alpha, R));
    }

    @Test
    void ctor_AlphaNotBetweenZeroAndOneShouldThrowIAE() {
        double T_max = 2.0;
        double T_min = 1.0;
        int N_max = 10000;
        int N_min = 2;
        int R = 1000;

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, N_max, N_min, -0.01, R));

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, N_max, N_min, 1.01, R));
    }

    @Test
    void ctor_nonPositiveRShouldThrowIAE() {
        double T_max = 2.0;
        double T_min = 1.0;
        int N_max = 10000;
        int N_min = 2;
        double alpha = 0.5;

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, N_max, N_min, alpha, -1));

        assertThrows(IllegalArgumentException.class,
                () -> new Parameters(T_max, T_min, N_max, N_min, alpha, 0));
    }
}
