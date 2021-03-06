package ua.com.bpgdev.customioc.context;

import ua.com.bpgdev.customioc.entity.Bean;
import ua.com.bpgdev.customioc.entity.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReferenceInjector extends Injector {

    public ReferenceInjector(Map<BeanDefinition, Bean> beanDefinitionToBeanMap) {
        super(beanDefinitionToBeanMap);
    }

    @Override
    Map<String, ?> getDependencies(BeanDefinition beanDefinition) {
        Map<String, Object> refDependencies = new HashMap<>();
        Map<String, String> beanRefDependencies = beanDefinition.getRefDependencies();
        for (String propertyName : beanRefDependencies.keySet()) {
            boolean refFound = false;
            String refDependencyBeanId = beanRefDependencies.get(propertyName);

            for (Bean bean : beanDefinitionToBeanMap.values()) {
                if (bean.getId().equals(refDependencyBeanId)) {
                    refDependencies.put(propertyName, bean.getValue());
                    refFound = true;
                    break;
                }
            }

            if (!refFound) {
                throw new BeanInstantiationException("Reference bean not found: " + refDependencyBeanId);
            }
        }
        return refDependencies;
    }

    @Override
    void injectPropertyIntoSetter(Object beanValue, Method method, Object propertyToInject) throws InvocationTargetException, IllegalAccessException {
        method.invoke(beanValue, propertyToInject);
    }
}