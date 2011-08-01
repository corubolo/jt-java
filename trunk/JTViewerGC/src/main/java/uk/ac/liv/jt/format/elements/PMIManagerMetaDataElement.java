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
package uk.ac.liv.jt.format.elements;

import java.io.IOException;

import uk.ac.liv.jt.format.JTElement;

public class PMIManagerMetaDataElement extends JTElement {

    long nodeFlags;
    int[] attObjectId;
    int versionNumber;
    int reservedField;
    int associationCount;
    int stringCount;
    int userAttributeCount;
     int modelViewCount;
     int genericEntityCount;
     int cadTagFlag;
     int cadTagIndexCount;
     ModelView[] views;
    private String[] stringTable;
    private GenericEntry[] genericEntries;
    
//     @Override
//     public void read() throws IOException {
//    
//     versionNumber = getReader().readI16();
//     reservedField = getReader().readI16();
//     readPMIEntities();
//     associationCount = getReader().readI32();
//     userAttributeCount = getReader().readI32();
//     stringCount = getReader().readI32();
//     if (stringCount > 0)
//         stringTable = new String[stringCount];
//     for (int i=0;i<stringCount;i++)
//         stringTable[i] = reader.readString8();
//     if (versionNumber > 5) {
//         modelViewCount = getReader().readI32();
//         views= readModelViews(modelViewCount);
//         
//         genericEntityCount = getReader().readI32();
//         genericEntries = readGenericEntries();
//         
//     }
//     if (versionNumber > 7) {
//         cadTagFlag = getReader().readI32();
//         if (cadTagFlag == 1) {
//             cadTagIndexCount = getReader().readI32();
//         }
//     }
//     }

    private GenericEntry[] readGenericEntries() {
        // TODO Auto-generated method stub
        return null;
    }

    private ModelView[] readModelViews(int modelViewCount2) throws IOException {
        ModelView[] ret= new ModelView[modelViewCount2];
        for (int i = 0; i < modelViewCount2;i++){
            ModelView c = ret[i] =new ModelView();
            c.eye_direction = reader.readCoordF32();
            c.angle = reader.readF32();
            c.eye_pos = reader.readCoordF32();
            c.target_point = reader.readCoordF32();
            c.view_angle = reader.readCoordF32();
            c.viewport_diameter = reader.readF32();
            c.reserved1 = reader.readF32();
            c.reserved2 = reader.readI32();
            c.active_flag = reader.readI32();
            
            c.view_id = reader.readI32();
            c.view_name_string_id = reader.readI32();
            
        }
        return ret;
    }

    public void readPMIEntities() throws IOException {
        // PMI Dimension Entities
        System.out.println("     PMI Dimension Entities");

        int dimensionCount = getReader().readI32();
        System.out.println("       Dimension count: " + dimensionCount);

        // PMI Note Entities
        System.out.println("     PMI Note Entities");

        int noteCount = getReader().readI32();
        System.out.println("       Note count: " + noteCount);

        // PMI Datum Feature Symbol Entities
        System.out.println("     PMI Datum Feature Symbol Entities");

        int dfsCount = getReader().readI32();
        System.out.println("       DFS count: " + dfsCount);

        // PMI Datum Target Entities
        System.out.println("     PMI Datum Target Entities");

        int datumTargetCount = getReader().readI32();
        System.out.println("       Datum Target count: " + datumTargetCount);

        // PMI Feature Control Frame Entities
        System.out.println("     PMI Feature Control Frame Entities");

        int fcfCount = getReader().readI32();
        System.out.println("       FCF count: " + fcfCount);

        // PMI Line Weld Entities
        System.out.println("     PMI Line Weld Entities");

        int lineWeldCount = getReader().readI32();
        System.out.println("       Line Weld count: " + lineWeldCount);

        // PMI Spot Weld Entities
        System.out.println("     PMI Spot Weld Entities");

        int spotWeldCount = getReader().readI32();
        System.out.println("       Spot Weld count: " + spotWeldCount);

        // PMI Surface Finish Entities
        System.out.println("     PMI Surface Finish Entities");

        int sfCount = getReader().readI32();
        System.out.println("       SF count: " + sfCount);

        // PMI Measurement Point Entities
        System.out.println("     PMI Measurement Point Entities");

        int mpCount = getReader().readI32();
        System.out.println("       MP count: " + mpCount);

        // PMI Locator Entities
        System.out.println("     PMI Locator Entities");

        int locatorCount = getReader().readI32();
        System.out.println("       Locator count: " + locatorCount);

        // PMI Reference Geometry Entities
        System.out.println("     PMI Reference Geometry Entities");

        int referenceGeometryCount = getReader().readI32();
        System.out.println("       Reference Geometry count: "
                + referenceGeometryCount);

        // PMI Design Group Entities
        System.out.println("     PMI Design Group Entities");

        int designGroupCount = getReader().readI32();
        System.out.println("       Design Group Count: " + designGroupCount);

        // PMI Coordinate System Entities
        System.out.println("     PMI Coordinate System Entities");

        int coordSysCount = getReader().readI32();
        System.out.println("       Coord Sys count: " + coordSysCount);

    }

}
