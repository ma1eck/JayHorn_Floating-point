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

        double a = Verifier.nondetDouble();
        double tiny = 1e-12; // Extremely small number
        double x = a - tiny;

        double y = Verifier.nondetDouble();


        if (x == 4999.999999999999) {
            assert(y / x == 2.0);
        }

}
}