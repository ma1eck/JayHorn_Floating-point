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

        Verifier.assume(a > 100.0 && b > 20.0 && a > b * 2.0);

        double y = a - b;
        double x = 2.0;

        assert(y != 88.5 || y / x != 44.25);

}
}