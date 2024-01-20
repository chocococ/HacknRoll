package com.example.quizmodoro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizmodoro.databinding.LoadingBinding
import com.example.quizmodoro.databinding.SessionEndMainBinding
import kotlinx.coroutines.selects.select


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
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), selectImages)
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


            for (imageUri in selectedImages) {
                Log.d("UploadActivity", "Selected Image URI: $imageUri")
            }

            loadingSessionBinding = LoadingBinding.inflate(layoutInflater)
            setContentView(loadingSessionBinding.root)
        }
    }


}