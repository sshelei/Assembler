# TOY Assembler

A Java assembler for Princeton's 16-bit TOY computer. It translates a small
symbolic assembly language—with labels and symbolic values—into TOY memory
records. Generated programs can be executed with Princeton's TOY simulator,
which users download directly from its official source page.

The assembler, symbol model, and register/cache allocation code in
`src/main/java` are the original project. The console utilities in
`src/framework/java` are derived from Princeton instructional sources; see
[Third-party notices](THIRD_PARTY_NOTICES.md). The current source tree does not
include `TOY.java`; users download it directly from Princeton.

## What this project demonstrates

- Instruction parsing and encoding for all 16 TOY opcodes
- Symbol, label, register, heap, and spill-cache bookkeeping
- Forward and backward branch generation
- End-to-end assembly and execution of example programs
- Integration of code with separately initialized memory and subroutines

## Requirements

- JDK 17 or newer
- A shell such as Bash, Git Bash, or WSL for the commands below
- `curl`, or a browser for downloading Princeton's `TOY.java`

`In.java`, `StdIn.java`, and `StdOut.java` are vendored under their documented
GPLv3 terms. `TOY.java` must be downloaded separately and is ignored by Git.

## Build

From the repository root, download the simulator into the ignored `vendor`
directory:

```bash
mkdir -p build vendor
curl --fail --location \
  https://introcs.cs.princeton.edu/java/64simulator/TOY.java \
  --output vendor/TOY.java
```

Then compile the retained framework utilities, original assembler, and local
simulator together:

```bash
javac -d build \
  src/framework/java/*.java \
  src/main/java/*.java \
  vendor/TOY.java
```

The framework and application can also be compiled separately, but the second
command must include the compiled framework on its classpath:

```bash
javac -d build src/framework/java/*.java
javac -cp build -d build src/main/java/*.java vendor/TOY.java
```

`In.java` currently produces a deprecation note; it does not prevent a
successful build.

## Quick start

Assemble the sum example at hexadecimal address `30`:

```bash
java -cp build Assembler 30 \
  examples/programs/sum.ass \
  build/sum.toy
```

Run it with the values 2, 4, and 8. A zero terminates input:

```bash
printf '0002\n0004\n0008\n0000\n' | \
  java -cp build TOY build/sum.toy 30
```

The final result is `000E`.

General command forms:

```bash
java -cp build Assembler START_HEX INPUT.ass OUTPUT.toy
java -cp build TOY [--verbose] PROGRAM.toy [START_HEX]
```

The assembler output format is one memory record per line:

```text
30: 7100
31: 82FF
```

The first field is an 8-bit memory address and the second is a 16-bit TOY
instruction or data word.

## Examples

| Program | Start | Verified behavior |
| --- | --- | --- |
| `sum.ass` | `30` | Sums hexadecimal input until zero |
| `fibonacci.ass` | `40` | Prints ten Fibonacci values, `0001` through `0059` |
| `powers2.ass` | `30` | Prints powers of two, `0001` through `4000` |
| `linkedlist.ass` | `30` | Traverses fixture data at `C0`–`CB` and prints `0001`–`0004` |
| `primefactor.ass` | `30` | Uses a GCD routine at `22`–`29` and stores factor `0007` for `005B` |

The linked-list and prime-factor examples require additional initialized
memory. Their exact assembly, composition, and execution commands are in
[examples/README.md](examples/README.md).

The files under `examples/expected` are curated, runnable reference memory
images. They demonstrate intended behavior but are not all byte-for-byte
golden outputs from the current allocator, because valid register and memory
allocation can produce different instruction streams.

## Assembly language

Commas are optional because the parser removes them. A line may begin with a
label.

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

## Repository layout

```text
src/main/java/          original assembler implementation
src/framework/java/     Princeton-derived I/O utilities (GPLv3)
vendor/TOY.java         user-downloaded simulator (ignored by Git)
examples/programs/      symbolic assembly programs
examples/fixtures/      external memory and subroutine records
examples/expected/      complete runnable reference images
docs/                   design and experiment notes
LICENSES/               third-party license texts
```

## Known limitations

- Invalid command-line arguments are not validated before use.
- Blank lines and comments in `.ass` files can cause parsing failures.
- Some debugging diagnostics are printed during normal execution.
- The assembly language cannot place data at an explicit address, so two
  examples compose generated code with separate memory fixtures.
- External binary routines depend on the assembler's current register
  allocation convention.
- There is no automated regression-test suite or CI workflow yet.

## Attribution and licensing

`In.java`, `StdIn.java`, and `StdOut.java` come from the Princeton IntroCS
standard library, which Princeton identifies as GPLv3. The complete license is
included at `LICENSES/GPL-3.0.txt`. `TOY.java` is not tracked in the current
revision and is downloaded by the user. Review
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) before licensing or
distributing the combined application.
