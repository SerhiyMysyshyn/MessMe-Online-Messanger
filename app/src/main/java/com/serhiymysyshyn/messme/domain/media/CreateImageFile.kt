package com.serhiymysyshyn.messme.domain.media

import android.net.Uri
import com.serhiymysyshyn.messme.domain.interactor.UseCase
import com.serhiymysyshyn.messme.domain.type.None
import javax.inject.Inject

class CreateImageFile @Inject constructor(
    private val mediaRepository: MediaRepository
) : UseCase<Uri, None>() {

    override suspend fun run(params: None) = mediaRepository.createImageFile()
}