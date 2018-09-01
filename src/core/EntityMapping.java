package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class EntityMapping {
	
	static Properties properties = new Properties();
	
	public EntityMapping() {
		// TODO Auto-generated constructor stub
	}
	
	public static Map<Class<?>, String> getEntityMap(){
		
		Map<Class<?>, String> map = new HashMap<>();
		
		try {
			
			properties.load(new FileInputStream(new File("entity-mapping.properties")));
			
			
			Set<Entry<Object, Object>> entrySet = properties.entrySet();
			
			for(Entry<Object, Object> entry : entrySet) {
				
				//System.out.println((String)entry.getKey());
				
				Class<?> cls = Class.forName((String)entry.getKey());
				
				map.put(cls, (String)entry.getValue());
			}
			
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	} 
	
	// test
	public static void main(String[] args) {
		//System.out.println(getEntityMap().size());
		
		Map<Class<?>, String> map = getEntityMap();
		
		Set<Entry<Class<?>, String>> entrySet = map.entrySet();
		
		for(Entry<Class<?>, String> entry : entrySet) {
			System.out.println(entry.getKey() + "===" + entry.getValue());
		}
	}

}
