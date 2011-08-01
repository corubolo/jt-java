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
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import uk.ac.liv.jt.format.ByteReader;
import uk.ac.liv.jt.format.JTFile;
import uk.ac.liv.jt.segments.JTSegment;

public class JTFeatureExtractor {

    public static void main(String[] args) {
        File jtf;
        File d;
        try {
            d = new File(new URI("file:///Users/fabio/Downloads/JT%20files/Philips/"));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        File[] f = d.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".jt");
            }
        });

        for (File j : f)
            try {
                read(j);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }

    private static void read(File j) throws IOException {
        ByteReader reader = new ByteReader(j);
        JTFile jtfile = new JTFile(reader);
        // System.out.println();
        // System.out.println("=================");
        // System.out.println("== File Header ==");
        // System.out.println("=================");
        // System.out.println();

        // Read File header
        System.out.println("FILE : " + j.getName());
        jtfile.readHeader();
        if (!jtfile.getVersion().startsWith("Version 8")) {
            System.err.println("Unsuppoted " + jtfile.getVersion());
            return;
        }
        //
        // System.out.println();
        // System.out.println("=================");
        // System.out.println("== TOC Segment ==");
        // System.out.println("=================");
        // System.out.println();

        // Read the TOC -> The TOC can be at the end
        reader.position(jtfile.getTocOffset());
        jtfile.readTOC();

        // System.out.println();
        // System.out.println();

        // Read the data segments
        Collection<JTSegment> segments = jtfile.getSegments();
        for (JTSegment segment : segments) {
            System.out.println(segment);
            //            if (segment.getType() != 7 && segment.getType() != 1
            //                    && segment.getType() != 3 && segment.getType() != 4)
            //                System.out.println(segment.getType());
            if ((segment.getType() == 3 || segment.getType() == 4)){

                segment.read();

            }
        }

    }

}
