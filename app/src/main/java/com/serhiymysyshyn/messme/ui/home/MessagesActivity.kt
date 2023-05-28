package com.serhiymysyshyn.messme.ui.home

import android.os.Bundle
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseActivity
import com.serhiymysyshyn.messme.ui.core.BaseFragment

class MessagesActivity : BaseActivity() {
    override var fragment: BaseFragment = MessagesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }
}
