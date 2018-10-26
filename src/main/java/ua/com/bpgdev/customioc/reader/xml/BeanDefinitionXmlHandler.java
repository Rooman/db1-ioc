package ua.com.bpgdev.customioc.reader.xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import ua.com.bpgdev.customioc.entity.BeanDefinition;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;

public class BeanDefinitionXmlHandler extends DefaultHandler {
    private Deque<BeanDefinition> beanDefinitions = new ArrayDeque<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if ("bean".equalsIgnoreCase(qName)) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setValueDependency(new HashMap<>());
            beanDefinition.setRefDependency(new HashMap<>());

            beanDefinition.setId(attributes.getValue("id"));
            beanDefinition.setClassName(attributes.getValue("class"));

            beanDefinitions.push(beanDefinition);

        } else if ("property".equalsIgnoreCase(qName)) {
            BeanDefinition beanDefinition = beanDefinitions.peek();
            String propertyName = attributes.getValue("name");
            String valueDependency = attributes.getValue("value");
            String refDependency = attributes.getValue("ref");
            if (valueDependency != null) {
                beanDefinition.getValueDependency().put(propertyName, valueDependency);
            } else {
                beanDefinition.getRefDependency().put(propertyName, refDependency);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
    }

    public Collection<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

}
