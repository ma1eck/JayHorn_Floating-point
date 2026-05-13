# JayHorn Benchmark Runner

This script automates the parallel execution of JayHorn benchmarks.

## Prerequisites

1. **Python**
2. **Java Runtime Environment** (available in system `PATH`).
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

#### PowerShell

To run benchmarks in parallel with an 8-minute timeout and 3 repetitions on eldrica:

```bash
python benchmark_runner.py --benchmarks-dirs "./examples/benchmarks" --jayhorn-jar "./jayhorn.jar" --native-lib "./native_libs" --max-workers 3 --timeout 480 --repetitions 3 --solver eldarica --output-csv output_table.csv
```

To run Double_div and Double_div_bad benchmarks with loop-free rounding encoding, loop-free and loop-based normalization encoding:

```bash
python benchmark_runner.py --rounding-encoding loop-free --normalization-encoding loop-based loop-free --select Double_div Double_div_bad
```

To run benchmarks on symex backend of eldarica and generate counter example or model:

```bash
python benchmark_runner.py --solver eldarica --symex --get-cex
```

To run C-SVCOM, JAVA-SVCOM, and unbounded benchmarks:

```bash
python benchmark_runner.py --benchmarks-dirs ".\examples\C-SVCOM" ".\examples\Java-SVCOM" ".\examples\sv-benchmarks-main-java-float_unboundedloop\float_unboundedloop" --timeout 30 --max-workers 1 --solver eldarica
```

#### Runing on Linux

To run benchmarks in parallel with an 8-minute timeout and 3 repetitions on eldrica:

```bash
python3 benchmark_runner.py --benchmarks-dirs ".\benchmarks" --jayhorn-jar ".\jayhorn.jar" --native-lib ".\native_libs" --max-workers 3 --timeout 480 --repetitions 3 --solver eldarica --output-csv output_table.csv
```

```bash
python benchmark_runner.py --benchmarks-dirs "./examples/benchmarks" --jayhorn-jar "./jayhorn.jar" --native-lib
"./native_libs" --max-workers 3 --timeout 480 --repetitions 3 --solver eldarica --output-csv output_table.csv
```

## Output

- **`benchmark_results.csv`**: Aggregated results (times, results, encodings).
- **Log Files**: Individual `.txt` files in each benchmark folder containing the full JayHorn output.
- **Counter-examples**: Saved to a subfolder within the benchmark directory if `--get-cex` is enabled (only in spacer).

## Note:

Comprehensive results are provided in the Appendices. For reproduction, the timeout should be set to 3,600 seconds (1 hour). Please note that the estimated execution time for all benchmarks across various strategies using Eldarica and Spacer is approximately five days.
