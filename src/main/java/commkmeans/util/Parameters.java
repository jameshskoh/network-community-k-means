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
