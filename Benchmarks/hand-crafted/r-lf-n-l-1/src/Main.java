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

        double b = 8.0;
        double x = a - b;

        double y = Verifier.nondetDouble();

        if (x == 7.4){
            assert(y / x == 5.874155405405405);
        }

}
}