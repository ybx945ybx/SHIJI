package cn.yiya.shiji.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author chenjian
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String mDatabasename = "shiji";
	private static CursorFactory mFactory = null;
	private static final int mVersion = 1;
	public static final String TABLE_NAME = "downloadinfo"; //文件下载信息数据表名称

	public SQLiteHelper(Context context) {
		super(context, mDatabasename, mFactory, mVersion);
	}

	public SQLiteHelper(Context context, String name, CursorFactory factory,
						int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		//创建文件下载信息数据表
		String downloadsql = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" ("
                + "id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "userID VARCHAR, "
                + "taskID VARCHAR, " 
                + "url VARCHAR, " 
                + "filePath VARCHAR, " 
                + "fileName VARCHAR, " 
                + "fileSize VARCHAR, " 
                + "downLoadSize VARCHAR, "
                + "countryID VARCHAR, "
                + "cityID VARCHAR, "
                + "notUnZip VARCHAR, "
                + "download VARCHAR "
                + ")";
        db.execSQL(downloadsql);
        

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
