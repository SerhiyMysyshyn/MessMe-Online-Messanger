package com.serhiymysyshyn.messme.ui.account

import android.os.Bundle
import com.serhiymysyshyn.messme.ui.App
import com.serhiymysyshyn.messme.ui.core.BaseActivity
import com.serhiymysyshyn.messme.ui.core.BaseFragment

class AccountActivity : BaseActivity() {
    override var fragment: BaseFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }
}
