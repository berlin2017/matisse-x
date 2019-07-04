package cn.lonsun.matissesample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.Glide4Engine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUESTCODE = 1
    private val REQUESTCODE_PERMISSION = 1
    private val permissions =
        listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions.toTypedArray(), REQUESTCODE_PERMISSION)
                }
            } else {
                selectPic()
            }
        }
    }

    private fun selectPic() {
        Matisse.from(this).choose(MimeType.ofImage())
            .imageEngine(Glide4Engine())
            .maxSelectable(9)
            .forResult(REQUESTCODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUESTCODE == requestCode && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainPathResult(data)
            tv.text = result?.toString() ?: ""
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTCODE_PERMISSION && grantResults.filter { it == PackageManager.PERMISSION_GRANTED }.size == permissions.size) {
            selectPic()
        }
    }
}
