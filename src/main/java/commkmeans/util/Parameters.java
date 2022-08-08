package commkmeans.util;

public class Parameters {
    public final double t_max;
    public final double t_min;
    public final int N_max;
    public final int N_min;
    public final double alpha;
    public final int R;

    public Parameters(
            double t_max, double t_min, int N_max, int N_min,
            double alpha, int R) {
        this.t_max = t_max;
        this.t_min = t_min;
        this.N_max = N_max;
        this.N_min = N_min;
        this.alpha = alpha;
        this.R = R;
    }
}
