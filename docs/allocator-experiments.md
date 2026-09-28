# Experimental improvements

The `improvements` branch preserves the work that was present in the original
working directory before the stable `main` branch was published. The original
snapshot is commit `5d05dc6`, and the branch is checked out in the sibling
directory `Assembler-improvements`.

This branch is experimental. It has not been merged into `main` or pushed to
GitHub because its larger allocator changes need more testing.

## What changed

### Register and cache allocation

- Reduced the assembler's managed register count from seven to five.
- Added a `lookAhead` path that tries to reuse an operand register for a
  result before spilling another value.
- Expanded cache load/store generation and register replacement logic.
- Added symbol-aware cache and heap lookup to reduce duplicate allocations.
- Added buffer-rewriting logic intended to keep cached values valid across
  loops.

### Labels and control flow

- Added an initial `setLoopMarkers` pass to discover labels and referenced
  data before instruction generation.
- Added logic for relocating labels and inserting jumps when emitted
  instructions no longer match their original source positions.
- Added branch-loop handling that can adjust buffered instructions and symbol
  locations.

### Symbol tracking

- Changed each `Symbol` from one assigned/used line number to lists of all
  assigned and used line numbers.
- Added methods to copy, replace, and adjust those histories when instructions
  move.
- Extended `SymbolTable` with symbol lookup, relocation, cache reuse, and
  alternate-symbol helpers.

### Program experiments

- Changed the Fibonacci example to start with `0, 1` and run for twelve
  iterations.
- Renamed the prime-test jump-link symbol from `store` to `func`.
- Added many Fibonacci and prime-test `.toy` variants recording allocator and
  control-flow experiments.
- Updated the small `test.java` scratch program to exercise the expanded
  symbol-history operations.
- Disabled several normal load/store trace messages in `TOY.java` while
  retaining commented debugging statements.

## Current verification status

The branch compiles, but it is not yet a replacement for `main`:

- `fibonacci.ass` assembles and runs.
- A standalone one-token instruction such as `H` can make the label pre-pass
  access a nonexistent second token and throw
  `ArrayIndexOutOfBoundsException`.
- Because of that issue, `sum.ass`, `powers2.ass`, `primeTest.ass`, and
  `linkedlist.ass` did not assemble during the branch-level regression check.
- Several `.toy` files are intermediate experiments rather than expected
  outputs or automated fixtures.

## Suggested path to integration

1. Make the parser explicitly skip blank lines and validate token counts.
2. Add tests for every opcode, forward/backward labels, data definitions,
   register spilling, and loop relocation.
3. Convert useful `.toy` variants into named test fixtures and remove obsolete
   experiments.
4. Compare each generated program with its expected records and execute it in
   `TOY`.
5. Merge allocator changes into `main` in small, independently tested commits.

Useful comparison commands:

```bash
git diff main..improvements -- '*.java'
git diff --stat main..improvements
```
