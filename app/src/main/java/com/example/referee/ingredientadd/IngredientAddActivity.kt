package com.example.referee.ingredientadd

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.transition.Transition
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.base.BaseActivity
import com.example.referee.databinding.ActivityAddIngredientBinding
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientExpirationUnit
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class IngredientAddActivity :
    BaseActivity<ActivityAddIngredientBinding>(R.layout.activity_add_ingredient) {

    companion object {
        const val EXTRA_IS_EDIT_MODE = "EXTRA_IS_EDIT_MODE"
        const val EXTRA_INGREDIENT = "EXTRA_INGREDIENT"

        fun newIntent(context: Context, isEdit: Boolean, item: IngredientEntity): Intent {
            return Intent(context, IngredientAddActivity::class.java).apply {
                putExtra(EXTRA_IS_EDIT_MODE, isEdit)
                putExtra(EXTRA_INGREDIENT, item)
            }
        }
    }

    private val viewModel: IngredientAddViewModel by viewModels()

    private val unitsAdapter by lazy {
        val unitArray = resources.getStringArray(R.array.ingredient_unit)
        val selection = unitArray.indexOfFirst { it == editingIngredient?.unit }.coerceAtLeast(0)
        IngredientUnitAdapter(
            binding.rvUnits,
            unitArray,
            selection
        ).apply {
            setHasStableIds(true)
        }
    }
    private val categoriesAdapter by lazy {
        binding.category = IngredientCategoryType.MEAT
        IngredientCategoryAdapter(
            binding.rvCategories,
            IngredientCategoryType.values(),
            editingIngredient?.category ?: 0
        ) { type ->
            viewModel.preSavedImageName ?: run {
                binding.category = type
            }
        }.apply {
            setHasStableIds(true)
        }
    }

    private var editingIngredient: IngredientEntity? = null
    private var isEditing = false

    private fun getDecoration(lastIndex: Int): ItemDecoration {
        val margin = CommonUtil.pxToDp(
            this,
            resources.getDimension(R.dimen.decorator_default_margin).toInt()
        )
        return CommonRecyclerViewDecoration(rightMargin = margin, exceptIndex = lastIndex)
    }

    private val cameraActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.getParcelable("data", Bitmap::class.java)
                viewModel.deletePreSavedImage()
                binding.bitmap = imageBitmap
                viewModel.saveImage(imageBitmap)
            }
        }
    private val galleryActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    binding.ivPhoto.setPadding(0)
                    binding.ivPhoto.setImageURI(it)
                    viewModel.deletePreSavedImage()
                    val photoBitmap = binding.ivPhoto.drawable.toBitmap()
                    viewModel.saveImage(photoBitmap)
                }
            }
        }

    private val launchRequestCameraPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        cameraActivityResult.launch(takePictureIntent)
                    }
                }
            } else if(!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                showToast(getString(R.string.ingredient_add_please_grant_permission_toast))
            }
        }

    private val launchRequestGalleryPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    addCategory(Intent.CATEGORY_OPENABLE)
                }.also { pickPhotoIntent ->
                    pickPhotoIntent.resolveActivity(packageManager)?.also {
                        galleryActivityResult.launch(pickPhotoIntent)
                    }
                }
            } else if(!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)) {
                showToast(getString(R.string.ingredient_add_please_grant_permission_toast))
            }
        }

    override fun initViews() {
        initExtra()
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition?) {
                // 전환 애니메이션이 시작될 때 호출됩니다.
            }

            override fun onTransitionEnd(transition: Transition?) {
                binding.etIngredientName.requestFocus()
                // 전환 애니메이션이 끝날 때 호출됩니다. 여기서 필요한 작업을 수행하세요.
            }

            override fun onTransitionCancel(transition: Transition?) {
                // 전환 애니메이션이 취소될 때 호출됩니다.
            }

            override fun onTransitionPause(transition: Transition?) {
                // 전환 애니메이션이 일시 중지될 때 호출됩니다.
            }

            override fun onTransitionResume(transition: Transition?) {
                // 전환 애니메이션이 재개될 때 호출됩니다.
            }
        })
        initUnitRecyclerView()
        initExpirationSpinner()
        initItemInfo()
    }

    override fun initListeners() {
        super.initListeners()

        with(binding) {
            btnConfirm.clicks()
                .throttleFirst(
                    resources.getInteger(R.integer.click_throttle_default_duration).toLong(),
                    TimeUnit.MILLISECONDS
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(isEditing) {
                        editingIngredient?.let {
                            val itemId = it.id
                            val name = binding.etIngredientName.text.toString()
                            val unit = unitsAdapter.getSelectedItemString()
                            val expiration =
                                IngredientExpirationUnit.fromString(binding.spExpiration.selectedItem as String)
                            val photoName = editingIngredient?.photoName
                            val category = categoriesAdapter.getSelectedItem()
                            viewModel.editIngredient(itemId,name, unit, photoName, expiration, category)
                        }
                    } else {
                        viewModel.isExistSameNameIngredient(etIngredientName.text.toString())
                    }
                }.apply {
                    compositeDisposable.add(this)
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
                                    launchRequestCameraPermissionCallback
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
                                    launchRequestGalleryPermissionCallback
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
                    hideLoading()
                    finish()
                }

                IngredientAddEvent.InsertFailed -> {
                    hideLoading()
                    Toast.makeText(
                        this@IngredientAddActivity,
                        getString(R.string.ingredient_add_failed_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is IngredientAddEvent.IsThereIngredient -> {
                    val event = it.peekContent() as IngredientAddEvent.IsThereIngredient

                    if (event.value) {
                        showToast(getString(R.string.ingredient_add_there_is_same_item_toast))
                    } else {
                        val name = binding.etIngredientName.text.toString()
                        val unit = unitsAdapter.getSelectedItemString()
                        val expiration =
                            IngredientExpirationUnit.fromString(binding.spExpiration.selectedItem as String)

                        if (name.isEmpty()) {
                            showToast(getString(R.string.ingredient_add_please_input_name))
                        } else {
                            viewModel.insertIngredient(
                                name,
                                unit,
                                expiration,
                                categoriesAdapter.getSelectedItem()
                            ).let { job ->
                                showLoading(job::cancel)
                            }
                        }
                    }
                }

                is IngredientAddEvent.IngredientBitmap -> {
                    val event = it.peekContent() as IngredientAddEvent.IngredientBitmap

                    binding.ivPhoto.setImageBitmap(event.bitmap)
                }

                is IngredientAddEvent.UpdateSuccess -> {
                    finish()
                    showToast(getString(R.string.ingredient_update_succeed_toast))
                }

                is IngredientAddEvent.UpdateFailed -> {
                    finish()
                    showToast(getString(R.string.ingredient_update_failed_toast))
                }

                else -> Unit
            }
        }
    }

    override fun initOnBackPressedDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.deletePreSavedImage()
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(callback)
    }

    private fun initExtra() {
        isEditing = intent.getBooleanExtra(EXTRA_IS_EDIT_MODE, false)
        editingIngredient =
            intent.getSerializableExtra(EXTRA_INGREDIENT, IngredientEntity::class.java)
    }

    private fun initItemInfo() {
       if(isEditing) {
           title = getString(R.string.ingredient_edit_title)
           binding.etIngredientName.setText(editingIngredient?.name ?: "")
           editingIngredient?.photoName?.let { photo ->
               viewModel.getImageBitmap(photo)
           }
           val expPosition = IngredientExpirationUnit.values()
               .indexOfFirst { it.days == editingIngredient?.expiration }.coerceAtLeast(0)
           binding.spExpiration.setSelection(expPosition)
           binding.rvUnits
       }
    }

    private fun checkPermissionAndRequestForActivityResult(
        permission: String,
        callback: ActivityResultLauncher<String>,
        onGranted: () -> Unit
    ) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@IngredientAddActivity,
            permission
        )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
            callback.launch(
                permission
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
                LinearLayoutManager(
                    this@IngredientAddActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = unitsAdapter
            addItemDecoration(getDecoration(units.lastIndex))
        }

        with(binding.rvCategories) {
            layoutManager =
                LinearLayoutManager(
                    this@IngredientAddActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoriesAdapter
            addItemDecoration(getDecoration(IngredientCategoryType.values().lastIndex))
        }
    }
}