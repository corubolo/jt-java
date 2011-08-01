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
/**
 * 
 * Meta data segment. Used to represent segment types 
 * PMI meta data segment and Meta data segment, as descrbed:
 * 
 * 7.2.7 PMI Data Segment The PMI Manager Meta Data Element (as
 * documented in 7.2.6.2 PMI Manager Meta Data Element) can sometimes
 * also be represented in a PMI Data Segment. This can occur when a pre
 * JT 8 version file is migrated to JT 8.1 version file. So from a
 * parsing point of view a PMI Data Segment should be treated exactly
 * the same as a 7.2.6 Meta Data Segment.

 * @author fabio
 *
 */
public class MetadataSegment extends JTSegment {

    @Override
    public void read() throws java.io.IOException {
        super.read();
        // System.out.println("=> Metadata Segment");
        //
        // System.out.println(" - Header");
        // System.out.println("    Id:" + this.id);
        // System.out.println("    Type:" + this.segType);
        // System.out.println("    Length:" + this.length);

        JTElement element = JTElement.createJTElement(reader);

        // System.out.println(" - Metadata Elements");
        //
        // System.out.println("     Element length: " + element.getLength());
        // System.out.println("     Object Type Id: " + element.getId());
        // System.out.println("     Object Base Type: "
        // + element.getObjectBaseType());

        element.read();

    }

}
