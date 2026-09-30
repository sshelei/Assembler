/*
 * Copyright (C) 2026 Sherry Lei
 *
 * This file is part of TOY Assembler.
 *
 * TOY Assembler is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, version 3 of the License.
 *
 * TOY Assembler is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TOY Assembler. If not, see <https://www.gnu.org/licenses/>.
 */

public class Symbol {
    private String symbol; // symbolic name
    private int loc;  // actual in TOY
    private int usedLineNumber;
    private int assignedLineNumber;
   // private int flags;
    //private final int FLAG_ASSIGNED = 0x1;
   // private final int FLAG_USED = 0x2;

    public Symbol() {
        this(-2, "null", 0, 0);
    }

    public Symbol (int addr, String sym, int used, int assign) {
        symbol = sym;
        loc = addr; 
        usedLineNumber = used;
        assignedLineNumber = assign;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getAddress() {
        return loc;
    }

    public void reset() {
        loc = -2;
    }

    public void changeSym (String newSym) {
        symbol = newSym;
    }

    public Symbol changeLoc (int newLoc) {
        loc = newLoc;
        return this;
    }

    public void setAssignedNumber (int addr) {
        assignedLineNumber = addr;
    }

    public void setUsedNumber (int addr) {
        usedLineNumber = addr;
    }

    public int getAssignedNumber () {
        return assignedLineNumber;
    }

    public int getUsedNumber () {
        return usedLineNumber;
    }
    /*
    public int isAssigned() {
        if ((flags & FLAG_ASSIGNED) != 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public int isUsed() {
        if ((flags & FLAG_USED) != 0) {
            return 1;
        } else {
            return 0;
        }
    }
    
    // set assigned flag to 1
    public void setAssigned() {
        flags = flags | FLAG_ASSIGNED;

    } 

    // set used flag to 1
    public void setUsed() {
        flags = flags | FLAG_USED;
    }
     */
}
