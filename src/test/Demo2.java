package test;


import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.ConnectionPool;
import core.MysqlQuery;
import entity.Tuser;
import entity.User;

public class Demo2 {
	
	public static void main(String[] args) {
		
		ConnectionPool cp = ConnectionPool.getInstance();
		
		Connection conn = cp.getConnection();
		//System.out.println(conn);
		
		User user = new User();
		user.setId(1);
		user.setName("\"g\"");
		
		/*T1 t1 = new T1();
		t1.setId(1);
		t1.setName("\"e\"");*/
		
		MysqlQuery mysqlQuery = new MysqlQuery(conn);
		
		//mysqlQuery.insert(t1);
		
		// test insert 
		//mysqlQuery.insert(user);
		
		// test delete
		//mysqlQuery.delete(user.getClass(), 1);
		//mysqlQuery.delete(user);
		
		// test update
		//user.setId(2);
		//mysqlQuery.update(user, 1);
		
		// test single select
		User user2 = (User) mysqlQuery.querySingleColumn(user.getClass(), 2);
		System.out.println(user2.getId());
		
		// test multi query
		Map<String, Object> map = new HashMap<>();
		map.put("name", "\"g\"");
		List<Object> list = mysqlQuery.queryMultiColumn(user.getClass(), map);
		System.out.println("list size : " + list.size());
		
		User user3 = (User) list.get(0);
		System.out.println(user3.getName());
		
		Tuser tuser = new Tuser();
		
		tuser.setId(1);
		tuser.setName("\"a\"");
		
		mysqlQuery.insert(tuser);
		
	}

}
