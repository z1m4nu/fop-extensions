/*
 * #%L
 * fop-zxing
 * %%
 * Copyright (C) 2012 Dmitriy Yakovlev
 * %%
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
 * #L%
 */
package org.crossroad.fop.barcode;

import java.awt.geom.Point2D;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * Created with IntelliJ IDEA.
 * User: hobbut
 * Date: 11.02.12
 * Time: 15:24
 */

public class BarcodeElement extends BarcodeObject {

    public BarcodeElement(FONode parent) {
        super(parent);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.fop.fo.XMLObj#getDimension(java.awt.geom.Point2D)
     */
    public Point2D getDimension(Point2D view) {
        Configuration configuration = ConfigurationUtil.toConfiguration(getDOMDocument().getDocumentElement());
        BarcodeDimension dimension = new BarcodeDimension(configuration.getChild(IKeywords.NAME_SIZE));
        return dimension.toPoint2D();
    }

    public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
        super.processNode(elementName, locator, attlist, propertyList);
        createBasicDocument();
    }
}
