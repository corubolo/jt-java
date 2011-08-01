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

public class DeeringNormalLookupTable {
	
	/* A table of sin/cos precomputed to optimize the calculation of normals
	 */

    long nBits;
    double[] cosTheta;
    double[] sinTheta;
    double[] cosPsi;
    double[] sinPsi;

    public DeeringNormalLookupTable() {
        super();

        int numberbits = 8;
        nBits = Math.min(numberbits, 31);
        int tableSize = (1 << nBits);
        cosTheta = new double[tableSize + 1];
        sinTheta = new double[tableSize + 1];
        cosPsi = new double[tableSize + 1];
        sinPsi = new double[tableSize + 1];

        double psiMax = 0.615479709;

        double fTableSize = tableSize;

        for (int ii = 0; ii <= tableSize; ii++) {
            double theta = Math.asin(Math.tan(psiMax * (tableSize - ii)
                    / fTableSize));
            double psi = psiMax * ((ii) / fTableSize);
            cosTheta[ii] = Math.cos(theta);
            sinTheta[ii] = Math.sin(theta);
            cosPsi[ii] = Math.cos(psi);
            sinPsi[ii] = Math.sin(psi);
        }
    }

    public long numBitsPerAngle() {
        return nBits;
    }

    public DeeringLookupEntry lookupThetaPsi(long theta, long psi,
            long numberBits) {
        long offset = nBits - numberBits;

        long offTheta = (theta << offset) & 0xFFFFFFFFL;
        long offPsi = (psi << offset) & 0xFFFFFFFFL;

        return new DeeringLookupEntry(cosTheta[(int) offTheta],
                sinTheta[(int) offTheta], cosPsi[(int) offPsi],
                sinPsi[(int) offPsi]);
    }
}
