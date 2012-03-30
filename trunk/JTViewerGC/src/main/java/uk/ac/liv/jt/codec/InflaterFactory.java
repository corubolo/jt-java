/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.liv.jt.codec;

/**
 * Java instance of InflaterFactory
 * Allows you to inject different Inflater implementations in runtime.
 * (IKVM's inflater implementation is buggy, 
 * need to use different platform dependent inflater)
 * 
 * @author przym
 */
public class InflaterFactory implements IInflaterFactory {
    
    private static IInflaterFactory factory;
    
    public static void setInflaterFactory(IInflaterFactory factory) {
        InflaterFactory.factory = factory;
    }
    
    public static IInflaterFactory getInflaterFactoryInstance() {
        if(InflaterFactory.factory == null)
            InflaterFactory.factory = new InflaterFactory();
        return factory;
    }
    
    protected InflaterFactory() { }
    
    public IInflater createInflater() {
        return new InternalInflater();
    }
        
}
