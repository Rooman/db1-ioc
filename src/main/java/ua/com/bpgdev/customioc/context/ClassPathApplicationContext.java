package ua.com.bpgdev.customioc.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.bpgdev.customioc.entity.Bean;
import ua.com.bpgdev.customioc.entity.BeanDefinition;
import ua.com.bpgdev.customioc.reader.BeanDefinitionReader;
import ua.com.bpgdev.customioc.reader.xml.XmlBeanDefinitionReader;

import java.util.ArrayList;
import java.util.List;

public class ClassPathApplicationContext implements ApplicationContext {
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathApplicationContext.class);
    private static final String LOG_MESSAGE_DELIMITER = "---------------------------------------";

    private List<Bean> beans;

    public ClassPathApplicationContext(String contextFile) {
        BeanDefinitionReader beanDefinitionReader =
                new XmlBeanDefinitionReader(contextFile);

        List<BeanDefinition> beanDefinitions = beanDefinitionReader.getBeanDefinitions();

        beans = constructBeans(beanDefinitions);
        injectValueDependency(beanDefinitions);
        injectRefDependency(beanDefinitions);
    }

    private List<Bean> constructBeans(List<BeanDefinition> beanDefinitions) {
        try {
            List<Bean> beans = new ArrayList<>();

            LOG.debug(LOG_MESSAGE_DELIMITER);
            LOG.info("Starting to construct Beans ...");

            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class clazz = Class.forName(beanDefinition.getClassName());
                Object instance = clazz.getDeclaredConstructor().newInstance();
                Bean bean = new Bean(beanDefinition.getId(), instance);
                beans.add(bean);

                LOG.debug("Bean with id = {} has added to Beans list.", bean.getId());
            }

            LOG.info("Constructing Beans has finished successfully.");

            return beans;
        } catch (Exception e) {
            LOG.error("Error during bean creation", e);
            throw new RuntimeException(e);
        }
    }

    private void injectValueDependency(List<BeanDefinition> beanDefinitions) {
        LOG.debug(LOG_MESSAGE_DELIMITER);
        LOG.debug("Starting to inject dependency by value ...");

        injectDependency(beanDefinitions, new SetValueField());

        LOG.debug("Injecting dependency by value to Beans has finished successfully.");
    }

    private void injectRefDependency(List<BeanDefinition> beanDefinitions) {
        LOG.debug(LOG_MESSAGE_DELIMITER);
        LOG.debug("Starting to inject dependency by reference ...");

        injectDependency(beanDefinitions, new SetRefField(beans));

        LOG.debug("Injecting dependency by reference to Beans has finished successfully.");
    }

    private void injectDependency(List<BeanDefinition> beanDefinitions, SetFieldStrategy setFieldStrategy) {
        try {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                for (Bean bean : beans) {
                    if (bean.getId().equals(beanDefinition.getId())) {
                        Object value = bean.getValue();
                        // Here is Strategy pattern
                        setFieldStrategy.doInjection(value.getClass(), value, beanDefinition);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        for (Bean bean : beans) {
            Class beanClassName = bean.getValue().getClass();
            if (beanClassName.equals(clazz)) {
                return clazz.cast(bean.getValue());
            }
        }

        // NoUniqueBeanException
        return null;
    }

    public <T> T getBean(String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return (T) bean.getValue();
            }
        }
        return null;
    }

    public <T> T getBean(String id, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id) &&
                    bean.getValue().getClass().toString().equals(clazz.getName())) {
                return (T) bean.getValue();
            }
        }
        return null;
    }

    public List<String> getBeanNames() {
        List<String> result = new ArrayList<>();
        for (Bean bean : beans) {
            result.add(bean.getId());
        }
        return result;
    }


}
