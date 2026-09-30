# Examples

This directory separates symbolic input programs from supporting memory and
runnable reference images:

```text
programs/    input accepted by Assembler
fixtures/    memory records not expressible in the current assembly syntax
expected/    complete, runnable reference images
```

Download Princeton's simulator and build the Java classes from the repository
root before running these examples:

```bash
mkdir -p build vendor
curl --fail --location \
  https://introcs.cs.princeton.edu/java/64simulator/TOY.java \
  --output vendor/TOY.java

javac -d build \
  src/framework/java/*.java \
  src/main/java/*.java \
  vendor/TOY.java
```

`vendor/` is ignored by Git; the downloaded simulator is not tracked in the
current revision.

## Self-contained programs

### Sum

```bash
java -cp build Assembler 30 \
  examples/programs/sum.ass \
  build/sum.toy

printf '0002\n0004\n0008\n0000\n' | \
  java -cp build TOY build/sum.toy 30
```

Expected final output: `000E`.

### Fibonacci

```bash
java -cp build Assembler 40 \
  examples/programs/fibonacci.ass \
  build/fibonacci.toy

java -cp build TOY build/fibonacci.toy 40
```

The program prints ten values from `0001` through `0059`.

### Powers of two

```bash
java -cp build Assembler 30 \
  examples/programs/powers2.ass \
  build/powers2.toy

java -cp build TOY build/powers2.toy 30
```

The program prints powers of two from `0001` through `4000`.

## Programs with fixtures

The assembler currently supports labeled data values but cannot place data at
an explicitly requested memory address. These examples therefore append TOY
memory records after assembly.

### Linked-list traversal

`linkedlist.ass` expects four two-word nodes beginning at `C0`. The fixture
stores each node as `[value, next-address]`.

```bash
java -cp build Assembler 30 \
  examples/programs/linkedlist.ass \
  build/linkedlist-code.toy

cat build/linkedlist-code.toy \
    examples/fixtures/linkedlist-memory.toy \
    > build/linkedlist-complete.toy

java -cp build TOY build/linkedlist-complete.toy 30
```

Expected output:

```text
0001
0002
0003
0004
```

`expected/linkedlist.toy` is an older hand-allocated reference image. It uses
start address `10` and list data at `D0`, so it is behaviorally equivalent but
not a byte-for-byte expected output for the current source and fixture.

### Prime-factor search

`primefactor.ass` searches for a factor of `005B` (hexadecimal 91). It calls a
GCD routine stored at addresses `22`–`29`. The current allocator stores the
return address in `R1`, so the routine returns with instruction `E100`.

```bash
java -cp build Assembler 30 \
  examples/programs/primefactor.ass \
  build/primefactor-code.toy

cat build/primefactor-code.toy \
    examples/fixtures/prime-gcd-routine.toy \
    > build/primefactor-complete.toy

java -cp build TOY --verbose build/primefactor-complete.toy 30
```

The program has no normal terminal output. In the final core dump, memory
address `DA` contains `0007`, a factor of `005B`.

The fixture depends on the register allocation used by the generated code:

- `F122` (link through `R1`) requires return instruction `E100`.
- `F722` (link through `R7`) requires return instruction `E700`.

This coupling is a known limitation and should eventually be replaced by an
explicit calling convention or assembler-supported subroutine labels.

## Reference images

Files under `expected/` are complete programs that can be loaded directly by
`TOY`. They are curated behavioral references, not an automated golden-file
suite. The allocator may emit a different but equivalent memory image.
