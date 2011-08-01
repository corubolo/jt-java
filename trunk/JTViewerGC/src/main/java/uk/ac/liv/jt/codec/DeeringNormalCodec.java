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

import uk.ac.liv.jt.types.Vec3D;

public class DeeringNormalCodec {
	
	/* The deering codec, transform the sextant, octant, theta, and psi values
	 * to a 3D Vector (the normal)
	 */

    long numBits;

    
    static DeeringNormalLookupTable lookupTable;
    public DeeringNormalCodec() {
        super();
        if (lookupTable == null)
            lookupTable = new DeeringNormalLookupTable();
        numBits = 6;
    }

    public DeeringNormalCodec(long numberbits) {
        super();
        if (lookupTable == null)
            lookupTable = new DeeringNormalLookupTable();
        numBits = numberbits;
    }

    public Vec3D convertCodeToVec(long sextant, long octant, long theta,
            long psi) {

        // Size of code = 6+2*numBits, and max code size is 32 bits,
        // so numBits must be <= 13.
        // Code layout: [sextant:3][octant:3][theta:numBits][psi:numBits]

        double psiMax = 0.615479709;
        long bitRange = 1 << numBits; // 2^numBits
        double fBitRange = bitRange;

        // For sextants 1, 3, and 5, theta needs to be incremented
        theta += (sextant & 1);

       
        DeeringLookupEntry lookupEntry = lookupTable.lookupThetaPsi(theta, psi,
                numBits);

        double x, y, z;
        double xx = x = lookupEntry.getCosTheta() * lookupEntry.getCosPsi();
        double yy = y = lookupEntry.getSinPsi();
        double zz = z = lookupEntry.getSinTheta() * lookupEntry.getCosPsi();

        // Change coordinates based on the sextant
        switch ((int) sextant) {
        case 0: // No op
            break;
        case 1: // Mirror about x=z plane
            z = xx;
            x = zz;
            break;
        case 2: // Rotate CW
            z = xx;
            x = yy;
            y = zz;
            break;
        case 3: // Mirror about x=y plane
            y = xx;
            x = yy;
            break;
        case 4: // Rotate CCW
            y = xx;
            z = yy;
            x = zz;
            break;
        case 5: // Mirror about y=z plane
            z = yy;
            y = zz;
            break;
        }
        ;

        // Change some more based on the octant
        // if first bit is 0, negate x component
        if ((octant & 0x4) == 0)
            x = -x;

        // if second bit is 0, negate y component
        if ((octant & 0x2) == 0)
            y = -y;

        // if third bit is 0, negate z component
        if ((octant & 0x1) == 0)
            z = -z;

        return new Vec3D(x, y, z);
    }

    public DeeringCode unpackCode(long code) {
        long mask = ((1 << numBits) - 1) & 0xFFFFFFFFL;

        return new DeeringCode((code >> (numBits + numBits + 3)) & 0x7,
                (code >> (numBits + numBits)) & 0x7,
                (code >> (numBits)) & mask, (code) & mask);
    }

}
