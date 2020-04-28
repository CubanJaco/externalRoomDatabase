package cu.jaco.externaldatabase

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import cu.jaco.externaldatabase.database.AppDatabase
import cu.jaco.externaldatabase.database.DatabaseDao
import cu.jaco.externaldatabase.database.models.DBTestTable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var dao: DatabaseDao
    private var text = ""

    companion object {

        const val ASK_PERMISSION = 21445

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (checkPermission())
            readDatabase()

        fab.setOnClickListener {
            writeDatabase()
        }
    }

    private fun checkPermission(): Boolean {

        val permissions = ArrayList<String>()

        // Chequear si hay permiso de lectura de memoria
        if (!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        // Chequear si hay permiso de escritura de memoria
        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissions.isNotEmpty())
        //no hay permiso, pero se puede solicitar
            ActivityCompat.requestPermissions(
                this, permissions.toTypedArray(),
                ASK_PERMISSION
            )

        return permissions.isEmpty()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        var hasPermission = true

        if (requestCode == ASK_PERMISSION) {

            for (result in grantResults) {

                if (result != PackageManager.PERMISSION_GRANTED) {
                    hasPermission = false
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_SHORT).show()
                    break
                }

            }

            if (hasPermission)
                readDatabase()

        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun readDatabase() {

        if (!File(AppDatabase.DB_NAME).exists()) {
            Toast.makeText(this, R.string.bad_file, Toast.LENGTH_SHORT).show()
            return
        }

        dao = AppDatabase.getDatabase(this).dao()

        dao.getMessages().observe(this) {

            text = ""
            for (message in it) {

                text += "- ${message.message}\n\n"

            }

            messages.text = text
        }

    }

    private fun writeDatabase() {

        if (!::dao.isInitialized)
            return

        dao.insert(
            arrayListOf(
                DBTestTable(
                    id = Random.nextInt(),
                    message = "Un mensaje!!!"
                )
            )
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

}
