/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.liv.jt.codec;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Wrapper for java.util.zip.Inflater, implementing IInflater
 * Created by InflaterFactory
 * 
 * Default inflater for Java version
 * 
 * 
 * @author przym
 */
public class InternalInflater implements IInflater {

    Inflater inflater;

    public void init(boolean nowrap) {
        inflater = new Inflater(nowrap);
    }

    public boolean finished() {
        return inflater.finished();
    }

    public boolean needsInput() {
        return inflater.needsInput();
    }

    public void setInput(byte[] tmp) {
        inflater.setInput(tmp);
    }

    public int inflate(byte[] p) throws DataFormatException {
        return inflater.inflate(p);
    }

    public void end() {
        inflater.end();
    }
}
