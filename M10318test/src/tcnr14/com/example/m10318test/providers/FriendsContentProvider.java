package tcnr14.com.example.m10318test.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FriendsContentProvider extends ContentProvider {
	private static final String DB_FILE = "members.db";
	private static final String DB_TABLE = "members";

	private static final int DB_TABLE_FRIENDS = 1;
	private static final int URI_ROOT = 0;

	private static final String AUTHORITY = "tcnr14.com.example.m10318.providers.FriendsContentProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + DB_TABLE);
	private static final UriMatcher sUriMatcher = new UriMatcher(URI_ROOT);
	static {
		sUriMatcher.addURI(AUTHORITY, DB_TABLE, DB_TABLE_FRIENDS);
	}
	private SQLiteDatabase mFriendDb;

	@Override
	public boolean onCreate() {
		FriendDbOpenHelper friendDbOpenHelper = new FriendDbOpenHelper(
				getContext(), DB_FILE, null, 1);

		mFriendDb = friendDbOpenHelper.getWritableDatabase();

		// 檢查資料表是否已經存在，如果不存在，就建立一個。
		Cursor cursor = mFriendDb.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ DB_TABLE + "'", null);

		if (cursor != null) {
			if (cursor.getCount() == 0) // 沒有資料表，要建立一個資料表。
				mFriendDb.execSQL("CREATE TABLE " + DB_TABLE + " ("
						+ "id INTEGER PRIMARY KEY," + "username TEXT NOT NULL,"
						+ "password TEXT NOT NULL," + "email TEXT NOT NULL,"
						+ "gup TEXT NOT NULL ," + "address2 TEXT ,"
						+ "address1 TEXT);");
			cursor.close();
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (sUriMatcher.match(uri) != DB_TABLE_FRIENDS) {
			throw new IllegalArgumentException("嚴重錯誤 未知URI" + uri);
		}
		Cursor c = mFriendDb.query(true, DB_TABLE, projection, selection, null,
				null, null, null, null);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (sUriMatcher.match(uri) != DB_TABLE_FRIENDS) {
			throw new IllegalArgumentException("嚴重錯誤 未知URI" + uri);
		}

		long rowId = mFriendDb.insert(DB_TABLE, null, values);
		if (rowId > 0) {
			// 在已有的 Uri的後面追加ID數據
			Uri insertedRowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			;
			// 通知數據已經改變
			getContext().getContentResolver()
					.notifyChange(insertedRowUri, null);
			return insertedRowUri;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (sUriMatcher.match(uri) != DB_TABLE_FRIENDS) {
			throw new IllegalArgumentException("嚴重錯誤 未知URI" + uri);
		}
		int rowsAffected = mFriendDb.delete(DB_TABLE, selection, null);
		return rowsAffected;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (sUriMatcher.match(uri) != DB_TABLE_FRIENDS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		int rowsAffected = mFriendDb.update(DB_TABLE, values, selection, null);
		return rowsAffected;
	}
}
