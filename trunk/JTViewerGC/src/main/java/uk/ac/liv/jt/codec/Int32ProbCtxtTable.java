/*******************************************************************************
 * This Library is :
 * 
 *     Copyright © 2010 Jerome Fuselier and Fabio Corubolo - all rights reserved
 *     jerome.fuselier@gmail.com ; corubolo@gmail.com
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * see COPYING.LESSER.txt
 * 
 * ---
 * 
 * This library used data structures from the JT Specification, that are subject to the JT specification license: 
 * JT_Specification_License.txt
 * 
 ******************************************************************************/
package uk.ac.liv.jt.codec;

import java.io.IOException;

import uk.ac.liv.jt.debug.DebugJTReader;
import uk.ac.liv.jt.format.BitReader;

public class Int32ProbCtxtTable {
	
	/* A Probability Context Table is a trimmed and scaled histogram of the 
	 * input values. It tallies the frequencies of the several most frequently 
	 * occurring values. It is central to the operation of the arithmetic CODEC, 
	 * and gives all theinformation necessary to reconstruct the Huffman codes 
	 * for the Huffman CODEC. p.227
	 */

    private BitReader reader;
    Int32ProbCtxts probCtxt;

    long nbSymbolBits;
    long nbOccCountBits;
    long nbNextContextBits;

    Int32ProbCtxtEntry[] entries;

    public Int32ProbCtxtEntry[] getEntries() {
        return entries;
    }

    public Int32ProbCtxtTable(Int32ProbCtxts probCtxt, BitReader reader) {
        this.reader = reader;
        this.probCtxt = probCtxt;
    }

    public int getTotalCount() {
        int totalCount = 0;

        for (Int32ProbCtxtEntry entrie : entries)
            totalCount += entrie.getOccCount();

        return totalCount;
    }

    public void read(Boolean isFirstTable, int codecType) throws IOException {

        // Header of the table
        long tableEntryCount = reader.readU32(32);
        nbSymbolBits = reader.readU32(6);
        nbOccCountBits = reader.readU32(6);

        probCtxt.nbOccCountBits = nbOccCountBits;

        if (isFirstTable)
            probCtxt.setNbValueBits(reader.readU32(6));

        nbNextContextBits = reader.readU32(6);

        if (isFirstTable)
            probCtxt.setMinValue(reader.readU32(32));

        if (DebugJTReader.debugCodec) {
            System.out.println("Prob Context Table Entry Count: "
                    + tableEntryCount);
            System.out.println("Number symbol bits: " + nbSymbolBits);
            System.out
                    .println("Number occurence count bits: " + nbOccCountBits);
            System.out.println("Number value bits: "
                    + probCtxt.getNbValueBits());
            System.out.println("Number context bits: " + nbNextContextBits);
            System.out.println("Min value: " + probCtxt.getMinValue());
        }

        // Probability Context Table Entries

        entries = new Int32ProbCtxtEntry[(int) tableEntryCount];
        long cumCount = 0;

        for (int i = 0; i < tableEntryCount; i++) {

            long symbol = reader.readU32((int) nbSymbolBits) - 2;
            long occCount = reader.readU32((int) nbOccCountBits);
            long associatedValue = 0;

            if (codecType == Int32Compression.HUFFMAN_CODEC)
                associatedValue = reader.readU32((int) probCtxt
                        .getNbValueBits());
            else if (codecType == Int32Compression.ARITHMETIC_CODEC)
            	// For the first table the associated value is read
                if (isFirstTable) {
                    associatedValue = reader.readU32((int) probCtxt
                            .getNbValueBits()) + probCtxt.getMinValue();
                    probCtxt.assValues.put(symbol, associatedValue);
                // For the second table we take the associated value of the 
                // symbol in the first table
                } else {
                	associatedValue = (long)probCtxt.assValues.get(symbol);
                }

            int nextContext = (int) reader.readU32((int) nbNextContextBits);

            entries[i] = new Int32ProbCtxtEntry(symbol, occCount, cumCount,
                    associatedValue, nextContext);
            cumCount += occCount;

            if (DebugJTReader.debugCodec)
                System.out
                        .println(String
                                .format("%d => Symbol: %d, Occurence Count: %d, Cum Count : %d, Associated Value: %d, Next Context: %d",
                                        i, symbol, occCount,
                                        entries[i].getCumCount(),
                                        associatedValue, nextContext));

        }

    }

    public long getNbValueBits() {
        return probCtxt.getNbValueBits();
    }

    public long getMinValue() {
        return probCtxt.getMinValue();
    }

    // Looks up the index of the context entry that falls just above
    // the accumulated count.
    public Int32ProbCtxtEntry lookupEntryByCumCount(long count) {
        long sum = entries[0].getOccCount();
        int idx = 0;

        while (count >= sum) {
            idx += 1;
            sum += entries[idx].getOccCount();
        }

        return entries[idx];
    }
}
