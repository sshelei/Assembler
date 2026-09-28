# linkedlist.ass
Generate the machine code translation:
```bash
java -cp build Assembler 30 \
  examples/programs/linkedlist.ass \
  build/linkedlist-code.toy
```
Combine the generated code and fixture:
```bash
cat build/linkedlist-code.toy \
    examples/fixtures/linkedlist-memory.toy \
    > build/linkedlist-complete.toy
```
Then run:
```bash
java -cp build TOY build/linkedlist-complete.toy 30
```
Check output with:
```bash
java -cp build TOY examples/expected/linkedlist.toy 10
```
# primefactor.ass
Generate the machine code translation:
```bash
java -cp build Assembler 30 \
  examples/programs/primefactor.ass \
  build/prime-code.toy
```
Combine the generated code and routine:
```bash
cat build/prime-code.toy \
    examples/fixtures/prime-gcd-routine.toy \
    > build/prime-complete.toy
```
Then run:
```bash
java -cp build TOY --verbose build/prime-complete.toy 30
```
Check output with
```bash
java -cp build TOY --verbose examples/expected/primefactor.toy 30
```