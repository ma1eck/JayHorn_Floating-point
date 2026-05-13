# JayHorn Benchmark Runner

This script automates the parallel execution of JayHorn benchmarks.

## Prerequisites

1. **Python**
2. Install OpenJDK 8:
   - Windows: https://adoptium.net/temurin/releases/?version=8
   - Linux: Use the standard package manager of your distribution
     2.1 **Java Runtime Environment** (available in system `PATH`).
     - Windows: update JAVA_HOME and PATH environment varialbes
     - Linux: update-alternatives --config java
3. **JayHorn JAR**: The `jayhorn.jar` file.
4. **Native Libraries**: The directory containing native solver binaries (e.g., Z3).

## Setup

Place your benchmarks in a directory structure where each benchmark folder contains a `classes` and `src` subdirectory:
`benchmarks_dir/benchmark_name/{classes, src} `

## Usage

The script is executed via `benchmark_runner.py`. It is recommended to run it in PowerShell or Command Prompt.

### Configuration Flags

| Flag                       | Description                                                    | Default                  |
| :------------------------- | :------------------------------------------------------------- | :----------------------- |
| **Required Paths**         |                                                                |                          |
| `--benchmarks-dirs`        | Space-separated paths to benchmark directories.                | `./examples`             |
| `--jayhorn-jar`            | Path to the `jayhorn.jar` file.                                | `./jayhorn.jar`          |
| `--native-lib`             | Path to the directory with native libraries.                   | `./native_lib`           |
| **Execution Control**      |                                                                |                          |
| `--max-workers`            | Number of parallel processes to run.                           | `2`                      |
| `--timeout`                | Maximum time in seconds for a single JayHorn run.              | `60`                     |
| `--repetitions`            | Number of times to run each configuration to average the time. | `1`                      |
| `--solver`                 | The Horn solver to use, spacer or eldarica.                    | `spacer`                 |
| `--rounding-encoding`      | Specify which rounding encodings to use.                       | `{loop-based,loop-free}` |
| `--normalization-encoding` | Specify which normalization encodings to use.                  | `{loop-based,loop-free}` |
| `--symex`                  | Enable using symex backend                                     | `False`                  |
| **Benchmark Selection**    |                                                                |                          |
| `--select`                 | Run only specific benchmarks. (e.g.,`--select B1 B2`)          | `None` (all)             |
| **Output & Logging**       |                                                                |                          |
| `--output-csv`             | Name of the CSV file for results.                              | `benchmark_results.csv`  |
| `--get-cex`                | Enable counter-example generation.                             | `False`                  |

### Examples

This section provides examples of running benchmarks on **PowerShell** and **Linux** .

#### PowerShell

To run benchmarks in parallel with an 8-minute timeout and 3 repetitions on eldrica:

```bash
python benchmark_runner.py --benchmarks-dirs ".\Benchmarks\hand-crafted" --jayhorn-jar ".\jayhorn.jar" --native-lib ".\native_libs" --max-workers 3 --timeout 480 --repetitions 3 --solver eldarica --output-csv output_table.csv
```

To run Double_div and Double_div_bad benchmarks with loop-free rounding encoding, loop-free and loop-based normalization encoding:

```bash
python benchmark_runner.py --rounding-encoding loop-free --normalization-encoding loop-based loop-free --select Double_div Double_div_bad --benchmarks-dirs ./Benchmarks/C-SVCOM
```

To run benchmarks on symex backend of eldarica and generate counter example or model:

```bash
python benchmark_runner.py --solver eldarica --symex --get-cex --benchmarks-dirs ./Benchmarks/C-SVCOM
```

To run C-SVCOM, JAVA-SVCOM, and unbounded benchmarks:

```bash
python benchmark_runner.py --benchmarks-dirs ".\Benchmarks\C-SVCOM" ".\Benchmarks\Java-SVCOM" ".\Benchmarks\java-float_unboundedloop\float_unboundedloop" --timeout 30 --max-workers 1 --solver eldarica
```

#### Runing on Linux

To run benchmarks in parallel with an 8-minute timeout and 3 repetitions on eldrica:

```bash
python3 benchmark_runner.py --benchmarks-dirs ./Benchmarks/C-SVCOM/  --jayhorn-jar ./jayhorn.jar --native-lib ./native_lib --max-workers 3 --timeout 480 --repetitions 3 --solver eldarica --output-csv output_table.csv
```

To run Double_div and Double_div_bad benchmarks with loop-free rounding encoding, loop-free and loop-based normalization encoding:

```bash
python3 benchmark_runner.py --rounding-encoding loop-free --normalization-encoding loop-based loop-free --select Double_div Double_div_bad --benchmarks-dirs ./Benchmarks/C-SVCOM
```

To run benchmarks on symex backend of eldarica and generate counter example or model:

```bash
python3 benchmark_runner.py --solver eldarica --symex --get-cex --benchmarks-dirs ./Benchmarks/Java-SVCOM
```

To run C-SVCOM, JAVA-SVCOM, and unbounded benchmarks:

```bash
python3 benchmark_runner.py --benchmarks-dirs "./Benchmarks/C-SVCOM" "./Benchmarks/Java-SVCOM" "./Benchmarks/java-loat_unboundedloop/float_unboundedloop" --timeout 30 --max-workers 1 --solver eldarica
```

## Output

- **`benchmark_results.csv`**: Aggregated results (times, results, encodings).
- **Log Files**: Individual `.txt` files in each benchmark folder containing the full JayHorn output.
- **Counter-examples**: Saved to a subfolder within the benchmark directory if `--get-cex` is enabled (only in spacer).

# Running jayhonr.jar directly

You can execute `jayhorn.jar` without the benchmark runner script by invoking Java manually from the command line.

### Command Syntacx

```bash
java -Xss1024m -XX:-UseGCOverheadLimit -Xmx13g "-Djava.library.path=.\native_lib" -jar .\jayhorn.jar -j <CLASSES_DIR> -src <SRC_DIR> -rounding-encoding <ENCODING>  -normalization-encoding <ENCODING> -solver <SOLVER> -heap-mode unbounded
```

`-Xss1024m`, `-XX:-UseGCOverheadLimit`, and `-Xmx13g` are VM configuration options.

`<CLASSES_DIR>` is the path to the `class` directory of the benchmark, and `<SRC_DIR>` is the path to the `src` directory.

Rounding encoding and normalization encoding can be either `loop-based` or `loop-free`.

The solver can be either `spacer` or `eldarica`.

When using `eldarica`, you can additionally include the `-symex` flag to select the symex backend; otherwise, the default backend is CEGAR.

### Sample command

```
java -Xss1024m -XX:-UseGCOverheadLimit -Xmx13g "-Djava.library.path=.\native_lib" -jar .\jayhorn.jar -j .\Benchmarks\C-SVCOM\Double_div\classes -src .\Benchmarks\C-SVCOM\Double_div\src -rounding-encoding loop-free -normalization-encoding loop-based -solver eldarica -heap-mode unbounded
```

## Note:

Comprehensive results are provided in the Appendices of the paper.

For reproduction  - Experiments ran on an AMD Ryzen 9 7845HX (12 cores, 24 threads, 3 GHz) with 16 GB DDR5 RAM - Timeout should be set to 3,600 seconds (1 hour). - Please note that the estimated execution time for all benchmarks across various strategies using JayHorn (Eldarica (SymEx, CEGAR) and Spacer is approximately two weeks. - To produce RQ1 results : - Results
```bash  	     
python benchmark_runner.py --benchmarks-dirs ".\Benchmarks\hand-crafted" --timeout 1800 --max-workers 1 --solver eldarica --symex
```
The results are produced in benchmark_results.csv as default

    ### Generating Figure 6: Scatter Plot

    To reproduce the scatter plot shown in Figure 6 of the paper, follow these steps:

    1.**Data Collection & Preparation:**
    			*   For each configuration (e.g., `Config_A`, `Config_B`) and every problem instance, collect the numerical result (e.g., execution time, number of iterations, resource usage).
    			*   For each configuration, sort its collected results (e.g., in ascending order of the primary metric). *Note: While sorting is done per config, the pairing step below relies on instance correspondence, not sorted index.*

    2.**Creating Data Pairs:**
    			*   Construct data pairs $(x, y)$ for the scatter plot. For *each individual problem instance*, $x$ should be the result obtained from `Config_A`, and $y$ should be the result obtained from `Config_B`.
    			*   **Crucially:** Ensure that $x$ and $y$ in each pair correspond to the *same* underlying problem instance.

    3.**Handling Timeouts:**
    			*   **Representation:** Any problem instance that results in a timeout for a given configuration should be assigned a specific maximum predefined value (e.g., the timeout threshold, or a value slightly above the maximum observed non-timeout result).
    			*   **Clarity in Plot:** To prevent multiple timeout points from completely overlapping at the maximum value, consider adding a small amount of "jitter" (a slight random offset) to their assigned maximum value, or use distinct visual markers/colors specifically for timeout points in the plot.

    - To produce RQ2 results :``bash  	     python benchmark_runner.py --benchmarks-dirs ".\Benchmarks\float_unboundedloop" --timeout 1800 --max-workers 1 --solver eldarica 		 ``
    	 The results are produced in benchmark_results.csv as default

    - To produce RQ3 results
    		- For Eldarica (symex)``bash  			python benchmark_runner.py --benchmarks-dirs ".\Benchmarks\C-SVCOM" --timeout 1800 --max-workers 1 --solver eldarica  			``
    		- For Eldarica (CEGAR)
    		``bash  			python benchmark_runner.py --benchmarks-dirs ".\Benchmarks\C-SVCOM" --timeout 1800 --max-workers 1 --solver eldarica --symex 			``
    		- For JBMC
    			- Download JBMC and Put it in the Main directory
    			``bash  				python benchmark_runner.py --jbmc --benchmarks-dirs ".\Benchmarks\C-SVCOM" --timeout 1800 --max-workers 1 				``
    	- To produce RQ4 results
    		- Z3
    		``bash  			python benchmark_runner.py --benchmarks-dirs ".\Benchmarks\float_unboundedloop" --timeout 1800 --max-workers 1 --solver spacer``
