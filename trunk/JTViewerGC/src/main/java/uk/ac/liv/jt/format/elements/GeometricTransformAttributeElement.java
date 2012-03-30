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
import java.io.PrintWriter;
import java.io.StringWriter;
import de.jreality.math.Matrix;
import de.jreality.scene.Transformation;
/** 
 * This class represent Geometric Transformations according to the specified matrix, where 
 * only the values different from the identity matrix are taken into consideration.
 * The matrix has to be transposed for use in JReality.
 * 
 * @author fabio
 *
 */
public class GeometricTransformAttributeElement extends BaseAttributeElement {

    double[] transform;
    private Transformation tra;

    @Override
    public void read() throws IOException {

        super.read();

        int storeValueMask = reader.readU16();

        int c = storeValueMask;
        transform = new double[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                1 };
                int readValues = 0;
        for (int i = 0; i < 16; i++) {
            if ((c & 0x8000) != 0){
                        transform[i] = reader.readF32();
                                readValues++;
                }
            c = c << 1;
        }

        if(reader.MAJOR_VERSION >= 9){
            reader.readBytes(readValues * 4);
        }
        //System.out.println(this);
    }

    @Override
    public String toString() {
        StringWriter s = new StringWriter();
        PrintWriter pw = new PrintWriter(s);
        for (int i = 0; i < transform.length; i++) {
            pw.printf("%+4f ", transform[i]);
            if ((i + 1) % 4 == 0)
                pw.println();
        }
        return "Transform matrix\n" + s.toString();
    }
    /** 
     * 
     * @return the JReality Transformation object 
     */
    public Transformation getTransformation() {
        if(tra == null) {
            Matrix m = new Matrix(transform);
            m.transpose();
            tra = new Transformation(m.getArray());
        }
        return tra;
    }
    
    
    public double[] getTransformationMatrix() {
        return transform;
    }

}
