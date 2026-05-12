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
        Verifier.assume(a > 0.0);
        for (int i =0; i < 2; i++) {
            a += 0.10000000000000003;
        }
        assert (a + 0.25000000000000067 != 2.450000000000001);


}
}