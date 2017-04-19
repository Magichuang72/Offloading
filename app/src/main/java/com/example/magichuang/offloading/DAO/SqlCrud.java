package com.example.magichuang.offloading.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by magichuang on 17-4-18.
 */
public class SqlCrud {
    private final SqlOpenHelper sqlOpenHelper;
    Context context;

    public SqlCrud(Context context) {
        this.context = context;
        sqlOpenHelper = new SqlOpenHelper(context);
    }

    //增加
    public void add(String name, String id) {
        SQLiteDatabase database = sqlOpenHelper.getWritableDatabase();
        //先判断数据库是否可用
        if (database.isOpen()) {
            //执行插入操作
            //database.execSQL("insert into person (name,phone) values('"+name+"','"+phone+"')");

            //推荐如下写法
            database.execSQL("insert into info (name,id) values(?,?)", new Object[]{name, id});
            database.close();
        }
    }

    //查找
    public boolean find(String id) {
        boolean result = false;
        SQLiteDatabase database = sqlOpenHelper.getReadableDatabase();
        if (database.isOpen()) {
            //database.execSQL("select * from phone='"+phone+"'");

            Cursor cursor = database.rawQuery("select * from info where id=?", new String[]{id});
            if (cursor.moveToFirst()) {//游标是否移动到下一行,如果是,那说明有数据返回
                //   Log.d(tag, "count:" + cursor.getColumnCount());
                int nameIndex = cursor.getColumnIndex("name");
                // Log.d(tag, "name:" + cursor.getString(nameIndex));
                cursor.close();
                result = true;
            } else {
                result = false;

            }
            database.close();
        }
        return result;
    }

    //删除一条数据
    public void delete(String name) {
        SQLiteDatabase database = sqlOpenHelper.getWritableDatabase();
        if (database.isOpen()) {
            database.execSQL("delete from info where name=?", new Object[]{name});
        }
        database.close();
    }

    //更新一条数据
//    public void updatePerson(String phone, String newName, String newPhone) {
//        SQLiteDatabase database = myDBHelper.getWritableDatabase();
//        if (database.isOpen()) {
//            database.execSQL("update person set name=?,phone=? where phone=?", new Object[]{newName, newPhone, phone});
//        }
//        database.close();
//    }
}
