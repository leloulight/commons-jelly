/*
 * Copyright 2002,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jelly.tags.bsf;

import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.expression.ExpressionFactory;
import org.apache.commons.jelly.impl.TagFactory;
import org.apache.commons.jelly.tags.core.CoreTagLibrary;
import org.xml.sax.Attributes;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;


/** Describes the Taglib. This class could be generated by XDoclet
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class BSFTagLibrary extends CoreTagLibrary {

    private BSFExpressionFactory expressionFactory = new BSFExpressionFactory();

    public BSFTagLibrary() {
        registerTagFactory(
            "script",
            new TagFactory() {
                public Tag createTag(String name, Attributes attributes)
                    throws JellyException {
                    return createScriptTag(name, attributes);
                }
            }
            );
    }

    public BSFTagLibrary(String language) {
        this();
        setLanguage(language);
    }

    public void setLanguage(String language) {
        expressionFactory.setLanguage(language);
    }

    /** Allows derived tag libraries to use their own factory */
    protected ExpressionFactory getExpressionFactory() {
        return expressionFactory;
    }

    protected BSFEngine getBSFEngine() throws BSFException {
        return expressionFactory.getBSFEngine();
    }

    /**
     * Factory method to create a new ScriptTag with a BSFEngine
     *
     * @param name is the name of the tag (typically 'script')
     * @param attributes the attributes of the tag
     * @return Tag
     */
    protected Tag createScriptTag(String name, Attributes attributes) throws JellyException {
        try {
            return new ScriptTag( expressionFactory.getBSFEngine(),
                                  expressionFactory.getBSFManager());
        }
        catch (BSFException e) {
            throw new JellyException("Failed to create BSFEngine: " + e, e);
        }
    }
}
