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
        Verifier.assume(b == 1.125899906842624E15 );
        double x = b;    // Exactly 2^50
        double y = 1.1258999068426242E15;   // Exactly 2^50 + 0.25
        double z = y - x;

        assert  z != 0.25;

}
}