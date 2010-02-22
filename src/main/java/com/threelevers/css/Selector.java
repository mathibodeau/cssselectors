package com.threelevers.css;

import static com.google.common.collect.Iterables.concat;
import static com.threelevers.css.CssSelectors.selectors;
import static com.threelevers.css.Nodes.isElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.ImmutableSet;

public final class Selector {
    private final Element element;
    
    private Selector(Element element) {
        this.element = element;
    }
    
    public static Selector from(Element element) {
        return new Selector(element);
    }
    
    public static Selector from(Document doc) {
        return new Selector(doc.getDocumentElement());
    }

    public Iterable<Element> select(String selector) {
        return select(element, selectors(selector));
    }
    
    public Element selectUnique(String selector) {
        return selectUnique(element, selectors(selector));
    }

    static Iterable<Element> select(Element element, CssSelector matcher) {
        Iterable<Element> matches = ImmutableSet.of();
        if (matcher.matches(element)) {
            matches = concat(matches, Collections.singletonList(element));
        }
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (isElement(children.item(i))) {
                matches = concat(matches, select((Element) children.item(i), matcher));
            }
        }
        return matches;
    }
    
    static Element selectUnique(Element rootElement, CssSelector matcher) {
    	List<Element> elements = asList(select(rootElement, matcher));
    	if(elements.size() > 1)
    		throw new IllegalArgumentException("The CSS selector "+matcher+" returned "+elements.size()+" elements but was supposed to find 0 or 1.");
    	return elements.size() == 0 ? null : elements.get(0);
    }

	private static List<Element> asList(Iterable<Element> selectedElements) {
		List<Element> elements = new ArrayList<Element>();
    	for (Element element : selectedElements)
			elements.add(element);
		return elements;
	}

}
