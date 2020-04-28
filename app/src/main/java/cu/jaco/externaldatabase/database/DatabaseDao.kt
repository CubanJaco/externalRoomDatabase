package cu.jaco.externaldatabase.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cu.jaco.externaldatabase.database.models.DBTestTable

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: List<DBTestTable>)

    @Query("SELECT * FROM table_test")
    fun getMessages(): LiveData<List<DBTestTable>>

}
