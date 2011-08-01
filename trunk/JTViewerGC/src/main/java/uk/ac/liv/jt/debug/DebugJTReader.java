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
package uk.ac.liv.jt.debug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.liv.jt.format.ByteReader;
import uk.ac.liv.jt.format.JTFile;
import uk.ac.liv.jt.segments.JTSegment;
import de.jreality.reader.AbstractReader;
import de.jreality.scene.SceneGraphComponent;
import de.jreality.util.Input;

public class DebugJTReader extends AbstractReader {

    private JTFile jtFile;

    private SceneGraphComponent mRootGroupNode;

    private ByteReader reader;

    public static boolean debugMode = false;
    // A more verbose debugging mode for decoding algorithms
    public static boolean debugCodec = false;

    public JTFile getJtFile() {
        return jtFile;
    }

    public SceneGraphComponent getMRootGroupNode() {
        return mRootGroupNode;
    }

    public SceneGraphComponent getRootGroupNode() {
        return mRootGroupNode;
    }

    /**
     * Copies content of an inputStream to temp a file
     * 
     * @param is
     * @return the file just created
     * @throws IOException
     */
    public static File copyToTemp(InputStream is) throws IOException {
        File f = File.createTempFile("jtfile", null);
        FileOutputStream os = new FileOutputStream(f);

        byte[] buf = new byte[16 * 1024];
        int i;
        while ((i = is.read(buf)) != -1)
            os.write(buf, 0, i);
        is.close();
        os.close();
        return f;
    }

    private boolean load(InputStream inputStream) {

        /*
         * as JT files can be big, we first need to create a local disk cache
         * copy
         */
        try {
            File jtf = copyToTemp(inputStream);
            reader = new ByteReader(jtf);
            setJtFile(new JTFile(reader));

            if (debugMode) {
                System.out.println("\n=================");
                System.out.println("== File Header ==");
                System.out.println("=================\n");
            }

            // Read File header
            jtFile.readHeader();

            if (debugMode) {
                System.out.println("\n=================");
                System.out.println("== TOC Segment ==");
                System.out.println("=================\n");
            }

            // Read the TOC -> The TOC can be at the end
            reader.position(jtFile.getTocOffset());
            jtFile.readTOC();

            // Read the data segments
            Collection<JTSegment> segments = jtFile.getSegments();
            for (JTSegment segment : segments)

                // Bnc (OK)
//            	if (segment.getOffset() == 5801) {
//        		if (segment.getOffset() == 16773) {
//        		if (segment.getOffset() == 18876) {
//        		if (segment.getOffset() == 21905) {
//        		if (segment.getOffset() == 25526) {
//        		if (segment.getOffset() == 28454) {
//        		if (segment.getOffset() == 29020) {
//        		if (segment.getOffset() == 30821) {
//        		if (segment.getOffset() == 32634) {
//        		if (segment.getOffset() == 34835) {
//        		if (segment.getOffset() == 39691) {
            	
                // ButterflyValve (OK)
//            	if (segment.getOffset() == 5743) {
//            	if (segment.getOffset() == 12274) {
//            	if (segment.getOffset() == 20236) {
//            	if (segment.getOffset() == 25476) {
//            	if (segment.getOffset() == 27788) {
//            	if (segment.getOffset() == 30921) {
//            	if (segment.getOffset() == 31718) {
//            	if (segment.getOffset() == 65915) {
//            	if (segment.getOffset() == 70946) {
                	
                // satphone.jt (OK)
//              if (segment.getOffset() == 1752) {
//              if (segment.getOffset() == 66188) {
//              if (segment.getOffset() == 68620) {
                	
                // Conrod
//              if (segment.getOffset() == 7905) {
//            	if (segment.getOffset() == 11102) {
//            	if (segment.getOffset() == 26004) {
//            	if (segment.getOffset() == 26907) {
//            	if (segment.getOffset() == 34022) {
//            	if (segment.getOffset() == 40003) {
//            	if (segment.getOffset() == 42886) {
//            	if (segment.getOffset() == 45764) {
            	if (segment.getOffset() == 48810) {
                	 
                // Wingflap            		
//              if (segment.getOffset() == 7660) {
//        		if (segment.getOffset() == 11306) {
//        		if (segment.getOffset() == 13414) {
//        		if (segment.getOffset() == 16938) {
//        		if (segment.getOffset() == 18572) {
//        		if (segment.getOffset() == 22894) {
//        		if (segment.getOffset() == 25013) {
//        		if (segment.getOffset() == 30119) {
//        		if (segment.getOffset() == 32342) {
//        		if (segment.getOffset() == 35005) {
//        		if (segment.getOffset() == 37768) {
//        		if (segment.getOffset() == 40686) {
//        		if (segment.getOffset() == 41743) {
//        		if (segment.getOffset() == 44399) {
//        		if (segment.getOffset() == 46001) {
//        		if (segment.getOffset() == 48689) {
//        		if (segment.getOffset() == 49858) {
//        		if (segment.getOffset() == 54613) {
//        		if (segment.getOffset() == 56445) {
//        		if (segment.getOffset() == 67070) {
//        		if (segment.getOffset() == 67361) {
//            	if (segment.getOffset() == 68858) {
//              if (segment.getOffset() == 73144) {
            		
            	// Cam  (Huffman problem)
//              if (segment.getOffset() == 9037) {		// .50-13_x_4.5_SHCS (PB)
//              if (segment.getOffset() == 11456) {		// tee-nut
//              if (segment.getOffset() == 13939) {		// vise-pin
//            	if (segment.getOffset() == 17011) {		// vise-sphere
//        		if (segment.getOffset() == 23918) {		// table (PB)
//        		if (segment.getOffset() == 33740) {		// .50-13_x_1.5_SHCS
//        		if (segment.getOffset() == 36310) {		// vise-jaw
//        		if (segment.getOffset() == 40246) {		// vise-base (PB)
//        		if (segment.getOffset() == 52421) {		// atp-cavity
//        		if (segment.getOffset() == 56273) {		// atp-core
//        		if (segment.getOffset() == 61556) {		// atp
//        		if (segment.getOffset() == 72456) {		// cutter1
//        		if (segment.getOffset() == 77817) {		// cutter2
//        		if (segment.getOffset() == 83153) {		// cavity-in-vice

            	
                    // if (segment.getType() == 1) {

                    if (debugMode) {
                        System.out.println("\n===============");
                        System.out.println("== Segment " + segment.getType()
                                + " ==");
                        System.out.println("===============\n");
                    }

                    segment.read();

                }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger("liv.JTViewer")
                    .log(Level.WARNING, "JTReader.load");
            Logger.getLogger("liv.JTViewer")
                    .log(Level.WARNING, "\tIOException");
            return false;
        }

        return true;
    }

    @Override
    public void setInput(Input input) throws IOException {
        super.setInput(input);

        root = new SceneGraphComponent("jt");
        setRootGroupNode(root);
        load(input.getInputStream());
    }

    public void setJtFile(JTFile jtFile) {
        this.jtFile = jtFile;
    }

    public void setMRootGroupNode(SceneGraphComponent rootGroupNode) {
        mRootGroupNode = rootGroupNode;
    }

    private void setRootGroupNode(SceneGraphComponent node) {
        mRootGroupNode = node;
    }

    public ByteReader getReader() {
        return reader;
    }

    public void setReader(ByteReader reader) {
        this.reader = reader;
    }

}
