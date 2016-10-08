package com.blackbox.ids.core.util;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanFactory;

public class BlackboxPropertyUtil  {

    private static AbstractBeanFactory beanFactory;

    private static final Map<String,String> cache = new ConcurrentHashMap<>(); 

    @Autowired 
    protected BlackboxPropertyUtil(AbstractBeanFactory beanFactory) {
    	BlackboxPropertyUtil.beanFactory = beanFactory; 
    } 

    public  static String getProperty(String key) { 
        if(cache.containsKey(key)){ 
            return cache.get(key); 
        } 

        String foundProp = null; 
        try { 
            foundProp = beanFactory.resolveEmbeddedValue("${" + key.trim() + "}");        
            cache.put(key,foundProp);        
        } catch (IllegalArgumentException ex) { 
           // ok - property was not found 
        }
        
        //TODO need to be refactored in correct way
        if(foundProp == null && key.equalsIgnoreCase("root.folder.dir")) {
        	String path = BlackboxPropertyUtil.class.getClassLoader().getResource("environment.properties").getPath();
	    	File file = new File(path);
        	path = file.getParentFile().getParentFile().getParentFile().getParentFile().getPath();
        	path = path + File.separator + "bb-integration-test" + File.separator + "src" + File.separator + "test" + 
        			File.separator + "resources" + File.separator + "META-INF" + File.separator + "test-data";
        	foundProp = path;
        }
        return foundProp;
    } 

}
