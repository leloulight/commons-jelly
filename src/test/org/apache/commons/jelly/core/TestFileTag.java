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
package org.apache.commons.jelly.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestSuite;

import org.apache.commons.jelly.Script;

/**
 * @author <a href="mailto:robert@bull-enterprises.com">Robert McIntosh</a>
 * @version $Revision: 1.2 $
 */
public class TestFileTag extends BaseJellyTest
{

    public TestFileTag(String name)
    {
        super(name);
    }

    public static TestSuite suite() throws Exception
    {
        return new TestSuite(TestFileTag.class);
    }

    public void testSimpleFileTag() throws Exception
    {
        setUpScript("testFileTag.jelly");
        Script script = getJelly().compileScript();

        script.run(getJellyContext(), getXMLOutput());

        FileInputStream fis = new FileInputStream("target/testFileTag.tmp");
        String data = readInputStreamIntoString(fis);
        fis.close();


        //FIXME This doesn't take into account attribute ordering
        //assertEquals("target/testFileTag.tmp", "<html xmlns=\"http://www.w3.org/1999/xhtml\"  xml:lang=\"en\"  lang=\"en\"></html>", data);

        //assertTrue( System.getProperty( "java.runtime.version" ).equals( getJellyContext().getVariable("propertyName" ) ) );
    }

    /**
     * Transfers an input stream to a string
     * @param is the stream to read the data from
     * @return A string containing the data from the input stream
     **/
    protected static final int BUFFER_SIZE = 16384;

    public static String readInputStreamIntoString(InputStream is) throws IOException {
        StringBuffer buffer = new StringBuffer();

        final byte b[] = new byte[BUFFER_SIZE];
        while (true) {
            int read = is.read(b);
            if (read == -1)
                break;

            String s = new String(b, 0, read);
            buffer.append(s);
        }
        return buffer.toString();
    }

}