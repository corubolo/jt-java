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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import uk.ac.liv.jt.format.JTElement;

public class PropertyProxyMetaDataElement extends JTElement {

    public Map<String, BasePropertyAtomData> properties;

    @Override
    public void read() throws IOException {
        String key;
        properties = new HashMap<String, BasePropertyAtomData>();
        while ((key = reader.readMbString()) != null) {
            int type = reader.readU8();
            BasePropertyAtomData d;
            switch (type) {
            case 0:
                d = null;
                break;
            case 1:
                d = new StringPropertyAtomElement();
                ((StringPropertyAtomElement) d).value = reader.readMbString();
                break;
            case 2:
                d = new IntegerPropertyAtomElement();
                ((IntegerPropertyAtomElement) d).value = reader.readI32();
                break;
            case 3:
                d = new FloatingPointPropertyAtomElement();
                ((FloatingPointPropertyAtomElement) d).value = reader.readF32();
                break;
            case 4:
                d = new DatePropertyAtomElement();
                short y = reader.readI16();
                short month = reader.readI16();
                short da = reader.readI16();
                short h = reader.readI16();
                short minute = reader.readI16();
                short s = reader.readI16();

                // Specification does not mention time zone or else; so we
                // assume GMT
                // lacking better information
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                c.set(y, month, da, h, minute, s);
                ((DatePropertyAtomElement) d).date = c.getTime();
                break;
            default:
                d = null;
                System.out
                        .println("Error : invalid data type in PropertyProxyMetaDataElement");
                break;
            }
            properties.put(key, d);
            //System.out.println(key + " = " + d);

        }

    }

}
