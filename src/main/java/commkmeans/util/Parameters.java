package commkmeans.util;

public class Parameters {
    public final double T_max;
    public final double T_min;
    public final int N_max;
    public final int N_min;
    public final double alpha;
    public final int R;
    public final int seed;

    public Parameters(
            double T_max, double T_min, int N_max, int N_min,
            double alpha, int R, int seed) {
        if (T_max <= 0 || T_min <= 0) {
            String msg = String.format(
                    "T must be positive, received %f, %f instead.", T_min, T_max);
            throw new IllegalArgumentException(msg);
        }

        if (T_max <= T_min) {
            String msg = String.format(
                    "T_max must be strictly greater than T_min, received %f, %f instead.", T_min, T_max);
            throw new IllegalArgumentException(msg);
        }

        if (N_max <= 0 || N_min <= 0) {
            String msg = String.format(
                    "N must be positive, received %d, %d instead.", N_min, N_max);
            throw new IllegalArgumentException(msg);
        }

        if (N_max < N_min) {
            String msg = String.format(
                    "N_max must be greater than N_min, received %d, %d instead.", N_min, N_max);
            throw new IllegalArgumentException(msg);
        }

        if (alpha <= 0.0 || alpha >= 1.0) {
            String msg = String.format(
                    "alpha must be in (0.0, 1.0), received %f instead.", alpha);
            throw new IllegalArgumentException(msg);
        }

        if (R <= 0) {
            String msg = String.format(
                    "R must be positive, received %d instead.", R);
            throw new IllegalArgumentException(msg);
        }

        this.T_max = T_max;
        this.T_min = T_min;
        this.N_max = N_max;
        this.N_min = N_min;
        this.alpha = alpha;
        this.R = R;
        this.seed = seed;
    }

    public Parameters(
            double T_max, double T_min, int N_max, int N_min,
            double alpha, int R) {
        this(T_max, T_min, N_max, N_min, alpha, R, -1);
    }
}
