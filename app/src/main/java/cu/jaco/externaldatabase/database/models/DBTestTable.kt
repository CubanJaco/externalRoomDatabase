package cu.jaco.externaldatabase.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_test")
data class DBTestTable(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "message") var message: String
)