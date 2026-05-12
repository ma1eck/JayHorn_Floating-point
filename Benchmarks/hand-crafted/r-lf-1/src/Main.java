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


        double a = 2.0;

        a = a * 1.0000000000000002;
        a = a + 0.20000000000000007;

        for (int i = 0; i < 4; i++) {
            a = a / 2.0;
            a = a + 1.1;
        }

        assert a != 2.2;

}
}