package com.example.referee.ingredientadd

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.R
import com.example.referee.databinding.ActivityAddIngredientBinding
import java.io.File

class IngredientAddActivity:AppCompatActivity() {
    lateinit var binding: ActivityAddIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ingredient)
        initViews()
    }

    private fun initViews() {
        initUnitRecyclerView()
        initExpirationSpinner()
        initPhoto()
    }

    private fun initPhoto() {
        val cameraActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
           if(result.resultCode == RESULT_OK) {
               val imageBitmap = result.data?.extras?.get("data") as Bitmap
               binding.ivPhoto.setImageBitmap(imageBitmap)
           }
        }

        val galleryActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    binding.ivPhoto.setImageURI(it)
                }
            }
        }
        binding.ivPhoto.setOnClickListener {
            val dialog = AlertDialog.Builder(this).apply {
                setTitle(R.string.ingredient_add_photo_desc)
                setItems(
                    arrayOf(
                        getString(R.string.ingredient_add_photo_camera),
                        getString(R.string.ingredient_add_photo_gallery)
                    )
                ) { _, which ->
                    when(which) {
                        0 -> {
                            val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                                this@IngredientAddActivity,
                                android.Manifest.permission.CAMERA
                            )

                            if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
                                ActivityCompat.requestPermissions(
                                    this@IngredientAddActivity,
                                    arrayOf(android.Manifest.permission.CAMERA),
                                    1000
                                )
                            } else { //권한이 있는 경우
                                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                    takePictureIntent.resolveActivity(packageManager)?.also {
                                        cameraActivityResult.launch(takePictureIntent)
                                    }
                                }
                            }
                        }
                        1 -> {
                            val galleryPermission = android.Manifest.permission.READ_MEDIA_IMAGES
                            val galleryPermissionCheck = ContextCompat.checkSelfPermission(
                                this@IngredientAddActivity,
                                galleryPermission
                            ) != PackageManager.PERMISSION_GRANTED

                            if (galleryPermissionCheck) { // 권한이 없는 경우
                                ActivityCompat.requestPermissions(
                                    this@IngredientAddActivity,
                                    arrayOf(galleryPermission),
                                    1001
                                )
                            } else { //권한이 있는 경우
                               Intent().apply {
                                   type = "image/*"
                                   action = Intent.ACTION_GET_CONTENT
                                   addCategory(Intent.CATEGORY_OPENABLE)
                               }.also { takePictureIntent ->
                                   takePictureIntent.resolveActivity(packageManager)?.also {
                                       galleryActivityResult.launch(takePictureIntent)
                                   }
                                }
                            }
                        }
                    }
                }
                setPositiveButton(R.string.confirm) {dialog,id ->

                }
                setNegativeButton(R.string.cancel) { dialog,id ->

                }
            }
            dialog.create().show()
        }
    }

    private fun initExpirationSpinner() {
        val expirationUnits = resources.getStringArray(R.array.ingredient_expiration_unit)
        with(binding.spExpiration) {
            adapter = ArrayAdapter(
                this@IngredientAddActivity,
                R.layout.item_spinner,
                expirationUnits
            )
        }
    }

    private fun initUnitRecyclerView() {
        val units = resources.getStringArray(R.array.ingredient_unit)
        with(binding.rvUnits) {
            layoutManager =
                LinearLayoutManager(this@IngredientAddActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = IngredientUnitAdapter(this@IngredientAddActivity, units).apply {
                setHasStableIds(true)
            }
            addItemDecoration(object :RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)

                    if (position != units.lastIndex) {
                        outRect.right = 30
                    }
                }
            })
        }
    }
}