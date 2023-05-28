package com.serhiymysyshyn.messme.presentation.viewmodel

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.serhiymysyshyn.messme.domain.media.CreateImageFile
import com.serhiymysyshyn.messme.domain.media.EncodeImageBitmap
import com.serhiymysyshyn.messme.domain.media.GetPickedImage
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class MediaViewModel @Inject constructor(
    val createImageFileUseCase: CreateImageFile,
    val encodeImageBitmapUseCase: EncodeImageBitmap,
    val getPickedImageUseCase: GetPickedImage
) : BaseViewModel() {

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 10001
        const val CAPTURE_IMAGE_REQUEST_CODE = 10002
    }

    class PickedImage(val bitmap: Bitmap, val string: String)

    var cameraFileCreatedData: MutableLiveData<Uri> = MutableLiveData()
    private var imageBitmapData: MutableLiveData<Bitmap> = MutableLiveData()
    var pickedImageData: MutableLiveData<PickedImage> = MutableLiveData()

    fun createCameraFile() {
        createImageFileUseCase(None()) { it.either(::handleFailure, ::handleCameraFileCreated) }
    }

    private fun getPickedImage(uri: Uri?) {
        updateProgress(true)
        getPickedImageUseCase(uri) { it.either(::handleFailure, ::handleImageBitmap) }
    }

    private fun encodeImage(bitmap: Bitmap) {
        encodeImageBitmapUseCase(bitmap) { it.either(::handleFailure, ::handleImageString) }
    }


    fun onPickImageResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val uri = when (requestCode) {
                PICK_IMAGE_REQUEST_CODE -> data?.data
                CAPTURE_IMAGE_REQUEST_CODE -> cameraFileCreatedData.value
                else -> null
            }

            getPickedImage(uri)
        }
    }

    private fun handleCameraFileCreated(uri: Uri) {
        this.cameraFileCreatedData.value = uri
    }

    private fun handleImageBitmap(bitmap: Bitmap) {
        this.imageBitmapData.value = bitmap

        encodeImage(bitmap)
    }

    private fun handleImageString(string: String) {
        this.pickedImageData.value = PickedImage(imageBitmapData.value!!, string)
        updateProgress(false)
    }

    override fun onCleared() {
        super.onCleared()
        createImageFileUseCase.unsubscribe()
        encodeImageBitmapUseCase.unsubscribe()
        getPickedImageUseCase.unsubscribe()
    }
}