package com.serhiymysyshyn.messme.domain.media

import android.graphics.Bitmap
import com.serhiymysyshyn.messme.domain.interactor.UseCase
import javax.inject.Inject

class EncodeImageBitmap @Inject constructor(
    private val mediaRepository: MediaRepository
) : UseCase<String, Bitmap?>() {

    override suspend fun run(params: Bitmap?) = mediaRepository.encodeImageBitmap(params)
}