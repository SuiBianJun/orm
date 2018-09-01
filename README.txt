ORM框架:对象关系映射（对象与SQL）

功能:
    1、添加数据: insert(Object),把对象对应到sql语句插入数据库
    2、删除数据: delete(Object, primaryKey),根据对象与主键删除数据
    3、修改数据: update(Object, primaryKey),根据对象与主键修改表数据
    4、查询: query(Object, primaryKey),单结果与多结果查询
    5、可配置类与表之间的简单映射
    6、实现了简单的连接池和单例连接
    7、通过表结构生成对应的类文件
    
核心类:
package core:
    DBManager: 数据库连接配置，生成连接对象
    ConnectionPool: 简单的连接池
    Query: 操作接口
    MysqlQuery: 操作接口的实现
    TableContext: 获取表结构
    EntityMapping: 类与表的映射
    
package javabean:
数据库表和字段信息

package po:
根据数据库表生成的类文件

package utils:
类文件生成和反射辅助类

    
说明：
实现方式比较简单，代码粗糙(很多地方没有条件的判断)，能力有限:). 大半年前写的，现在把功能全部修补好，并调试出来。
如何获取并存储数据库中表的信息，那部分不熟，参考了别人的代码。并且还要做表字段类型和java数据类型的转换。
test包下有测试代码



