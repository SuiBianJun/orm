package utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import entity.User;

public class ReflectUtils {
	
	public ReflectUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public Object invokeGetterMethod(Object obj, String filedName) {
		
		Class<?> cls = obj.getClass();
		String methodName = "get" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1, filedName.length());
		System.out.println(methodName);
		try {
			
			Method method = cls.getMethod(methodName);
			
			Object result = method.invoke(obj);
			
			return result;
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void invokeSetterMethod(Object obj, String filedName, Object value) {
		
		
		Class<?> cls = obj.getClass();
		
		//System.out.println(methodName);
		
		Field[] field;
		try {
			field = cls.getDeclaredFields();
			for(Field f : field) {
				if(f.getName().equals(filedName)) {
					f.setAccessible(true);
					f.set(obj, value);
					break;
				}
			}
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		ReflectUtils reflectUtils = new ReflectUtils();
		
		User user = new User();
		user.setId(1);
		user.setName("\"e\"");
		
		//System.out.println(reflectUtils.invokeGetterMethod(user, "id"));
		
		reflectUtils.invokeSetterMethod(user, "id", 2);
		
		System.out.println(user.getId());
		
		//System.out.println(user.getId());
	}
	
}
