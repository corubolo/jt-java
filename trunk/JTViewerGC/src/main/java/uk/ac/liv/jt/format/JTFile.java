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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import uk.ac.liv.jt.debug.DebugInfo;
import uk.ac.liv.jt.segments.JTSegment;
import uk.ac.liv.jt.segments.LSGSegment;
import uk.ac.liv.jt.types.GUID;

/** 
 * Main class to interpret a JT file. 
 * 
 * Use the standard {@link #read()} method to read the header and Table of Contents.
 * 
 * @author fabio
 *
 */
public class JTFile {
	private ByteReader reader;

    // File Header
    private String version;
    private int fileAttribute;
    private int tocOffset;
    private GUID lsgSegmentID;
    public URI uri;

    private Map<GUID, JTSegment> segments;

    private LSGSegment lsgSegment;

    public JTFile(ByteReader reader) {
        super();

        this.reader = reader;
        segments = new HashMap<GUID, JTSegment>();
    }

    public JTFile(File f, URI uri) throws IOException {
        super();
        this.uri = uri;
        reader = new ByteReader(f);
        segments = new HashMap<GUID, JTSegment>();
    }
    @Override
    public String toString() {
        String s = new String();

        s += "Version:" + getVersion() + '\n';
        s += "Byte Order:" + reader.getByteOrder() + '\n';
        s += "File Attribute:" + getFileAttribute() + '\n';
        s += "TOC Offset:" + getTocOffset() + '\n';
        s += "LSG Segment ID:" + getLsgSegmentID() + '\n';
        s += "Number of segments:" + segments.size() + '\n';

        return s;
    }

    public void print() {
        System.out.println(this);

        System.out.println("");

        Set set = segments.entrySet();
        Iterator i = set.iterator();

        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();

            System.out.println("Segment: " + me.getKey() + " :\n"
                    + me.getValue());

            System.out.println("");
        }
    }

    public void addSegment(JTSegment segment) {
        segments.put(segment.getID(), segment);
    }

    /** reads the JT file header 
     * 
     * @throws IOException
     */
    public void readHeader() throws IOException {
        version = reader.readString(80);
        
        // Quick and dirty
        reader.MAJOR_VERSION = Byte.parseByte(version.substring(8, 9));

        int a = reader.readUChar();
        if (a == 0)
            reader.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        else if (a == 1)
            reader.setByteOrder(ByteOrder.BIG_ENDIAN);
        else
            throw new IOException(
                    "IN JT header: byteorder has non valid value: " + a);
        fileAttribute = reader.readI32();
        tocOffset = reader.readI32();
        lsgSegmentID = reader.readGUID();

        if (DebugInfo.debugMode) {
            System.out.println("Version:" + version);
            System.out.println("Byte Order:" + reader.getByteOrder());
            System.out.println("File Attribute:" + getFileAttribute());
            System.out.println("TOC Offset:" + tocOffset);
            System.out.println("LSG Segment ID:" + lsgSegmentID);
        }
    }

    

    /** reads the JT file Table of contents 
     * 
     * @throws IOException
     */
    public void readTOC() throws IOException {
        
        reader.position(getTocOffset());
        
        int entryCount = reader.readI32();

        // System.out.println("Entry Count:" + entryCount);

        for (int i = 0; i < entryCount; i++) {
            JTSegment segment = readTOCEntry();
            addSegment(segment);
            if (segment.getID().equals(lsgSegmentID))
                lsgSegment = (LSGSegment) segment;

            if (DebugInfo.debugMode) {
                System.out.println("Segment Entry " + i + ":");
                System.out.println("  GUID: " + segment.getID());
                System.out.println("  Offset: " + segment.getOffset());
                System.out.println("  Length: " + segment.getLength());
                System.out.println("  Segment type: " + segment.getType());
                
                
                
//            	if (segment.getType() == 7)
//                  System.out.println(segment.getOffset());
            }
        }
    }

    private JTSegment readTOCEntry() throws IOException {
        GUID segmentID = reader.readGUID();
        int segmentOffset = reader.readI32();
        int segmentLength = reader.readI32();
        long segmentAttributes = reader.readU32();

        return JTSegment.createJTSegment(reader, segmentID, segmentOffset,
                segmentLength, segmentAttributes, this);

    }
    
    
    /** Convenience methods, reads the header and Table of Contents, 
     * and returns the pointer to the LSG segment
     * 
     * @return a pointer to the LSG segment
     * @throws IOException
     */
    public LSGSegment read() throws IOException {
        // read the file header
        readHeader();

        // Read the Table of Contents

        readTOC();
        
        return lsgSegment;

    }
    
    /** 
     * Returns the collection of JT segments.
     * @return
     */
    public Collection<JTSegment> getSegments() {

        return segments.values();

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getFileAttribute() {
        return fileAttribute;
    }

    public void setFileAttribute(int fileAttribute) {
        this.fileAttribute = fileAttribute;
    }

    public int getTocOffset() {
        return tocOffset;
    }

    public GUID getLsgSegmentID() {
        return lsgSegmentID;
    }

    public LSGSegment getLsgSegment() {
        return lsgSegment;
    }

    public JTSegment getSegment(GUID g) {
        return segments.get(g);
    }
}
