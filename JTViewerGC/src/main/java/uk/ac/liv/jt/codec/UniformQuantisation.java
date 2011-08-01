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

import uk.ac.liv.jt.format.JTUniformQuantizerData;
import uk.ac.liv.jt.types.U32Vector;

public class UniformQuantisation {
	
	/*
	 * Uniform Data Quantization is a lossy encoding algorithm in which a 
	 * continuous set of input values (floating point data) is approximated 
	 * with integral multipliers (i.e. integers) of a common factor.
	 */

	/* dequantize transforms the quantized integer values to an array of float
	 * values. Parameters of the quantization are in quantData. p.259
	 */
    public static float[] dequantize(U32Vector values,
            JTUniformQuantizerData quantData) {
        float[] res = new float[values.length()];

        float minInputRange = quantData.getMin();
        float maxInputRange = quantData.getMax();
        int nbBits = quantData.getNumberOfBits();

        long maxCode = 0xffffffff;

        if (nbBits < 32)
            maxCode = 0x1 << nbBits;

        double encodeMultiplier = (double) maxCode
                / (maxInputRange - minInputRange);

        for (int i = 0; i < values.length(); i++)
            res[i] = (float)((values.get(i) - 0.5) / encodeMultiplier + minInputRange);

        return res;
    }
}
