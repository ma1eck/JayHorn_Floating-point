#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import subprocess
import csv
import re
import time
import argparse
from concurrent.futures import ProcessPoolExecutor, as_completed
from functools import partial

# --- Constants ---
LOOP_BASED = "loop-based"
LOOP_FREE = "loop-free"
DEFAULT_ENCODINGS = [LOOP_BASED, LOOP_FREE]



def run_single_benchmark(task_info, args):
    """
    Runs a single benchmark configuration and returns the results.
    This function is designed to be executed in a separate process.
    """
    base_dir, folder_name, rounding_enc, norm_enc = task_info
    folder_path = os.path.join(base_dir, folder_name)

    classes_dir = os.path.join(folder_path, "classes")
    src_dir = os.path.join(folder_path, "src")

    output_filename = f"output_R_{rounding_enc}_N_{norm_enc}.txt"
    output_file_path = os.path.join(folder_path, output_filename)

    # Basic check to see if it's a valid benchmark folder
    if not (os.path.isdir(classes_dir) and os.path.isdir(src_dir)):
        return None

    cmd = [
        "java",
        "-Xss1024m",
        "-XX:-UseGCOverheadLimit",
        "-Xmx13g",
        f"-Djava.library.path={args.native_lib}",
        "-jar", args.jayhorn_jar,
        "-j", classes_dir,
        "-src", src_dir,
        "-rounding-encoding", rounding_enc,
        "-normalization-encoding", norm_enc,
        "-solver", args.solver,
        "-heap-mode", "unbounded"
    ]
    
    if args.symex:
        cmd += ["-symex"]

    if args.get_cex:
        cex_path = os.path.join(folder_path, args.cex_dir_name,
                                f"rounding_{rounding_enc}_normalization_{norm_enc}.txt")
        # Ensure the directory for the counter-example exists
        os.makedirs(os.path.dirname(cex_path), exist_ok=True)
        cmd += ["-solution", "-full-cex", "-print-horn", "-cex-path", cex_path]

    env = os.environ.copy()
    env["PATH"] = args.native_lib + os.pathsep + env["PATH"]

    total_times = []
    solver_times = []
    result = "UNKNOWN"
    stdout_all_runs = ""

    for i in range(args.repetitions):
        start_wall_clock = time.time()
        try:
            # Use Popen to have better control over the process
            process = subprocess.Popen(
                cmd,
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                env=env,
                text=True,
                encoding="utf-8",
                errors="replace"
            )

            try:
                run_stdout, _ = process.communicate(timeout=args.timeout)
                end_wall_clock = time.time()

                total_time_ms = (end_wall_clock - start_wall_clock) * 1000
                total_times.append(total_time_ms)
                
                solver_time_ms = None
                for line in run_stdout.splitlines():
                    if "takes" in line and solver_time_ms == None: 
                        match = re.search(r'takes\s+([\d.]+)', line)
                        if match:
                            solver_time_ms = float(match.group(1)) # Assuming time is already in ms
                    
                    clean_line = line.strip()
                    if clean_line in ("SAFE", "UNSAFE"):
                        result = clean_line

                if solver_time_ms is not None:
                    solver_times.append(solver_time_ms)

                stdout_all_runs += f"\n\n=== RUN {i+1} / {args.repetitions} ===\n{run_stdout}"

            except subprocess.TimeoutExpired:
                process.kill()
                process.wait()
                result = "TIMEOUT"
                total_times.append(args.timeout * 1000)
                stdout_all_runs += f"\n\n=== RUN {i+1} / {args.repetitions} TIMEOUT ===\n"
                # If one run times out, no point in continuing repetitions
                break

        except Exception as e:
            result = "ERROR"
            stdout_all_runs += f"\n\n=== ERROR during execution ===\n{str(e)}"
            break

    avg_total = round(sum(total_times) / len(total_times), 2) if total_times else ""
    avg_solver = round(sum(solver_times) / len(solver_times), 2) if solver_times else ""

    try:
        with open(output_file_path, "w", encoding="utf-8", errors="replace") as f:
            f.write(stdout_all_runs)
    except Exception as e:
        print(f"Warning: Failed to write {output_filename} for {folder_name}: {e}")

    return [folder_name, rounding_enc, norm_enc, avg_total, result, avg_solver]


def parse_arguments():
    """Parses command-line arguments."""
    parser = argparse.ArgumentParser(
        description="Run JayHorn benchmarks in parallel.",
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )

    parser.add_argument(
        '--benchmarks-dirs',
        nargs='+',
        default=[r"./examples/benchmarks"],
        help="List of directories containing benchmarks."
    )
    parser.add_argument(
        '--native-lib',
        default=r".\native_lib",
        help="Path to the directory containing native libraries (e.g., Z3)."
    )
    parser.add_argument(
        '--jayhorn-jar',
        default=r".\jayhorn.jar",
        help="Path to the JayHorn JAR file."
    )
    parser.add_argument(
        '--output-csv',
        default='benchmark_results.csv',
        help="Path to the output CSV file for results."
    )
    parser.add_argument(
        '--solver',
        choices=["spacer", "eldarica"],
        default="spacer",
        help="The solver to be used by JayHorn."
    )
    parser.add_argument(
        '--normalization-encoding',
        nargs='+',  
        choices=[LOOP_BASED, LOOP_FREE],
        default=DEFAULT_ENCODINGS,
        help=(
            "Specify which normalization encodings to test. "
            "Provide one or two space-separated values. "
            f"Choices: {{{LOOP_BASED}, {LOOP_FREE}}}. "
            f"Default: {LOOP_BASED} {LOOP_FREE}"
        )
    )

    parser.add_argument(
        '--rounding-encoding',
        nargs='+',  
        choices=[LOOP_BASED, LOOP_FREE],
        default=DEFAULT_ENCODINGS,
        help=(
            "Specify which rounding encodings to test. "
            "Provide one or two space-separated values. "
            f"Choices: {{{LOOP_BASED}, {LOOP_FREE}}}. "
            f"Default: {LOOP_BASED} {LOOP_FREE}"
        )
    )
    parser.add_argument(
        '--symex',
        action='store_true',
        dest='symex',
        help="Enable using symex in backend (used in eldarica)."
    )
    parser.set_defaults(symex=False)
    parser.add_argument(
        '--timeout',
        type=int,
        default=60,
        help="Timeout for each benchmark run in seconds."
    )
    parser.add_argument(
        '--max-workers',
        type=int,
        default=2,
        help="Maximum number of parallel processes to use."
    )
    parser.add_argument(
        '--cex-dir-name',
        default="counter_examples_or_models",
        help="Name of the subdirectory to store counter-examples or models (only used in spacer solver)."
    )
    parser.add_argument(
        '--get-cex',
        action='store_true',
        dest='get_cex',
        help="Enable generation of counter-examples/models."
    )
    parser.set_defaults(get_cex=False)

    parser.add_argument(
        '--repetitions',
        type=int,
        default=1,
        help="Number of times to repeat each benchmark run for averaging."
    )
    parser.add_argument(
        '--select',
        nargs='*',
        default=[],
        help="Run only specific benchmark folder names. If not provided, all benchmarks in the specified directories are run."
    )

    return parser.parse_args()


def main():
    """Main function to set up and run the benchmarks."""
    args = parse_arguments()
    
    tasks = []
    benchmarks_to_run = set(args.select)

    for b_dir in args.benchmarks_dirs:
        if not os.path.exists(b_dir):
            print(f"Warning: Directory not found, skipping: {b_dir}")
            continue
        
        for folder in os.listdir(b_dir):
            folder_path = os.path.join(b_dir, folder)
            if not os.path.isdir(folder_path):
                continue
            
            # If a selection is made, only include folders from that selection
            if benchmarks_to_run and folder not in benchmarks_to_run:
                continue

            # Create tasks for each combination of encoding
            for rounding_enc in args.rounding_encoding:
                for norm_enc in args.normalization_encoding:
                    tasks.append((b_dir, folder, rounding_enc, norm_enc))

    if not tasks:
        print("No tasks to run. Check your --benchmarks-dirs or --select arguments.")
        return

    print(f"Starting parallel run for {len(tasks)} tasks.")
    print(f"Number of parallel workers: {args.max_workers}")
    print(f"Timeout set to {args.timeout / 60:.2f} minutes per task.")
    if args.repetitions > 1:
        print(f"Averaging results over {args.repetitions} repetitions per task.")

    results_data = []
    
    # Use partial to pass the 'args' object to the worker function
    worker_func = partial(run_single_benchmark, args=args)
    
    with ProcessPoolExecutor(max_workers=args.max_workers) as executor:
        try:
            future_to_task = {executor.submit(worker_func, t): t for t in tasks}

            for i, future in enumerate(as_completed(future_to_task), 1):
                res = future.result()
                if res:
                    results_data.append(res)
                    print(f"({i}/{len(tasks)}) Finished: {res[0]} [R: {res[1]}, N: {res[2]}] -> {res[4]} | Total: {res[3]}ms | Solver: {res[5]}ms")
        except KeyboardInterrupt:
            print("\nCtrl+C detected. Terminating workers...")
            # cancel_futures is available in Python 3.9+
            executor.shutdown(wait=False, cancel_futures=True)
            print("Workers terminated.")
            return

    headers = ['Benchmark Name', 'Rounding', 'Normalization', 'Total Time (ms)', 'Result', 'Solver Time (ms)']
    
    # Sort results for consistent output
    results_data.sort(key=lambda x: (x[0], x[1], x[2]))
    
    try:
        with open(args.output_csv, mode='w', newline='', encoding='utf-8') as file:
            writer = csv.writer(file)
            writer.writerow(headers)
            writer.writerows(results_data)
        print(f"\nAll processing complete. Results written to: {args.output_csv}")
    except IOError as e:
        print(f"\nError writing to CSV file: {e}")


if __name__ == "__main__":
    main()
