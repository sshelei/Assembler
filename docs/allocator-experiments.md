# Allocator design experiments

During development, an experimental implementation explored more aggressive
register reuse, spilling, and loop-aware relocation. It was not merged into
`main` because its larger allocator changes introduced parser and control-flow
regressions. This document preserves the design ideas and test findings without
presenting that implementation as production-ready code.

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
- Produced multiple Fibonacci and prime-factor `.toy` variants while tracing
  allocator and control-flow behavior. Those scratch artifacts are intentionally
  excluded from the public repository.
- Updated the small `test.java` scratch program to exercise the expanded
  symbol-history operations.
- Experimented locally with reduced simulator tracing. Those changes were not
  retained because `TOY.java` is now downloaded directly from Princeton.

## Verification status at the end of the experiment

The branch compiles, but it is not yet a replacement for `main`:

- `fibonacci.ass` assembles and runs.
- A standalone one-token instruction such as `H` can make the label pre-pass
  access a nonexistent second token and throw
  `ArrayIndexOutOfBoundsException`.
- Because of that issue, the sum, powers-of-two, prime-factor, and linked-list
  programs did not assemble during the experimental regression check.
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
5. Integrate allocator changes in small, independently tested commits only
   after the behavior matches the examples under `examples/programs`.
