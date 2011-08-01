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

import uk.ac.liv.jt.debug.DebugJTReader;

/** Quantization Parameters specifies for each shape data type grouping 
 * (i.e. Vertex, Normal, Texture Coordinates, Color) the number of 
 * quantization bits used for given qualitative compression level. p.57 */
public class JTQuantizationParam {


    private ByteReader reader;

    private int bitsPerVertex;
    private int normalBitsFactor;
    private int bitsPerTextureCoord;
    private int bitsPerColor;
    private int bitsPerNormal;

    public int getBitsPerNormal() {
        return bitsPerNormal;
    }

    public JTQuantizationParam(ByteReader reader) {
        super();
        this.reader = reader;
    }

    public void read() throws IOException {
    	/* Bits Per Vertex specifies the number of quantization bits per vertex 
    	 * coordinate component. Value must be within range [0:24] inclusive. */
        bitsPerVertex = reader.readU8();
    	
    	/* Normal Bits Factor is a parameter used to calculate the number of 
    	 * quantization bits for normal vectors. 
		 * The actual number of quantization bits per normal is computed using 
		 * this factor and the following formula: 
		 * “BitsPerNormal = 6 + 2 * Normal Bits Factor” */
        normalBitsFactor = reader.readU8();
        bitsPerNormal = 6 + 2 * normalBitsFactor;
        
        /* Bits Per Texture Coord specifies the number of quantization bits per 
         * texture coordinate component. */
        bitsPerTextureCoord = reader.readU8();
        
        /* Bits Per Color specifies the number of quantization bits per 
         * color component. */
        bitsPerColor = reader.readU8();

        if (DebugJTReader.debugMode) {
            System.out.println("Bits per vertex:" + getBitsPerVertex());
            System.out.println("Normal bits factor:" + getNormalBitsFactor());
            System.out.println("Bits per normal:" + getBitsPerNormal());
            System.out.println("Bits per texture Coord:"
                    + getBitsPerTextureCoord());
            System.out.println("Bits per color:" + getBitsPerColor());
        }

    }

    public int getBitsPerVertex() {
        return bitsPerVertex;
    }

    public int getNormalBitsFactor() {
        return normalBitsFactor;
    }

    public int getBitsPerTextureCoord() {
        return bitsPerTextureCoord;
    }

    public int getBitsPerColor() {
        return bitsPerColor;
    }

}
