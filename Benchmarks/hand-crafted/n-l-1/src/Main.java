/*
 * Origin of the benchmark:
 *     license: MIT (see /java/jayhorn-recursive/LICENSE)
 *     repo: https://github.com/jayhorn/cav_experiments.git
 *     branch: master
 *     root directory: benchmarks/recursive
 * The benchmark was taken from the repo: 24 January 2018
 */
import org.sosy_lab.sv_benchmarks.Verifier;
public class Main {
    public static void main(String[] args) {

        double a = 1.0;
        for (int i = 0; i < 3; i++) {
            double t = a / 3.0;
            a = t + 1.0;
            a = a - 1.0;
        }
        assert(a != 0.03703703703703698);

}
}