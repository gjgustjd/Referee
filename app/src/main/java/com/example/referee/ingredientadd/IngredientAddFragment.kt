package com.example.referee.ingredientadd

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Transition
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.setPadding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.referee.R
import com.example.referee.common.CommonRecyclerViewDecoration
import com.example.referee.common.CommonUtil
import com.example.referee.common.base.BaseActivity
import com.example.referee.common.base.BaseFragment
import com.example.referee.databinding.ActivityAddIngredientBinding
import com.example.referee.ingredientadd.model.IngredientCategoryType
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientExpirationUnit
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class IngredientAddFragment :
    BaseFragment<ActivityAddIngredientBinding>(R.layout.activity_add_ingredient) {

    companion object {
        const val EXTRA_IS_EDIT_MODE = "EXTRA_IS_EDIT_MODE"
        const val EXTRA_INGREDIENT = "EXTRA_INGREDIENT"

        fun newIntent(isEdit: Boolean, item: IngredientEntity): IngredientAddFragment {
            return IngredientAddFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_EDIT_MODE, isEdit)
                    putSerializable(EXTRA_INGREDIENT, item)
                }
            }
        }
    }

    private val viewModel: IngredientAddViewModel by activityViewModels()

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

    private val transitionAnimListener by lazy {
        object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition?) = Unit
            override fun onTransitionCancel(transition: Transition?) = Unit
            override fun onTransitionPause(transition: Transition?) = Unit
            override fun onTransitionResume(transition: Transition?) = Unit

            override fun onTransitionEnd(transition: Transition?) {
                with(binding.etIngredientName) {
                    requestFocus()
                    setSelection(text.length)
                    val inputMethodManager =
                        getSystemService(requireContext(), InputMethodManager::class.java)
                    inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
    }

    private var editingIngredient: IngredientEntity? = null
    private var isEditing = false

    private fun getDecoration(lastIndex: Int): ItemDecoration {
        val margin = CommonUtil.pxToDp(
            requireContext(),
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
                    takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
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
                    pickPhotoIntent.resolveActivity(requireActivity().packageManager)?.also {
                        galleryActivityResult.launch(pickPhotoIntent)
                    }
                }
            } else if(!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)) {
                showToast(getString(R.string.ingredient_add_please_grant_permission_toast))
            }
        }

    override fun initViews() {
        initExtra()
        initUnitRecyclerView()
        initExpirationSpinner()
        initItemInfo()
    }

    override fun initListeners() {
        activity?.window?.sharedElementEnterTransition?.addListener(transitionAnimListener)
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
                            viewModel.editIngredient(
                                itemId,
                                name,
                                unit,
                                photoName,
                                expiration,
                                category
                            ).let { job ->
                                showLoading(job::cancel)
                            }
                        }
                    } else {
                        viewModel.isExistSameNameIngredient(etIngredientName.text.toString())
                    }
                }.apply {
                    (activity as? BaseActivity<*>)?.let {
                        it.compositeDisposable.add(this)
                    }
                }

            ivPhoto.setOnClickListener {
                AlertDialog.Builder(requireContext()).apply {
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
                                        takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
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
                                        pickPhotoIntent.resolveActivity(requireActivity().packageManager)?.also {
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
        viewModel.event.observe(this@IngredientAddFragment) {
            when (it.getContentIfNotHandled()) {
                IngredientAddEvent.InsertSuccess -> {
                    showToast(getString(R.string.ingredient_add_succeed_toast))
                    hideLoading()
//                    finishAfterTransition()
                }

                IngredientAddEvent.InsertFailed -> {
                    hideLoading()
                    showToast(getString(R.string.ingredient_add_failed_toast))
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
                    hideLoading()
//                    finishAfterTransition()
                    showToast(getString(R.string.ingredient_update_succeed_toast))
                }

                is IngredientAddEvent.UpdateFailed -> {
                    hideLoading()
//                    finishAfterTransition()
                    showToast(getString(R.string.ingredient_update_failed_toast))
                }

                else -> Unit
            }
        }
    }

    private fun initExtra() {
        isEditing = arguments?.getBoolean(EXTRA_IS_EDIT_MODE) ?: false
        editingIngredient = arguments?.getSerializable(EXTRA_INGREDIENT, IngredientEntity::class.java)
    }

    private fun initItemInfo() {
       if(isEditing) {
           binding.editingIngredient = editingIngredient
           activity?.title = getString(R.string.ingredient_edit_title)
           val expPosition = IngredientExpirationUnit.values()
               .indexOfFirst { it.days == editingIngredient?.expiration }.coerceAtLeast(0)
           binding.spExpiration.setSelection(expPosition)
       }
    }

    private fun checkPermissionAndRequestForActivityResult(
        permission: String,
        callback: ActivityResultLauncher<String>,
        onGranted: () -> Unit
    ) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
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
                requireContext(),
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
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = unitsAdapter
            addItemDecoration(getDecoration(units.lastIndex))
        }

        with(binding.rvCategories) {
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoriesAdapter
            addItemDecoration(getDecoration(IngredientCategoryType.values().lastIndex))
        }
    }
}