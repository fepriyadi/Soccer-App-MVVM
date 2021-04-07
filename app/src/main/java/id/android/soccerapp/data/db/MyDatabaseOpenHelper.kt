package id.android.soccerapp.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by developer on 9/24/18.
 */
class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(FavoriteEvent.TABLE_FAVORITE, true,
        FavoriteEvent.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
        FavoriteEvent.EVENT_ID to TEXT + UNIQUE,
        FavoriteEvent.EVENT_DATE to TEXT,
        FavoriteEvent.HOME_TEAM to TEXT,
        FavoriteEvent.HOME_SCORE to TEXT,
        FavoriteEvent.AWAY_TEAM to TEXT,
        FavoriteEvent.AWAY_SCORE to TEXT)

        db.createTable(FavoriteTeam.TABLE_FAVORITE, true,
                FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
                FavoriteTeam.TEAM_NAME to TEXT,
                FavoriteTeam.TEAM_BADGE to TEXT,
                FavoriteTeam.TEAM_YEAR to TEXT,
                FavoriteTeam.TEAM_STADIUM to TEXT,
                FavoriteTeam.TEAM_OVERVIEW to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteEvent.TABLE_FAVORITE, true)
        db.dropTable(FavoriteTeam.TABLE_FAVORITE, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)