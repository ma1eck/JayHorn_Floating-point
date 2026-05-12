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


        double a=Verifier.nondetDouble();//0.0625;
        Verifier.assume(a > 0);
        for (int i=0; i < 5; i++)
            a *= 2.0;

        assert a+0.2500000000000002  != 2.25;

}
}