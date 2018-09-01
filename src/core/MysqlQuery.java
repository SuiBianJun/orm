package core;

import javabean.ColumnInfo;
import javabean.TableInfo;
import utils.ReflectUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class MysqlQuery implements Query {

    static List<Connection> connectionList = new ArrayList<Connection>(10);
    
    // use this connection to execute sql
    Connection conn;
    
    // entity-table mapping
    Map<Class<?>, String> entityMap;
    
    
    public MysqlQuery(Connection conn) {
		// TODO Auto-generated constructor stub
    	this.conn = conn;
    	entityMap = EntityMapping.getEntityMap();
    	/*try {
			TableContext.loadPoTables();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

    @Override
    public void delete(Class<?> cls, int primaryKeyValue) {

    	String tableName = entityMap.get(cls);
    	
        //delete from tablename where primaryKeyValue = 1
        try {
        	
        	Map<Class<?>, TableInfo> map = TableContext.loadPoTables();
        	// get po class with table name
        	Class<?> cls2 = TableContext.entityMap.get(tableName);
        	
        	TableInfo ti = map.get(cls2);

            String sql = "delete from " + ti.getTableName() + " where " + ti.getOnlyPrimaryKey().getName() + " = " + primaryKeyValue;
            System.out.println(sql);
            executeDML(sql);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Object object) {

        Class<?> cls = object.getClass();
        String tableName = entityMap.get(cls);
        try {
        	Map<Class<?>, TableInfo> map = TableContext.loadPoTables();
        	// get po class with table name
        	Class<?> cls2 = TableContext.entityMap.get(tableName);
        	
        	TableInfo ti = map.get(cls2);
        	
            String primaryKey = ti.getOnlyPrimaryKey().getName();
            
            Integer primaryKeyValue = (Integer) new ReflectUtils().invokeGetterMethod(object, primaryKey);
            delete(cls, (int)primaryKeyValue);
        }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // insert Object with field
    @Override
    public int insert(Object object) {
        Class<?> cls = object.getClass();
        // get entity-mapping relate tableName
        String tableName = entityMap.get(cls);
        try {
            //TableInfo ti = TableContext.loadPoTables().get(cls);
        	
        	Map<Class<?>, TableInfo> map = TableContext.loadPoTables();
        	// get po class with table name
        	Class<?> cls2 = TableContext.entityMap.get(tableName);
        	
        	TableInfo ti = map.get(cls2);
            
            StringBuilder stringBuilder = new StringBuilder();

            // constrcut sql statement
            List<Object> list = new ArrayList<>();
            ReflectUtils reflectUtils = new ReflectUtils();
            stringBuilder.append("insert into ").append(ti.getTableName()).append(" ").append("(");// 键值对对应
            for(ColumnInfo ci : ti.getColumnInfoList()){
                stringBuilder.append(ci.getName()).append(", ");
                // add value order
                list.add(reflectUtils.invokeGetterMethod(object, ci.getName()));
            }
            
            stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            stringBuilder.append(") values (");
            
            StringBuffer sb = new StringBuffer();
            for(Object v : list) {
            	sb.append(v).append(", ");
            }

            stringBuilder.append(sb.toString().substring(0, sb.toString().length() - 2)).append(")");
            
            // add real value
            /*Method method = cls.getMethod("getId", null);
            Method method2 = cls.getMethod("getName", null);
            stringBuilder.append((int)method.invoke(object, null)).append(", ");
            stringBuilder.append(method2.invoke(object, null)).append(")");*/

            System.out.println(stringBuilder.toString());
            executeDML(stringBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

	@Override
	public void update(Object object, Object primaryKey) {
		// TODO Auto-generated method stub
		
		TableInfo ti = getTableInfoByClass(object);
		
		StringBuffer stringBuffer = new StringBuffer();
		
		stringBuffer.append("update ").append(ti.getTableName()).append(" set ");
		ReflectUtils reflectUtils = new ReflectUtils();
		StringBuffer stringBuffer2 = new StringBuffer();
		for(ColumnInfo ci : ti.getColumnInfoList()) {
			stringBuffer2.append(ci.getName()).append("=").append(reflectUtils.invokeGetterMethod(object, ci.getName())).append(", ");
		}
		stringBuffer.append(stringBuffer2.toString().substring(0, stringBuffer2.toString().length() - 2));
		stringBuffer.append(" where ").append(ti.getOnlyPrimaryKey().getName()).append("=").append(primaryKey);
		
		System.out.println(stringBuffer.toString());
		
		executeDML(stringBuffer.toString());
	}
	

    @Override
    public Object querySingleColumn(Class<?> cls, Object primaryKey) {
            //TableInfo ti = TableContext.loadPoTables().get(object.getClass());
        	TableInfo ti = getTableInfoByClass(cls);
            String sql = "select * from " + ti.getTableName() + " where " + ti.getOnlyPrimaryKey().getName() + "=" + primaryKey;
            try {
                Connection conn = SingleConnection.getInstance().getConnection();
                //Connection conn = ConnectionPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);

                ResultSet rs = ps.executeQuery();
                
                //Class<?> cls = object.getClass();
                
                Object obj = cls.newInstance();
                ReflectUtils reflectUtils = new ReflectUtils();
                Field[] filed = cls.getDeclaredFields();
                if(rs.next()) {
                	for(Field f : filed) {
                		reflectUtils.invokeSetterMethod(obj, f.getName(), rs.getObject(f.getName()));
                	}
                }
                
                return obj;

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        
        return null;
        
    }
    
    @Override
	public List<Object> queryMultiColumn(Class<?> cls, Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Object> list = new ArrayList<>();
    	TableInfo ti = getTableInfoByClass(cls);
    	
    	Set<Entry<String, Object>> entry = map.entrySet();
    	StringBuffer sb = new StringBuffer();
    	for(Entry<String, Object> e : entry) {
    		sb.append(e.getKey()).append("=").append(e.getValue());
    	}
    	
        String sql = "select * from " + ti.getTableName() + " where " + sb.toString();
        System.out.println(sql);
        Connection conn = SingleConnection.getInstance().getConnection();
        //Connection conn = ConnectionPool.getConnection();
        PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
	        
	        //Class<?> cls = object.getClass();
	        
	        Object obj = cls.newInstance();
	        ReflectUtils reflectUtils = new ReflectUtils();
	        Field[] filed = cls.getDeclaredFields();
	        while(rs.next()) {
	        	for(Field f : filed) {
	        		reflectUtils.invokeSetterMethod(obj, f.getName(), rs.getObject(f.getName()));
	        	}
	        	list.add(obj);
	        }
	        
	        return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return null;
	}

    // execute sql statement
    @Override
    public int executeDML(String sql) {
    	System.out.println(sql);
        try {
        	
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.execute();
            

        } catch (SQLException e) {
            e.printStackTrace();
            ConnectionPool.closeConnection(conn);
        } 
        
        return 0;
    }
    
    private TableInfo getTableInfoByClass(Class<?> cls) {
    	
    	//Class<?> cls = object.getClass();
        // get entity-mapping relate tableName
        String tableName = entityMap.get(cls);
        //TableInfo ti = TableContext.loadPoTables().get(cls);
    	
    	Map<Class<?>, TableInfo> map;
		try {
			map = TableContext.loadPoTables();
			Class<?> cls2 = TableContext.entityMap.get(tableName);
	    	
	    	TableInfo ti = map.get(cls2);
	    	
	    	return ti;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private TableInfo getTableInfoByClass(Object object) {
    	
    	Class<?> cls = object.getClass();
        // get entity-mapping relate tableName
        return getTableInfoByClass(cls);
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        Class<?> cls = Class.forName("po.T1");
        //new MysqlQuery().delete(cls, 1);
        Object object = cls.newInstance();
        Method method = cls.getMethod("setId", Integer.class);
        Method method2 = cls.getMethod("setName", String.class);
        method.invoke(object, 2);
        method2.invoke(object, "\"b\"");

        long start = System.currentTimeMillis();
        Connection conn = null;
        conn = SingleConnection.getInstance().getConnection();
        for(int i = 0; i < 3000; i++) {
            //new MysqlQuery().querySingleColumn(object);//26437
            test2(object, conn);//11468
            //10822
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static void test(Object object){
        try {
            TableInfo ti = TableContext.loadPoTables().get(object.getClass());
            String sql = "select * from " + ti.getTableName();
            Connection conn = null;
            try {
                //Connection conn = DBManager.getConnection();
                ConnectionPool cp = ConnectionPool.getInstance();
                conn = cp.getConnection();
                connectionList.add(conn);

                System.out.println(conn);
                PreparedStatement ps = conn.prepareStatement(sql);

                ResultSet rs = ps.executeQuery();
                while(rs.next())
                    System.out.println(rs.getInt(1) + "--" + rs.getString(2));

                if(connectionList.size() > 10){
                    for(int i = 0; i < 10; i++){
                        ConnectionPool.closeConnection(connectionList.get(0));
                        connectionList.remove(0);
                    }
                }
                //ConnectionPool.closeConnection(conn);
            } catch (SQLException e) {
                ConnectionPool.closeConnection(conn);
                e.printStackTrace();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public static void test2(Object object, Connection conn){
        try {
            TableInfo ti = TableContext.loadPoTables().get(object.getClass());
            String sql = "select * from " + ti.getTableName();
            try {
                //Connection conn = DBManager.getConnection();
                //ConnectionPool cp = ConnectionPool.getInstance();
                //conn = SingleConnection.getInstance().getConnection();
                System.out.println(conn);
                //connectionList.add(conn);

                //System.out.println(conn);
                PreparedStatement ps = conn.prepareStatement(sql);

                ResultSet rs = ps.executeQuery();
                while(rs.next())
                    System.out.println(rs.getInt(1) + "--" + rs.getString(2));
                //ConnectionPool.closeConnection(conn);
            } catch (SQLException e) {
                ConnectionPool.closeConnection(conn);
                e.printStackTrace();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

	

	
}
