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

        double b = Verifier.nondetDouble();  // Random value
        Verifier.assume(b == 1.0 );
        double x = b;

        double y = 0.99999999999999988898;
        double z = x - y;

        assert  z == 1.1102230246251565E-16;

}
}