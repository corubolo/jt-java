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

/** The Uniform Quantizer Data collection contains information that 
 * defines a scalar quantizer/dequantizer (encoder/decoder) whose range 
 * is divided into levels of equal spacing. */
public class JTUniformQuantizerData {


    private ByteReader reader;

    /* Min specifies the minimum of the quantized range. */
    private float min;
    /* Max specifies the maximum of the quantized range. */
    private float max;
    /* Number of Bits specifies the quantized size (i.e. the number of bits 
     * of precision). */
    private int numberOfBits;

    public JTUniformQuantizerData(ByteReader reader) {
        super();
        this.reader = reader;
    }

    public void read() throws IOException {
        min = reader.readF32();
        max = reader.readF32();
        numberOfBits = reader.readU8();
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public int getNumberOfBits() {
        return numberOfBits;
    }

}
