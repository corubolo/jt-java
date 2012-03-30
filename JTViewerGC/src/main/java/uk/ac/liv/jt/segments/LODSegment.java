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
package uk.ac.liv.jt.segments;

import java.io.IOException;

import uk.ac.liv.jt.debug.DebugInfo;
import uk.ac.liv.jt.format.JTElement;
/** 
 * Shape LOD Segment contains an Element that defines the geometric shape 
 * definition data (e.g. vertices, polygons, normals, etc) for a particular 
 * shape Level Of Detail or alternative representation. (p. 117)
 */

public class LODSegment extends JTSegment {
	
    int level;
    public JTElement e;

    protected LODSegment(int level) {
        this.level = level;
    }

    @Override
    public void read() throws IOException {
        super.read();

        if (DebugInfo.debugMode) {
            System.out.println("- Header");
            System.out.println("  Id:" + id);
            System.out.println("  Type:" + segType);
            System.out.println("  Length:" + length);
            System.out.println();

            System.out.println("**** Shape LOD Element ****");
            System.out.println();
        }

        JTElement element = JTElement.createJTElement(reader);

        if (DebugInfo.debugMode) {
            System.out.println("Element length: " + element.getLength());
            System.out.println("Object Type Id: " + element.getId());
            System.out.println("Object Base Type: "
                    + element.getObjectBaseType());
        }

        /* Shape LOD Element p. 118 */        
        element.read();
        e = element;

    }

}
