/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.liv.jt.codec;

import java.util.zip.DataFormatException;

/**
 * Interface for inflater, created by IInflaterFactory
 * 
 * @author przym
 */
public interface IInflater {
    
    /**
     * Inits inner Inflater
     * 
     * @param nowrap if true, read header, omit it otherwise
     */
    public void init(boolean nowrap);
    
    /**
     * 
     * @return true when stream under inflater is empty, false otherwise
     */
    public boolean finished();
    
    /**
     * 
     * @return true when input is needed
     */
    public boolean needsInput();
    
    /**
     * Sets input (byte array)
     * 
     * @param tmp bytearray, that contains data to inflate
     */
    public void setInput(byte[] tmp);
    
    /**
     * Inflates internal data 
     * 
     * @param p destination
     * @return number of bytes inflated
     * @throws DataFormatException 
     */
    public int inflate(byte[] p) throws DataFormatException;
    
    /**
     * Finishes all the underlying resources
     */
    public void end();
    
    
}
