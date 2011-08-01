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

import uk.ac.liv.jt.codec.DeeringNormalCodec;
import uk.ac.liv.jt.codec.Int32Compression;
import uk.ac.liv.jt.codec.Predictors.PredictorType;
import uk.ac.liv.jt.debug.DebugJTReader;
import uk.ac.liv.jt.types.U32Vector;
import uk.ac.liv.jt.types.Vec3D;

/** The Quantized Vertex Normal Array data collection contains the 
 * quantization data/representation for a set of vertex normals. Quantized 
 * Vertex Normal Array data collection is only present if previously read
 * Normal Binding value is not equal to zero. p.239 */
public class QuantizedVertexNormalArray {


    ByteReader reader;
    
    /* Normals for the vertices */
    Vec3D[] normals;
    

	public QuantizedVertexNormalArray(ByteReader reader) {
        super();
        this.reader = reader;

    }
    
    public void read() throws IOException {
    	/* Number of Bits specifies the quantized size (i.e. the number of bits
    	 *  of precision) for the Theta and Psi	angles*/
        int nbBits = reader.readU8();

        /* Normal Count specifies the count (number of unique) Normal Codes. */
        int normalCount = reader.readI32();


        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("** Sextant Codes: **");
        }
        /* Sextant Codes is a vector of “codes” (one per normal) for a set of 
         * normals identifying which Sextant of the corresponding sphere Octant 
         * each normal is located in. */
        U32Vector sextantCodes = Int32Compression.read_VecU32_Int32CDP(reader,
                PredictorType.Lag1);

        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("Float values (" + sextantCodes.length() + ") ");
        }


        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("** Octant Codes: **");
        }
        /* Octant Codes is a vector of “codes” (one per normal) for a set of
         * normals identifying which sphere Octant each normal is located in. */
        U32Vector octantCodes = Int32Compression.read_VecU32_Int32CDP(reader,
                PredictorType.Lag1);

        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("Float values (" + octantCodes.length() + ") ");
        }


        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("** Theta Codes: **");
        }
        /* Theta Codes is a vector of “codes” (one per normal) for a set of 
         * normals representing in Sextant coordinates the quantized theta 
         * angle for each normal’s location on the unit radius sphere; where 
         * theta angle is defined as the angle in spherical coordinates about 
         * the Y-axis on a unit radius sphere. */
        U32Vector thetaCodes = Int32Compression.read_VecU32_Int32CDP(reader,
                PredictorType.Lag1);

        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("Float values (" + thetaCodes.length() + ") ");
        }


        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("** Psi Codes: **");
        }
        /* Psi Codes is a vector of “codes” (one per normal) for a set of 
         * normals representing in Sextant coordinates the quantized Psi angle 
         * for each normal’s location on the unit radius sphere; where Psi 
         * angle is defined as the longitudinal angle in spherical coordinates 
         * from the y = 0 plane on the unit radius sphere. */
        U32Vector psiCodes = Int32Compression.read_VecU32_Int32CDP(reader,
                PredictorType.Lag1);

        if (DebugJTReader.debugMode) {
            System.out.println();
            System.out.println("Float values (" + psiCodes.length() + ") ");
        }

        normals = new Vec3D[psiCodes.length()];
        DeeringNormalCodec deeringCodec = new DeeringNormalCodec(nbBits);

        for (int i = 0; i < psiCodes.length(); i++)
            normals[i] = deeringCodec.convertCodeToVec(sextantCodes.get(i),
                    octantCodes.get(i), thetaCodes.get(i), psiCodes.get(i));
    }
    
    
    public Vec3D[] getNormals() {
		return normals;
	}

	

}
