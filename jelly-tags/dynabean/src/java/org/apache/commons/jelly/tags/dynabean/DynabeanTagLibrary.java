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
package org.apache.commons.jelly.tags.dynabean;

import org.apache.commons.jelly.TagLibrary;

/** Describes the Taglib. This class could be generated by XDoclet
  *
  * @author Theo Niemeijer
  * @version $Revision$
  */
public class DynabeanTagLibrary extends TagLibrary {

    public DynabeanTagLibrary() {
        registerTag("dynaclass", DynaclassTag.class);
        registerTag("property", PropertyTag.class);
        registerTag("dynabean", DynabeanTag.class);
        registerTag("set", SetTag.class);
    }

}
