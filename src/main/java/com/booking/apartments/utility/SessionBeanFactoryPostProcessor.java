package com.booking.apartments.utility;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        configurableListableBeanFactory.registerScope("session", new Scope() {

            private Map<String, Object> scopedObjects = Collections.synchronizedMap(new HashMap<String, Object>());

            private Map<String, Runnable> destructionCallbacks = Collections.synchronizedMap(new HashMap<String, Runnable>());

            @Override
            public Object get(String name, ObjectFactory<?> objectFactory) {
                if (!scopedObjects.containsKey(name)) {
                    scopedObjects.put(name, objectFactory.getObject());
                }
                return scopedObjects.get(name);
            }

            @Override
            public Object remove(String name) {
                destructionCallbacks.remove(name);
                return scopedObjects.remove(name);
            }

            @Override
            public void registerDestructionCallback(String name, Runnable callback) {
                destructionCallbacks.put(name, callback);
            }

            @Override
            public Object resolveContextualObject(String s) {
                return null;
            }

            @Override
            public String getConversationId() {
                return "session";
            }
        });
    }
}
