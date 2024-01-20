package com.example.quizmodoro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmodoro.databinding.LoadingBinding
import com.example.quizmodoro.databinding.SessionEndMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit


class UploadActivity : AppCompatActivity() {
    private lateinit var endSessionBinding: SessionEndMainBinding
    private lateinit var loadingSessionBinding: LoadingBinding
    private val selectImages = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        endSessionBinding = SessionEndMainBinding.inflate(layoutInflater)
        setContentView(endSessionBinding.root)

        endSessionBinding.uploadButton.setOnClickListener {
            imageChooser()
        }
    }

    private fun imageChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), selectImages)
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".png", context.cacheDir).apply {
            deleteOnExit()
        }
        val outputStream = FileOutputStream(tempFile)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        return tempFile
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == selectImages) {
            val selectedImages = mutableListOf<Uri>()

            val clipData = data?.clipData
            if (clipData != null) {
                if (clipData.itemCount > 16) {
                    Toast.makeText(this, "You can select a maximum of 16 images", Toast.LENGTH_LONG).show()
                    for (i in 0 until 16) {
                        val imageUri = clipData.getItemAt(i).uri
                        selectedImages.add(imageUri)
                    }
                } else {
                    for (i in 0 until clipData.itemCount) {
                        val imageUri = clipData.getItemAt(i).uri
                        selectedImages.add(imageUri)
                    }
                }
            } else {
                data?.data?.let { imageUri ->
                    selectedImages.add(imageUri)
                }
            }

            val builder = OkHttpClient.Builder()
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)
            builder.writeTimeout(60, TimeUnit.SECONDS)
            val client = builder.build()

            for (imageUri in selectedImages) {
                Log.d("UploadActivity", "Selected Image URI: $imageUri")

                val imageFile = uriToFile(this, imageUri)

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.name, RequestBody.create(MediaType.parse("image/png"), imageFile)) // add here
                    .build()
                val request = Request.Builder()
                    .url("https://quizmodoro.onrender.com/")
                    .post(requestBody)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Unsuccessful")
                        println(call)
                        println(e)
                    }

                    override fun onResponse(
                        call: Call, response: Response
                    ) {
                        // handle response
                        println(response.body()?.string())
                    }
                })
            }

            loadingSessionBinding = LoadingBinding.inflate(layoutInflater)
            setContentView(loadingSessionBinding.root)
        }
    }


}