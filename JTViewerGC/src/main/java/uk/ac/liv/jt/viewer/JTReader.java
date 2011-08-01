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
package uk.ac.liv.jt.viewer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.liv.jt.format.JTFile;
import uk.ac.liv.jt.format.elements.TriStripSetShapeLODElement;
import uk.ac.liv.jt.segments.LSGSegment;
import de.jreality.reader.AbstractReader;
import de.jreality.util.Input;


/**
 * Implementation of a JT file Reader for the JReality system. 
 * This needs to be properly registered in the system.
 * 
 * @see JTViewer
 * @author fabio
 *
 */
public class JTReader extends AbstractReader {

    private JTFile jtFile;

    //private ByteReader reader;


    @Override
    public void setInput(Input input) throws IOException {
        super.setInput(input);

        TriStripSetShapeLODElement.testDisplay = false;

        /*
         * as JT files can be big, we first need to create a local disk cache
         * copy
         */

        File jtf = null;
        try {
            jtf = input.toFile();
            
        } catch (Exception e) {

        } 
        if (jtf == null)   
            try {
                URI uri = input.toURL().toURI();
                jtf = new  File(uri);
                
            } catch (Exception e) {
                System.out.println("copy inputStream to file");
                InputStream inputStream = input.getInputStream();  
                jtf = copyToTemp(inputStream);
            }



            jtFile = new JTFile(jtf, jtf.toURI());

            // read file header and TOC, and returns the pointer to the LSG segment
            LSGSegment lsg = jtFile.read();

            // root is the root element declared in the reader interface
            root = lsg.generateSceneGraph();

    }


    /**
     * Utility method, copies content of an inputStream to temp a file
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



}
