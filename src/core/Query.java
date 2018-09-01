package core;

import java.util.List;
import java.util.Map;

public interface Query {

	// persistence a object
    int insert(Object object);
    
	// delete with primaryKey
    void delete(Class<?> cls, int primaryKey);
    
    void delete(Object object);
    
    // alter table
    
    void update(Object object, Object primaryKey);

    // query
    Object querySingleColumn(Class<?> cls, Object primaryKey);
    
    List<Object> queryMultiColumn(Class<?> cls, Map<String, Object> map);

    // execute sql statement
    int executeDML(String sql);
    

}
