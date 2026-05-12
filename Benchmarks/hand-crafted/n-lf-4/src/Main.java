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

        double one = 1.0;
        double almostOne = Verifier.nondetDouble();
        Verifier.assume(almostOne > 0.999999999999990 && almostOne < 0.999999999999999);
        double tiny = one - almostOne;
        double massive = 1.0E16;
        double result = massive + tiny;
        assert result != 1.0E16;

}
}