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

public class Predictors {
    
	/* The primal input data is first run through a Predictor Type algorithm, 
	 * which produces an output array of residual values (i.e. difference from
	 * the predicted value), and this resulting output array of residual values 
	 * is the data which is actually the predicted value), and this resulting 
	 * output array of residual values is the data which is actually
	 * encoded/compressed.
	 */ 
	

    // ---------- Predictor Type Residual Unpacking ----------
    public enum PredictorType {
        Lag1, Lag2, Stride1, Stride2, StripIndex, Ramp, Xor1, Xor2, NULL
    }

    // Apply the predictor algorithm on the residual values to obtain the
    // primal values
    public static int[] unpackResiduals(int[] residuals, PredictorType predType) {

        int predicted;
        int len = residuals.length;

        int[] primals = new int[len];

        for (int i = 0; i < len; i++)
            if (i < 4)
                // The first four values are just primers
                primals[i] = residuals[i];
            else {
                // Get a predicted value
                predicted = predictValue(primals, i, predType);

                if (predType == PredictorType.Xor1
                        || predType == PredictorType.Xor2)
                    // Decode the residual as the current value XOR predicted
                    primals[i] = residuals[i] ^ predicted;
                else
                    // Decode the residual as the current value plus predicted
                    primals[i] = residuals[i] + predicted;
            }
        return primals;
    }

    public static void unpackResidualsOverwrite(int[] residuals, PredictorType predType) {

        int predicted;
        int len = residuals.length;

       // int[] primals = new int[len];

        for (int i = 0; i < len; i++)
            if (i < 4)
                // The first four values are just primers
                residuals[i] = residuals[i];
            else {
                // Get a predicted value
                predicted = predictValue(residuals, i, predType);

                if (predType == PredictorType.Xor1
                        || predType == PredictorType.Xor2)
                    // Decode the residual as the current value XOR predicted
                    residuals[i] = residuals[i] ^ predicted;
                else
                    // Decode the residual as the current value plus predicted
                    residuals[i] = residuals[i] + predicted;
            }
        return;
    }
    // Predict a value given the 4 previous values. Several algorithms may be
    // used depending on the data considered.
    public static int predictValue(int[] primals, int index,
            PredictorType predType) {
        int predicted;
        int v1 = primals[index - 1];
        int v2 = primals[index - 2];
        int v3 = primals[index - 3];
        int v4 = primals[index - 4];

        switch (predType) {
        default:
        case Lag1:
        case Xor1:
            predicted = v1;
            break;
        case Lag2:
        case Xor2:
            predicted = v2;
            break;
        case Stride1:
            predicted = v1 + (v1 - v2);
            break;
        case Stride2:
            predicted = v2 + (v2 - v4);
            break;
        case StripIndex:

            if (v2 - v4 < 8 && v2 - v4 > -8)
                predicted = v2 + (v2 - v4);
            else
                predicted = v2 + 2;
            break;
            
        case Ramp:
            predicted = index;
            break;
        }
        return predicted;
    }

}
