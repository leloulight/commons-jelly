/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//jelly/src/java/org/apache/commons/jelly/tags/core/SetTag.java,v 1.14 2003/01/31 14:16:25 jstrachan Exp $
 * $Revision: 1.14 $
 * $Date: 2003/01/31 14:16:25 $
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
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
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
 * $Id: SetTag.java,v 1.14 2003/01/31 14:16:25 jstrachan Exp $
 */
package org.apache.commons.jelly.tags.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** A tag which sets a variable from the result of an expression 
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 1.14 $
  */
public class SetTag extends TagSupport {

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(SetTag.class);
    
    /** The variable name to export. */
    private String var;

    /** The variable scope to export */
    private String scope;
    
    /** The expression to evaluate. */
    private Expression value;

    /** The target object on which to set a property. */
    private Object target;

    /** The name of the property to set on the target object. */
    private String property;
    
    /** Should we XML encode the body of this tag as text? */
    private boolean encode = true;

    public SetTag() {
    }

    // Tag interface
    //------------------------------------------------------------------------- 
    public void doTag(XMLOutput output) throws JellyTagException {
        // perform validation up front to fail fast
        if ( var != null ) {
            if ( target != null || property != null ) {
                throw new JellyTagException( "The 'target' and 'property' attributes cannot be used in combination with the 'var' attribute" );
            }
        }
        else {
            if ( target == null ) {
                throw new JellyTagException( "Either a 'var' or a 'target' attribute must be defined for this tag" );
            }
            if ( property == null ) {
                throw new JellyTagException( "The 'target' attribute requires the 'property' attribute" );
            }
        }

        Object answer = null;
        if ( value != null ) {
            answer = value.evaluate(context);
        }
        else {
            answer = getBodyText(isEncode());
        }
        
        if ( var != null ) {
            if ( scope != null ) {
                context.setVariable(var, scope, answer);
            }
            else {
                context.setVariable(var, answer);
            }
        }
        else {
            setPropertyValue( target, property, answer );
        }
    }

    // Properties
    //-------------------------------------------------------------------------                
    /** Sets the variable name to define for this expression
     */
    public void setVar(String var) {
        this.var = var;
    }
    
    /** 
     * Sets the variable scope for this variable. For example setting this value to 'parent' will
     * set this value in the parent scope. When Jelly is run from inside a Servlet environment
     * then other scopes will be available such as 'request', 'session' or 'application'.
     * 
     * Other applications may implement their own custom scopes.
     */
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    /** Sets the expression to evaluate. */
    public void setValue(Expression value) {
        this.value = value;
    }
    
    /** Sets the target object on which to set a property. */
    public void setTarget(Object target) {
        this.target = target;
    }
           
    /** Sets the name of the property to set on the target object. */
    public void setProperty(String property) {
        this.property = property;
    }
    
    /**
     * Returns whether the body of this tag will be XML encoded or not.
     */
    public boolean isEncode() {
        return encode;
    }

    /**
     * Sets whether the body of the tag should be XML encoded as text (so that &lt; and &gt; are
     * encoded as &amp;lt; and &amp;gt;) or leave the text as XML which is the default.
     * This is only used if this tag is specified with no value so that the text body of this
     * tag is used as the body.
     */
    public void setEncode(boolean encode) {
        this.encode = encode;
    }


    // Implementation methods
    //-------------------------------------------------------------------------                
    protected void setPropertyValue( Object target, String property, Object value ) {
        try {
            if ( target instanceof Map ) {
                Map map = (Map) target;
                map.put( property, value );
            }
            else {
                BeanUtils.setProperty( target, property, value );       
            }
        } catch (InvocationTargetException e) {
            log.error( "Failed to set the property: " + property + " on bean: " + target + " to value: " + value + " due to exception: " + e, e );
        } catch (IllegalAccessException e) {
            log.error( "Failed to set the property: " + property + " on bean: " + target + " to value: " + value + " due to exception: " + e, e );
        }
    }

}
