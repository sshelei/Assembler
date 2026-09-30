# Third-party notices

This project separates its original assembler implementation from instructional
framework code obtained from the Princeton IntroCS materials.

This notice documents provenance and practical compliance steps; it is not
legal advice.

## Princeton standard-library files

The following tracked files are not original to this repository:

| Local file | Upstream source | Authors |
| --- | --- | --- |
| `src/framework/java/In.java` | [Princeton `In.java`](https://introcs.cs.princeton.edu/java/stdlib/In.java) | David Pritchard, Robert Sedgewick, and Kevin Wayne |
| `src/framework/java/StdIn.java` | [Princeton `StdIn.java`](https://introcs.cs.princeton.edu/java/stdlib/StdIn.java) | Robert Sedgewick, Kevin Wayne, and David Pritchard |
| `src/framework/java/StdOut.java` | [Princeton `StdOut.java`](https://introcs.cs.princeton.edu/java/stdlib/StdOut.java) | Robert Sedgewick and Kevin Wayne |

The local copies retain their upstream authorship and documentation. Each file
contains a dated notice describing its local formatting or warning-suppression
changes, as required for redistribution of modified GPL-covered source.

Princeton's [standard-library page](https://introcs.cs.princeton.edu/java/stdlib/)
states that `stdlib.jar` is distributed under the GNU General Public License,
version 3 (GPLv3). The complete, unmodified license text is included in the
root `COPYING` file, with an additional copy at `LICENSES/GPL-3.0.txt`, and is
also available from the
[GNU Project](https://www.gnu.org/licenses/gpl-3.0.txt).

The GPL is a copyleft license, not merely an attribution requirement. The
[GNU licensing FAQ](https://www.gnu.org/licenses/gpl-faq.html) discusses the
requirements for distributing a combined application that uses GPL-covered
code.

## Externally downloaded TOY simulator

The current revision does not include `TOY.java`. Users download it directly
from Princeton's official source:

<https://introcs.cs.princeton.edu/java/64simulator/TOY.java>

The documented setup saves it as `vendor/TOY.java`; `vendor/` is ignored by
Git. Princeton remains the source of the simulator, and users should review
the terms provided by Princeton before redistributing their downloaded copy or
a combined binary.

Earlier commits in this repository included a copy of `TOY.java`. Removing a
file from the current revision does not remove it from Git history. Eliminating
those historical objects would require rewriting repository history and
force-pushing every affected branch.

## Original project code

The following tracked files contain the original assembler implementation:

- `src/main/java/Assembler.java`
- `src/main/java/Symbol.java`
- `src/main/java/SymbolTable.java`

Copyright (C) 2026 Sherry Lei.

These files, along with the project's assembly examples and documentation, are
released as part of TOY Assembler under the GNU General Public License, version
3 only. Each Java source file contains the copying-permission and warranty
notices recommended by the GNU Project. The assembly syntax does not support
comments, so the project-wide notice in `README.md` identifies the license for
the `.ass` files.
