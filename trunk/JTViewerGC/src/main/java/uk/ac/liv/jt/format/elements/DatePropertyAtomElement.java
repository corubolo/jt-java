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
import java.util.Date;
import java.util.TimeZone;
/** 
 * Expresses a 'date' property.
 * @author fabio
 *
 */
public class DatePropertyAtomElement extends BasePropertyAtomData {

    public Date date;

    @Override
    public void read() throws IOException {
        super.read();
        short y = reader.readI16();
        short month = reader.readI16();
        short d = reader.readI16();
        short h = reader.readI16();
        short minute = reader.readI16();
        short s = reader.readI16();

        // Specification does not mention time zone or else; so we assume GMT
        // lacking better information
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(y, month, d, h, minute, s);
        date = c.getTime();
        ovalue = date;
        // System.out.println("For ID " + objectID + " Date " + date);

    }

    @Override
    public String toString() {
        return date.toString();
    }

}
