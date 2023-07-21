package com.example.bookmarket.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;

public class JdbcUtils {
    private static final String url = "jdbc:mysql://localhost:3306/database_achieve_class_design?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
    private static final String user = "root";
    private static final String password = "lsy12345";
    private static Connection connection = null;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static synchronized Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
    public static int add(String sql, Object... var) {
        return modify(sql, var);
    }//添加
    public static int delete(String sql, Object... var) {
        return modify(sql, var);
    }//删除
    public static int update(String sql, Object... var) {
        return modify(sql, var);
    }//修改
    public static <T> ArrayList<T> search(String sql, Class<T> cls, Object... var) {
        ArrayList<T> list = new ArrayList<>();
        try {
            PreparedStatement prep = getConnection().prepareStatement(sql);
            ArrayList<Field> fields = new ArrayList<>();
            for (int i = 0; i < var.length; i++) {
                prep.setObject(i + 1, var[i]);
            }
            ResultSet resultSet = prep.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            //根据sql执行结果，获取cls当中属性字段field
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //System.out.println("ColumnLabel:"+metaData.getColumnLabel(i)+ " ColumnName:"+metaData.getColumnName(i));
                try {
                    //System.out.println(cls.getDeclaredField(metaData.getColumnLabel(i).toLowerCase()));
                    fields.add(cls.getDeclaredField(metaData.getColumnLabel(i).toLowerCase()));
                } catch (NoSuchFieldException e) {
                    System.out.println(cls + "不存在" + metaData.getColumnLabel(i) + "字段");
                }
            }
            while (resultSet.next()) {
                T instance = null;
                //基本类型处理
                if (cls.isAssignableFrom(Integer.class)
                        || cls.isAssignableFrom(Long.class)
                        || cls.isAssignableFrom(Double.class)
                        || cls.isAssignableFrom(String.class)) {
                    instance = (T) resultSet.getObject(1);
                } else if (cls.isAssignableFrom(Boolean.class)) {
                    long v = resultSet.getLong(1);
                    Boolean val = v > 0;
                    instance = (T) val;
                } else {
                    instance = cls.newInstance();//创建cls对应的实例
                    for (Field field : fields) {
                        field.setAccessible(true);//属性字段为private，需设置可见性为true才能调用field的set方法
                        field.set(instance, resultSet.getObject(field.getName()));//设置实例属性字段的值
                    }
                }
                list.add(instance);
            }
            prep.close();
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return list;
    }//执行查询
    public static <T> T searchOne(String sql, Class<T> cls, Object... var) {
        ArrayList<T> list=search(sql, cls, var);
        if(list.size()==1){
            return list.get(0);
        }
        return null;
    }//执行查询（查询一条）
    /**
     * 执行存储过程（不带结果集）
     * @param sql SQL语句
     * @param outType OUT参数类型（使用数组方式给出）
     * @param var IN参数
     * @return 存储过程执行后OUT参数返回值
     * */
    public static Object[] executeProcedure(String sql, int[] outType, Object... var) {
        Object[] outPara = new Object[outType.length];
        try {
            CallableStatement cs = getConnection().prepareCall(sql);
            for (int i = 0; i < var.length; i++) {
                cs.setObject(i + 1, var[i]);//填入IN参数
            }
            for (int i = 0; i < outType.length; i++) {
                cs.registerOutParameter(var.length + i + 1, outType[i]);//填入OUT参数
            }
            cs.execute();
            for (int i = 0; i < outType.length; i++) {
                outPara[i] = cs.getObject(var.length + i + 1);//输出OUT参数
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return outPara;
    }
    /**
     * 执行存储过程（带结果集，不带OUT参数）
     * @param sql SQL语句
     * @param classList 结果集对应实体类（使用数组方式给出）
     * @param var IN参数
     * @return 存储过程执行后结果集信息（数组当中每一条为对应结果集当中的数据，类型为ArrayList，和classList一一对应）
     * */
    public static Object[] executeProcedureWithResultSet(String sql, Class<?>[] classList, Object... var) {
        return executeProcedureWithResultSet(sql, classList, new int[]{}, new Object[]{}, var);
    }
    /**
     * 执行存储过程（带结果集，带带OUT参数）
     * @param sql SQL语句
     * @param classList 结果集对应实体类（使用数组方式给出）
     * @param outType OUT参数类型（使用数组方式给出）
     * @param outPara 存储过程执行后OUT参数返回值（Object数组，使用前创建好并传入）
     * @param var IN参数
     * @return 存储过程执行后结果集信息（数组当中每一条为对应结果集当中的数据，类型为ArrayList，和classList一一对应）
     * */
    public static Object[] executeProcedureWithResultSet(String sql, Class<?>[] classList, int[] outType, Object[] outPara, Object... var) {
        Object[] result = new Object[classList.length];
        try {
            CallableStatement cs = getConnection().prepareCall(sql);
            for (int i = 0; i < var.length; i++) {
                cs.setObject(i + 1, var[i]);//填入IN参数
            }
            for (int i = 0; i < outType.length; i++) {
                cs.registerOutParameter(var.length + i + 1, outType[i]);//填入OUT参数类型
            }
            cs.execute();//执行存储过程
            for (int i = 0; i < classList.length; i++) {
                ResultSet resultSet = cs.getResultSet();//获取结果集
                ResultSetMetaData metaData = resultSet.getMetaData();//结果集元数据，可以得到列名
                ArrayList<Field> fields = new ArrayList<>();//对应Class当中的字段
                ArrayList<Object> list = new ArrayList<>();//保存结果
                Class<?> clazz = classList[i];//获取反射创建对象的Class
                for (int j = 1; j <= metaData.getColumnCount(); j++) {
                    try {
                        fields.add(clazz.getDeclaredField(metaData.getColumnLabel(j).toLowerCase()));//获取对应字段
                    } catch (NoSuchFieldException e) {
                        System.out.println(clazz + "不存在" + metaData.getColumnLabel(j) + "字段");
                    }
                }
                while (resultSet.next()) {
                    Object obj = clazz.getDeclaredConstructor().newInstance();//创建对象
                    for (Field field : fields) {
                        field.setAccessible(true);//属性字段为private，需设置可见性为true才能调用field的set方法
                        field.set(obj, resultSet.getObject(field.getName()));//填充字段值
                    }
                    list.add(obj);
                }
                result[i] = list;
                cs.getMoreResults();//获取更多结果集
            }
            for (int i = 0; i < outType.length; i++) {
                outPara[i] = cs.getObject(var.length + i + 1);//输出OUT参数
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    private static int modify(String sql, Object... var) {
        int modify = 0;
        try {
            PreparedStatement prep = getConnection().prepareStatement(sql);
            for (int i = 0; i < var.length; i++) {
                prep.setObject(i + 1, var[i]);
            }
            modify = prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modify;
    }
}
