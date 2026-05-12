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


        double a = 0x1.0000000000001p+0;   // 1 + 2^-52
        double b = 1.5;
        double c=Verifier.nondetDouble();
        Verifier.assume(c > 0.0);
        double d = 0x1.0p-54;

        double x1 = a * b;
        double x2 = x1 + c;
        double x3 = x2 * b;
        double x4 = x3 + d;

        assert x4 != 2.250000000000001;

}
}