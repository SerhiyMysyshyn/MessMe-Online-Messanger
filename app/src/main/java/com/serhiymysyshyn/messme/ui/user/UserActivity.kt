package com.serhiymysyshyn.messme.ui.user

import android.os.Bundle
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseActivity
import com.serhiymysyshyn.messme.ui.core.BaseFragment

class UserActivity : BaseActivity() {
    override var fragment: BaseFragment = UserFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }
}
