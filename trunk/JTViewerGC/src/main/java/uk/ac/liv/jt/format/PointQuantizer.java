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
package uk.ac.liv.jt.format;

import java.io.IOException;

/** A Point Quantizer Data collection is made up of three Uniform Quantizer
 * Data collections; there is a separate Uniform Quantizer Data collection 
 * for the X, Y, and Z values of point coordinates. p. 244 */

public class PointQuantizer {


    ByteReader reader;

    JTUniformQuantizerData xQuantizerData;
    JTUniformQuantizerData yQuantizerData;
    JTUniformQuantizerData zQuantizerData;

    public PointQuantizer(ByteReader reader) {
        super();
        this.reader = reader;

        xQuantizerData = new JTUniformQuantizerData(this.reader);
        yQuantizerData = new JTUniformQuantizerData(this.reader);
        zQuantizerData = new JTUniformQuantizerData(this.reader);

    }

    public void read() throws IOException {
        xQuantizerData.read();
        yQuantizerData.read();
        zQuantizerData.read();
    }

    public JTUniformQuantizerData getXQuantizerData() {
        return xQuantizerData;
    }

    public JTUniformQuantizerData getYQuantizerData() {
        return yQuantizerData;
    }

    public JTUniformQuantizerData getZQuantizerData() {
        return zQuantizerData;
    }

}
