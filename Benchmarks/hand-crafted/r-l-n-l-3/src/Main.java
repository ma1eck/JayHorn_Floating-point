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
        double b = Verifier.nondetDouble();
        Verifier.assume(a > 100000.0);
        double y = a - b;
        assert(y != 9.094947017729282E-13);

}
}