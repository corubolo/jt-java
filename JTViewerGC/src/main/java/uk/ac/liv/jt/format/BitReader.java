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
package uk.ac.liv.jt.format;

import java.io.IOException;
import java.nio.ByteBuffer;


public class BitReader {
	
	/* Give a way to read a ByteBuffer "bit by bit".
	 * The ByteReader given at the creation is used to read bytes and the 
	 * readU32(nbBits) method read nbbits bits in the bytes read.
	 */

    private ByteReader reader;
    BitBuffer bitBuf;

    public BitReader(ByteReader reader) {
        this.reader = reader;
        bitBuf = new BitBuffer(ByteBuffer.wrap(new byte[0]));
    }

    public int getNbBitsLeft() {
        return (int) (bitBuf.getBitBufBitSize() - bitBuf.getBitPos());
    }

    /* Read an U32 encoded on nbBits bits */
    public long readU32(int nbBits) throws IOException {
        if (nbBits == 0)
            return 0;

        int nbLeft = getNbBitsLeft();

        // If there are not enough bits already read and stored in bitBuf we 
        // read additional bytes and update the bitBuffer
        if (nbLeft < nbBits) {
            int nbBytes = ((nbBits - nbLeft - 1) / 8) + 1;
            int sizeBytes = nbBytes;
            int cpt = 0;

            if (nbLeft != 0)
                sizeBytes += 1;

            byte[] byteBuf = new byte[sizeBytes];

            if (nbLeft != 0) {
                byte remainingByte = bitBuf.readAsByte(nbLeft);
                byteBuf[cpt] = remainingByte;
                cpt += 1;
            }

            byte[] tmpBytes = reader.readBytes(nbBytes);

            for (int i = cpt; i < sizeBytes; i++)
                byteBuf[i] = tmpBytes[i - cpt];

            bitBuf = new BitBuffer(ByteBuffer.wrap(byteBuf));

        }

        // Read the int
        if (nbLeft > 0) {
            if (nbLeft < nbBits)
                return bitBuf.readAsInt(8 - nbLeft, nbBits);
            else
                return bitBuf.readAsInt(nbBits);
        } else {
            long res = bitBuf.readAsInt(nbBits);
            return res;
        }
    }

    public BitBuffer getBitBuf() {
        return bitBuf;
    }

}
