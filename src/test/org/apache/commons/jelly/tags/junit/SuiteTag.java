/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//jelly/src/test/org/apache/commons/jelly/tags/junit/SuiteTag.java,v 1.3 2003/10/09 21:21:31 rdonkin Exp $
 * $Revision: 1.3 $
 * $Date: 2003/10/09 21:21:31 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 * 
 * $Id: SuiteTag.java,v 1.3 2003/10/09 21:21:31 rdonkin Exp $
 */
package org.apache.commons.jelly.tags.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;

/** 
 * Represents a collection of TestCases.. This tag is analagous to
 * JUnit's TestSuite class.
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision: 1.3 $
 */
public class SuiteTag extends TagSupport {

    /** the test suite this tag created */
    private TestSuite suite;
    
    /** the name of the variable of the test suite */
    private String var;
    
    /** the name of the test suite to create */
    private String name;

    public SuiteTag() {
    }
    
    /**
     * Adds a new Test to this suite
     */
    public void addTest(Test test) {
        getSuite().addTest(test);
    }    
    
    // Tag interface
    //------------------------------------------------------------------------- 
    public void doTag(XMLOutput output) throws JellyTagException {
        suite = createSuite();
        
        TestSuite parent = (TestSuite) context.getVariable("org.apache.commons.jelly.junit.suite");        
        if ( parent == null ) {
            context.setVariable("org.apache.commons.jelly.junit.suite", suite );
        }
        else {
            parent.addTest( suite );
        }

        invokeBody(output);
        
        if ( var != null ) {
            context.setVariable(var, suite);
        }            
    }
    
    // Properties
    //-------------------------------------------------------------------------                
    public TestSuite getSuite() {
        return suite;
    }
    
    /**
     * Sets the name of the test suite whichi is exported
     */
    public void setVar(String var) {
        this.var = var;
    }
    
    /**
     * @return the name of this test suite
     */
    public String getName() {
        return name;
    }
    
    /** 
     * Sets the name of this test suite
     */
    public void setName(String name) {
        this.name = name;
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                
    
    /**
     * Factory method to create a new TestSuite
     */
    protected TestSuite createSuite() {
        if ( name == null ) {
            return new TestSuite();
        }
        else {
            return new TestSuite(name);
        }
    }
}