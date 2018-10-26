package ua.com.bpgdev.customioc.lifecycle;

import ua.com.bpgdev.customioc.entity.BeanDefinition;

import java.util.List;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(List<BeanDefinition> definitions);
}