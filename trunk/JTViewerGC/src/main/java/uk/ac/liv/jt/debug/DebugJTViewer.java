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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import de.jreality.reader.Readers;
import de.jreality.scene.SceneGraphComponent;

// JPanel -> http://www3.math.tu-berlin.de/jreality/phpbb/viewtopic.php?f=3&t=455
// Swing integration : http://www3.math.tu-berlin.de/jreality/phpbb/viewtopic.php?f=3&t=53

public class DebugJTViewer {

    /**
     * @param args
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws MalformedURLException,
            URISyntaxException {

        // register the reader class for the JT-format
        Readers.registerReader("JT", DebugJTReader.class);
        // register the file ending .jt for files containing JT-format data
        Readers.registerFileEndings("JT", "jt");

        // load the sample file:
        //
        // This is not needed, after registering the reader one can load
        // DEMO-files from the File-Menu: File->Load
        //
//        File d = new File(JTViewer.class.getResource("/samples").toURI());
//        File[] f = d.listFiles(new FilenameFilter() {
//
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".jt");
//            }
//        });
//        // URL fileUrl = JTViewer.class.getResource("/samples/bnc.jt");
//        // URL fileUrl = JTViewer.class.getResource("/samples/conrod.jt");
//        // URL fileUrl =
//        // JTViewer.class.getResource("/samples/butterflyvalve.jt");
//
        SceneGraphComponent content = null;
//        if (false)
//            for (File j : f) {
//                URL fileUrl = j.toURI().toURL();
//                System.out.println(fileUrl);
//                try {
//                    content = Readers.read(fileUrl);
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        else {
//            URL fileUrl = new File("src/main/resources/samples/bnc.jt").toURI().toURL();
       	  URL fileUrl = new File("samples/conrod.jt").toURI().toURL();
//            URL fileUrl =new File("src/main/resources/samples/satphone.jt").toURI().toURL();
//      URL fileUrl =new File("src/main/resources/samples/butterflyvalve.jt").toURI().toURL();
//      URL fileUrl =new File("src/main/resources/samples/wingflap.jt").toURI().toURL();
//      URL fileUrl =new File("src/main/resources/samples/cam.jt").toURI().toURL();
            try {
                content = Readers.read(fileUrl);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//        }
        // try {
        // content = Readers.read(fileUrl);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // JRViewer v = new JRViewer();
        // v.addBasicUI();
        // v.addContentSupport(ContentType.CenteredAndScaled);
        // v.addContentUI();
        // v.setContent(content);
        // v.startup();

    }

}
