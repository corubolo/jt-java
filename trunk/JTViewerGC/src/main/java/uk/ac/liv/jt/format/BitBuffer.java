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

import java.nio.ByteBuffer;

public class BitBuffer {

	ByteBuffer buffer;
	int bitBuffer; // Temporary i/o buffer
    int nBits; // Number of bits in bitBuffer

	int bitPos;
	
	

	public BitBuffer(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
		this.bitPos = 0;
		this.bitBuffer = 0x0000;
        this.nBits = 0;
	}

	
	public int getBitPos() {
		return bitPos;
	}
	
	
	public long getBitBufBitSize() {
		return this.buffer.limit() * 8;
		
	}
	
	public long readAsLong(int nbBits) {
		return readAsLong(0, nbBits);
	}
	
	
	/* Read specified number of bits (max 32) starting from the given bit 
	 * position, return the value as long.
	 */
	public long readAsLong(long bPos, int nbBits) {
		long value = 0;
		long len = bPos + nbBits;
			
		// len = number of bits to read, we skip bPos bits and create a long
		// with nbBits bits
		while (len > 0) {
			// Not enough bits in the buffer => We read another byte
			if (this.nBits == 0) {
				bitBuffer = (int)buffer.get();
				this.nBits = 8;
				bitBuffer &= 0xFFL;
			}
			
			// This test skips the first bPos bits
			if (bPos == 0) {				
				value <<= 1;	
				// The value of the msb is added to the value result
				value |= (int) (bitBuffer >> 7);
			} else {
				bPos--;
			}
			// Remove the msb so the 2nd bit becomes the msb
			bitBuffer <<= 1;
			bitBuffer &= 0xFFL;
			this.nBits--;
			len--;
			this.bitPos++;
	   }
		
		return value;
	}
	
	

	public int readAsInt(int nbBits) {
		return (int)readAsLong(nbBits);
	}

	public int readAsInt(long bitPos, int nbBits) {
		return (int)readAsLong(bitPos, nbBits);
	}

	

	public byte readAsByte(int nbBits) {
		return (byte)readAsLong(nbBits);
	}
	
	
}
