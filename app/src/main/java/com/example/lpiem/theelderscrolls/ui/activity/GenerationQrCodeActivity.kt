package com.example.lpiem.theelderscrolls.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.WriterException
import android.media.MediaScannerConnection
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.manager.PermissionManager
import kotlinx.android.synthetic.main.activity_generation_qrcode.*
import kotlinx.android.synthetic.main.activity_main.*


class GenerationQrCodeActivity: BaseActivity() {

    val QRcodeWidth = 500
    private val IMAGE_DIRECTORY = "/QRcodeDemonuts"
    var bitmap: Bitmap? = null

    companion object {
        const val ExtraCardId = "ExtraCardId"
        fun start(activity: AppCompatActivity, cardId: String) = activity.startActivity(Intent(activity, GenerationQrCodeActivity::class.java).apply {
            putExtra(ExtraCardId, cardId)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generation_qrcode)
        setSupportActionBar(toolbar_qrcode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val idCard = intent.getStringExtra(ExtraCardId)

        try {
            bitmap = encodeAsBitmap(idCard)
            iv_qr_code.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

    fun encodeAsBitmap(str: String): Bitmap? {
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, QRcodeWidth, QRcodeWidth, null)
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }

        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) ContextCompat.getColor(this, R.color.black) else ContextCompat.getColor(this, R.color.white)
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, w, h)
        return bitmap
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}