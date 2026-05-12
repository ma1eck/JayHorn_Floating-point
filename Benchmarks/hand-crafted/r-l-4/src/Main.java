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


        double b = Verifier.nondetDouble();
        Verifier.assume(b > 0.0);
        double x = 1.0000000000000002;
        double d = b + x*0.0625;
        double e = d + x*0.125;
        double f = e + x * 0.25;

        assert (f != 1.4375);

}
}