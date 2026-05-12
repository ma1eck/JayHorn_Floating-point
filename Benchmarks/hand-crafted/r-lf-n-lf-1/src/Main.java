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
        Verifier.assume(tiny > 4.5 && tiny < 5.2);
        double recovered = 0.0;
        for(int i = 0; i < 3; i++) {

            double temp = huge + tiny;
            recovered = temp - huge;

            huge = huge / 2.0;
        }
        assert(recovered == 5.0);

}
}