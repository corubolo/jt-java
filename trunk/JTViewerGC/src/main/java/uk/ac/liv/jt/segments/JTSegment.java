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
import java.util.HashMap;
import java.util.Map;

import uk.ac.liv.jt.format.ByteReader;
import uk.ac.liv.jt.format.JTElement;
import uk.ac.liv.jt.format.JTFile;
import uk.ac.liv.jt.types.GUID;


/**
 * JTSegment is parent to all the segment classes. 
 * The factory method {@link #createJTSegment} instantiates segments of the appropriate type. 
 * The {@link #read} method is overridden to implement the segment specific code. 
 * The header and ZLIB compression is handled transparently in here.
 * 
 * @author fabio
 *
 */
public class JTSegment {

    protected ByteReader reader;

    protected GUID id;
    protected int offset;
    protected int length;
    protected long attributes;
    protected int segType;
    public JTFile file;
    protected Map<GUID, JTElement> elements;

    public long compressionFlag;

    protected int compressedDataLength;

    protected int compressionAlgorithm;

    protected JTSegment() {
    }
    
    /**
     * Factory method to instantiate segments of the appropriate type. 
     * 
     * @param reader a ByteReader for the JT file, allowing random access 
     * @param segmentID the ID of the segment (from TOC)
     * @param segmentOffset the offset from the start of the file (from TOC)
     * @param segmentLength the length of the segment (from TOC)
     * @param segmentAttributes (from TOC)
     * @param f reference to the JTFile class used 
     * @return a new JTSegment of the appropriate type
     * @throws IOException
     */
    public static JTSegment createJTSegment(ByteReader reader, GUID segmentID,
            int segmentOffset, int segmentLength, long segmentAttributes,
            JTFile f) throws IOException {
        JTSegment t;

        int segType = (int) segmentAttributes >> 24;
        switch (segType) {
        case 1:
            t = new LSGSegment();
            break;
        case 2:
            t = new JTBRepSegment();
            break;
        /*
         * 7.2.7 PMI Data Segment The PMI Manager Meta Data Element (as
         * documented in 7.2.6.2 PMI Manager Meta Data Element) can sometimes
         * also be represented in a PMI Data Segment. This can occur when a pre
         * JT 8 version file is migrated to JT 8.1 version file. So from a
         * parsing point of view a PMI Data Segment should be treated exactly
         * the same as a 7.2.6 Meta Data Segment.
         */
        case 3:
        case 4:
            t = new MetadataSegment();
            break;
        case 6:
            t = new LODSegment(-1);
            break;
        // LOD levels 0 to 9
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
            t = new LODSegment(segType - 7);
            break;
        case 17:
            t = new XTSegment();
            break;
        case 18:
            t = new WireFrameSegment();
            break;

        default:
            throw new IOException("Invalid segment Type");
        }

        t.reader = reader;

        t.elements = new HashMap<GUID, JTElement>(1);
        t.file = f;
        t.id = segmentID;
        t.offset = segmentOffset;
        t.length = segmentLength;
        t.attributes = segmentAttributes;

        t.segType = (int) segmentAttributes >> 24;

        return t;
    }

    /**
     * reads the header and contents of the JT Segment. The header and ZLIB compression is handled here
     * Subclasses should override and read the remaining content
     * 
     * @throws IOException
     */
    public void read() throws IOException {
        // Position the cursor to the beginning of the segment
        reader.position(getOffset());

        readHeader();

        // handle ZLIB segment header here
        if (segType < 5 || segType > 16) {
            readHeaderZLIB();
            if (compressionFlag == 2 && compressionAlgorithm == 2)
                reader.setInflating(true, compressedDataLength);
           
        }

    }
    

    void readHeaderZLIB() throws IOException {
        compressionFlag = reader.readU32();
        compressedDataLength = reader.readI32();
        compressionAlgorithm = reader.readU8();
        // Note that data field compressionAlgorithm is included in
        // compressedDataLength count =>
        compressedDataLength -= 1;
    }

  

    private void readHeader() throws IOException {
        // The header in the TOC should be equal to the header read here
        id = reader.readGUID();
        int tocType = segType;
        int tocLength = length;
        segType = reader.readI32();
        length = reader.readI32();
        if (tocType != segType)
            System.err
                    .println("TOC segment type different from segment type... ");
        if (tocLength != length)
            System.err
                    .println("TOC segment lenght different from segment lenght... ");
        //System.out.println(this);
    }

    @Override
    public String toString() {
        String s = new String();
        s += "Segment ID:" + getID() + '\n';
        s += "Segment Offset:" + getOffset() + '\n';
        s += "Segment Length:" + getLength() + '\n';
        s += "Segment Attributes:" + getAttributes() + '\n';
        s += "Segment Type:" + getType() + '\n';

        return s;
    }

    public GUID getID() {
        return id;
    }

    public void setID(GUID segmentID) {
        id = segmentID;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int segmentOffset) {
        offset = segmentOffset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int segmentLength) {
        length = segmentLength;
    }

    public long getAttributes() {
        return attributes;
    }

    public void setAttributes(long segmentAttributes) {
        attributes = segmentAttributes;
    }

    public int getType() {
        return segType;
    }

    public void setType(int segmentType) {
        segType = segmentType;
    }

    public Map<GUID, JTElement> getElements() {
        return elements;
    }

   


}
