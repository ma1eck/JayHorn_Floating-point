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


        double huge = 1.0E16;
        double tiny = Verifier.nondetDouble();
        Verifier.assume(tiny > 3.0 && tiny < 3.9);
        double step1 = huge + tiny;
        double step2 = step1 - huge;
        assert(step2 == 4.0);

}
}