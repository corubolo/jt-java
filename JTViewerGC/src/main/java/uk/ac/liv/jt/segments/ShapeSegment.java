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

import uk.ac.liv.jt.format.JTElement;
import uk.ac.liv.jt.format.elements.PartitionNodeElement;
import uk.ac.liv.jt.format.elements.TriStripSetShapeLODElement;
import uk.ac.liv.jt.types.GUID;

/**
 * Shape segment type. TODO: implement
 * 
 * @author fabio
 *
 */
public class ShapeSegment extends JTSegment {
    
    
    //public TriStripSetShapeLODElement lod;
    @Override
    public void read() throws java.io.IOException {
        super.read();
//        JTElement element2 = JTElement.createJTElement(reader);
//        element2.read(); 
//        if (element2 instanceof TriStripSetShapeLODElement) {
//          //  lod = (TriStripSetShapeLODElement) element2;
//            
//        }
//        else 
            
            System.err.println("Unsupported element: " + this.getClass());

    }

}
