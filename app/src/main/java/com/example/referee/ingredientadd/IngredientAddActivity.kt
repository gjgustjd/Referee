package com.example.referee.ingredientadd

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.referee.R
import com.example.referee.common.base.BaseActivity
import com.example.referee.common.Const
import com.example.referee.databinding.ActivityAddIngredientBinding
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredientadd.model.IngredientExpirationUnit

class IngredientAddActivity : BaseActivity() {
    private val viewModel:IngredientAddViewModel by viewModels()
    lateinit var binding: ActivityAddIngredientBinding

    private val unitsAdapter by lazy {
        IngredientUnitAdapter(this@IngredientAddActivity, resources.getStringArray(R.array.ingredient_unit)).apply {
            setHasStableIds(true)
        }
    }
    private val categoriesAdapter by lazy {
        IngredientCategoryAdapter(
            this@IngredientAddActivity,
            IngredientCategoryType.values()
        ).apply {
            setHasStableIds(true)
        }
    }

    private fun getDecoration(lastIndex: Int) = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)

            if (position != lastIndex) {
                outRect.right = 30
            }
        }
    }

    private val cameraActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.getParcelable("data", Bitmap::class.java)
            binding.ivPhoto.setImageBitmap(imageBitmap)
        }
    }
    private val galleryActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                binding.ivPhoto.setImageURI(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ingredient)
        initViews()
    }

    private fun initViews() {
        initUnitRecyclerView()
        initExpirationSpinner()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnConfirm.setOnClickListener {
                val name = etIngredientName.text.toString()
                val unit = unitsAdapter.getSelectedItemString()
                val photoBitmap = ivPhoto.drawable.toBitmap()
                val expiration = IngredientExpirationUnit.fromString(spExpiration.selectedItem as String)

                if(name.isEmpty()) {
                    showToast(getString(R.string.ingredient_add_please_input_name))
                } else {
                    viewModel.insertIngredient(name, photoBitmap, unit, expiration)
                }
            }
            ivPhoto.setOnClickListener {
                AlertDialog.Builder(this@IngredientAddActivity).apply {
                    setTitle(R.string.ingredient_add_photo_desc)
                    setNegativeButton(R.string.cancel, null)
                    setItems(
                        arrayOf(
                            getString(R.string.ingredient_add_photo_camera),
                            getString(R.string.ingredient_add_photo_gallery)
                        )
                    ) { _, which ->
                        when (which) {
                            0 -> {
                                checkPermissionAndRequestForActivityResult(
                                    android.Manifest.permission.CAMERA,
                                    Const.REQUEST_CAMERA_CODE
                                ) {
                                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                        takePictureIntent.resolveActivity(packageManager)?.also {
                                            cameraActivityResult.launch(takePictureIntent)
                                        }
                                    }
                                }
                            }

                            1 -> {
                                checkPermissionAndRequestForActivityResult(
                                    android.Manifest.permission.READ_MEDIA_IMAGES,
                                    Const.REQUEST_GALLERY_CODE
                                ) {
                                    Intent().apply {
                                        type = "image/*"
                                        action = Intent.ACTION_GET_CONTENT
                                        addCategory(Intent.CATEGORY_OPENABLE)
                                    }.also { pickPhotoIntent ->
                                        pickPhotoIntent.resolveActivity(packageManager)?.also {
                                            galleryActivityResult.launch(pickPhotoIntent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                    .create()
                    .show()
            }
        }
        viewModel.event.observe(this@IngredientAddActivity) {
            when (it.getContentIfNotHandled()) {
                IngredientAddEvent.InsertSuccess -> {
                    Toast.makeText(
                        this@IngredientAddActivity,
                        getString(R.string.ingredient_add_succeed_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

                IngredientAddEvent.InsertFailed -> Toast.makeText(
                    this@IngredientAddActivity,
                    getString(R.string.ingredient_add_failed_toast),
                    Toast.LENGTH_SHORT
                ).show()

                else -> Unit
            }
        }
    }

    private fun checkPermissionAndRequestForActivityResult(
        permission: String,
        requestCode: Int,
        onGranted: () -> Unit
    ) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@IngredientAddActivity,
            permission
        )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
            ActivityCompat.requestPermissions(
                this@IngredientAddActivity,
                arrayOf(permission),
                requestCode
            )
        } else { //권한이 있는 경우
            onGranted()
        }
    }

    private fun initExpirationSpinner() {
        val expirationUnits = IngredientExpirationUnit.values().map { it.unitName }
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
            adapter = unitsAdapter
            addItemDecoration(getDecoration(units.lastIndex))
        }

        with(binding.rvCategories) {
            layoutManager =
                LinearLayoutManager(this@IngredientAddActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
            addItemDecoration(getDecoration(IngredientCategoryType.values().lastIndex))
        }
    }
}