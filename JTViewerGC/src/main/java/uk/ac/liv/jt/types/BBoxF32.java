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
package uk.ac.liv.jt.types;

/**
 * A bounding box defined as the two corner points in xyz coordinates
 * 
 * @author fabio
 *
 */
public class BBoxF32 {

    public CoordF32 minCorner;
    public CoordF32 maxCorner;

    public BBoxF32(CoordF32 minCorner, CoordF32 maxCorner) {
        super();
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
    }

    @Override
    public String toString() {
        return "Min: " + minCorner + " - Max: " + maxCorner;
    }

}
