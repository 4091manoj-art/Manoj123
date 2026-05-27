package com.example.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_sessions")
data class UserSession(
    @PrimaryKey val id: Int = 1,
    val isLoggedIn: Boolean,
    val email: String,
    val name: String,
    val isPremium: Boolean = false,
    val remainingEdits: Int = 5 // Premium gets unlimited (or 9999)
)

@Entity(tableName = "recent_edits")
data class RecentEdit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val featureName: String,
    val originalImage: String, // Base64 or Preset resource name or Local URI
    val processedImage: String, // Base64 or Preset resource name or Local URI
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface PhotoDao {
    @Query("SELECT * FROM user_sessions WHERE id = 1 LIMIT 1")
    fun getUserSession(): Flow<UserSession?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserSession(session: UserSession)

    @Query("DELETE FROM user_sessions")
    suspend fun clearUserSession()

    @Query("SELECT * FROM recent_edits ORDER BY timestamp DESC")
    fun getAllRecentEdits(): Flow<List<RecentEdit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentEdit(edit: RecentEdit)

    @Query("DELETE FROM recent_edits")
    suspend fun clearHistory()

    @Query("DELETE FROM recent_edits WHERE id = :id")
    suspend fun deleteEditById(id: Int)
}

@Database(entities = [UserSession::class, RecentEdit::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "photo_editor_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
