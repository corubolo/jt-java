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
import java.io.IOException;

import uk.ac.liv.jt.debug.DebugJTReader;
import uk.ac.liv.jt.segments.LSGSegment;
import de.jreality.plugin.JRViewer;
import de.jreality.plugin.JRViewer.ContentType;
import de.jreality.plugin.basic.Inspector;
import de.jreality.plugin.content.ContentLoader;
import de.jreality.plugin.content.ContentTools;
import de.jreality.reader.Readers;
import de.jreality.scene.Viewer;
import de.jtem.jrworkspace.plugin.sidecontainer.template.ShrinkPanelPlugin;

// JPanel -> http://www3.math.tu-berlin.de/jreality/phpbb/viewtopic.php?f=3&t=455
// Swing integration : http://www3.math.tu-berlin.de/jreality/phpbb/viewtopic.php?f=3&t=53


/**
 *  Class containing the main method. This class register the JT format reader class 
 *  in the Jreality system,
 *  instantiates an instance of a JReality viewer, and opens a sample JT file 
 *  @see JTReader 
 *  
 */
public class JTViewer {

    /**
     * @param args ignored

     */
    public static void main(String[] args) {

        DebugJTReader.debugCodec = false;
        DebugJTReader.debugMode = false;
        LSGSegment.doRender = false;
        
        // register the reader class for the JT-format
        Readers.registerReader("JT", JTReader.class);
        // register the file ending .jt for files containing JT-format data
        Readers.registerFileEndings("JT", "jt"); 

        JRViewer v = new JRViewer();
        v.setPropertiesFile("JRViewer.xml");
        v.setPropertiesResource(JRViewer.class, "JRViewer.xml");
        v.addBasicUI();

        v.registerPlugin(new ContentLoader());
        v.registerPlugin(new ContentTools());
        v.getPlugin(Inspector.class).setInitialPosition(
                ShrinkPanelPlugin.SHRINKER_LEFT);
        v.addContentSupport(ContentType.CenteredAndScaled);
        v.setShowPanelSlots(true, false, false, false);

        
//         try {
//            v.setContent(Readers.read(new File(
//              "/Users/fabio/Downloads/JT files/C-bend.jt")));
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }			// 1 segment is false
          v.startup();
          Viewer vs =         v.getViewer();

        



    }

}
