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


        double a=0.0;
        double d = Verifier.nondetDouble();
        Verifier.assume(d > 0.0);
        for (int i=0; i<5; i++){
            a += d;
        }
        assert  a + 0.09375000000000007 != 1.34375;

}
}