# TOY Assembler

This repository contains a Java assembler and simulator for Princeton's TOY
computer. `Assembler` translates the symbolic assembly syntax used by the
included `.ass` files into TOY memory records. `TOY` loads and executes those
records.

The stable baseline is commit `3825d90`. Experimental register-allocation and
symbol-tracking work is preserved separately on the `improvements` branch and
is described in [IMPROVEMENTS.md](IMPROVEMENTS.md).

## Requirements

- A Java Development Kit (JDK). Java 17 or newer is recommended.
- A shell for the examples below. On Windows, Git Bash or WSL works.

No third-party Java libraries are required.

## Build

Compile into a separate directory so that generated `.class` files do not
replace the class files stored in the repository:

```bash
mkdir -p build
javac -d build *.java
```

The compiler currently reports a deprecation note for `In.java`; it does not
prevent compilation.

## Assemble a program

```bash
java -cp build Assembler START_ADDRESS INPUT.ass OUTPUT.toy
```

`START_ADDRESS` is hexadecimal. For example:

```bash
java -cp build Assembler 30 sum.ass sum-generated.toy
```

The generated file contains one 16-bit TOY word per line:

```text
30: 7100
31: 82FF
```

The two digits before the colon are the memory address, and the four digits
after it are the instruction or data word.

## Run a generated program

Pass the generated file and the same hexadecimal starting address to the TOY
simulator:

```bash
java -cp build TOY sum-generated.toy 30
```

TOY input values are four-digit hexadecimal words. This example adds 2, 4,
and 8; the final zero ends input:

```bash
printf '0002\n0004\n0008\n0000\n' | \
  java -cp build TOY sum-generated.toy 30
```

The result is `000E`.

Use `-v` or `--verbose` to display memory and register dumps:

```bash
java -cp build TOY --verbose sum-generated.toy 30
```

## Assembly syntax

Commas are optional because the assembler removes them before parsing. A line
may begin with a label.

| Mnemonic | Operands | Meaning |
| --- | --- | --- |
| `H` | none | Halt |
| `A` | `d, s, t` | Add |
| `S` | `d, s, t` | Subtract |
| `BA` | `d, s, t` | Bitwise AND |
| `BX` | `d, s, t` | Bitwise XOR |
| `LS` | `d, s, t` | Shift left |
| `RS` | `d, s, t` | Shift right |
| `LA` | `d, value` | Load an address/immediate value |
| `L` | `d, name` | Load from memory; `stdin` reads input |
| `ST` | `d, name` | Store to memory; `stdout` prints output |
| `LI` | `d, 0, t` | Load indirectly through `t` |
| `SI` | `d, 0, t` | Store indirectly through `t` |
| `BZ` | `d, label` | Branch when `d` is zero |
| `BP` | `d, label` | Branch when `d` is positive |
| `JR` | `d` | Jump indirectly through `d` |
| `JL` | `d, address` | Jump and save the return address in `d` |

A data definition consists of a label and a hexadecimal word:

```text
N    000A
```

The parser does not currently support comments or blank lines in `.ass`
files. Keep every source line nonempty.

## Included programs and verified behavior

The following checks were performed by assembling into temporary files and
then running those files in `TOY`:

| Source | Start | Result |
| --- | --- | --- |
| `fibonacci.ass` | `40` | Prints ten values from `0001` through `0059` |
| `powers2.ass` | `30` | Prints powers of two from `0001` through `4000` |
| `sum.ass` | `30` | Correctly sums hexadecimal input until zero |
| `linkedlist.ass` | `30` | Assembles after its trailing blank line is removed; requires list data at `D0`-`DF` |
| `primeTest.ass` | `30` | Assembles; requires a subroutine at `22`-`29` |
| `GCD.ass` | — | Unfinished and not part of the verified set |

`linkedlist.toy` contains sample linked-list memory at `D0`-`DF`.
`primeTest.toy` contains the additional routine at `22`-`29`. The corresponding
`.ass` sources do not emit those external memory regions by themselves.

## Project files

- `Assembler.java` — parser, symbol handling, register allocation, and TOY
  record generation.
- `Symbol.java` and `SymbolTable.java` — symbols, registers, heap, and cache
  bookkeeping.
- `TOY.java` — TOY virtual machine and loader.
- `In.java`, `StdIn.java`, and `StdOut.java` — input/output utilities.
- `*.ass` — symbolic assembly examples.
- `*.toy` — assembled programs and experiments.

## Known limitations

- Invalid command-line arguments are not validated before use.
- Blank lines and comments in assembly source can cause parsing failures.
- Some diagnostics are printed during normal assembly and simulation.
- Programs that depend on preloaded memory or library routines are not
  self-contained in their `.ass` files.
- There is no automated regression-test suite yet.

