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

import java.util.StringTokenizer;

/**
 * GUID are 16 bytes numbers used as unique identifiers in the JT system.
 * @author fabio
 *
 */

public class GUID {

    /**
     * Special GUID indicating the end of elements in a section of the Logical Scene graph
     */
    public static final GUID END_OF_ELEMENTS = fromString("{FFFFFFFF-FFFF-FFFF-FF-FF-FF-FF-FF-FF-FF-FF}");

    public static final int LENGTH = 16;
    public long w1;
    public int w2;
    public int w3;
    public int w4;
    public int w5;
    public int w6;
    public int w7;
    public int w8;
    public int w9;
    public int w10;
    public int w11;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String)
            try {
                obj = fromString((String) obj);
            } catch (Exception e) {
                return false;
            }
        if (!(obj instanceof GUID))
            return false;
        GUID g = (GUID) obj;
        if (g.w1 == w1 && g.w2 == w2 && g.w3 == w3 && g.w4 == w4 && g.w5 == w5
                && g.w6 == w6 && g.w7 == w7 && g.w8 == w8 && g.w9 == w9
                && g.w10 == w10 && g.w11 == w11)
            return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{" + Long.toHexString(w1).toUpperCase() + "-"
                + Integer.toHexString(w2).toUpperCase() + "-"
                + Integer.toHexString(w3).toUpperCase() + "-"
                + Integer.toHexString(w4).toUpperCase() + "-"
                + Integer.toHexString(w5).toUpperCase() + "-"
                + Integer.toHexString(w6).toUpperCase() + "-"
                + Integer.toHexString(w7).toUpperCase() + "-"
                + Integer.toHexString(w8).toUpperCase() + "-"
                + Integer.toHexString(w9).toUpperCase() + "-"
                + Integer.toHexString(w10).toUpperCase() + "-"
                + Integer.toHexString(w11).toUpperCase() + "}");

        return sb.toString();
    }

    /** Converts a string representation of a GUID to a GUID.
     *  The String is expressed in the format {FFFFFFFF-FFFF-FFFF-FF-FF-FF-FF-FF-FF-FF-FF} with hexadecimal values.
     * @param s the String
     * @return the corresponding GUID
     */
    public static GUID fromString(String s) {
        GUID g = new GUID();
        if (s.charAt(0) != '{')
            throw new IllegalArgumentException();
        StringTokenizer st = new StringTokenizer(
                s.substring(1, s.length() - 1), "-");

        g.w1 = Long.parseLong(st.nextToken(), 16);
        g.w2 = Integer.parseInt(st.nextToken(), 16);
        g.w3 = Integer.parseInt(st.nextToken(), 16);
        g.w4 = Integer.parseInt(st.nextToken(), 16);
        g.w5 = Integer.parseInt(st.nextToken(), 16);
        g.w6 = Integer.parseInt(st.nextToken(), 16);
        g.w7 = Integer.parseInt(st.nextToken(), 16);
        g.w8 = Integer.parseInt(st.nextToken(), 16);
        g.w9 = Integer.parseInt(st.nextToken(), 16);
        g.w10 = Integer.parseInt(st.nextToken(), 16);
        g.w11 = Integer.parseInt(st.nextToken(), 16);
        return g;
    }

    @Override
    public int hashCode() {
        return (int) (w1 + w2 + w3 + w4 + w5 + w6 + w7 + w8 + w9 + w10);

    }

 

}
