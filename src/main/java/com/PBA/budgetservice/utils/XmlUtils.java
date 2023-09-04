package com.PBA.budgetservice.utils;

import org.jdom2.Element;

import java.util.Map;

public class XmlUtils {
    public static final String BODY = "Body";
    public static final String CUBE = "Cube";
    public static final String CURRENCY = "currency";

    public static Element getElementByName(String name, Element rootNode) {
        return elementNameToChildExtractor.get(name).getChildOfElement(rootNode);
    }

    private static Element getBodyChild(Element rootNode) {
        return rootNode.getChild(BODY, rootNode.getNamespace());
    }

    private static Element getCubeChild(Element rootNode) {
        return rootNode.getChild(CUBE, rootNode.getNamespace());
    }

    private static final Map<String, XmlChildElementExtractor> elementNameToChildExtractor = Map.of(
            BODY, XmlUtils::getBodyChild,
            CUBE, XmlUtils::getCubeChild
    );
}
