package com.serhiymysyshyn.messme.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.ui.core.navigation.Navigator
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    abstract val layoutId: Int

    open val titleToolbar = R.string.chats
    open val showToolbar = true

    open val showHelpButton = true

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(layoutId, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()

        base {
            if (showToolbar) supportActionBar?.show() else supportActionBar?.hide()
            supportActionBar?.title = getString(titleToolbar)
        }
    }

    open fun setHelpButtonClickListener(listener: View.OnClickListener){
        view?.findViewById<ImageButton>(R.id.mainNavigatorButton)?.apply {
            setOnClickListener(listener)
        }
    }

    open fun setHelpButtonImageResource(resource: Int){
        view?.findViewById<ImageButton>(R.id.mainNavigatorButton)?.apply {
            setImageResource(resource)
        }
    }

    open fun onBackPressed() {}


    open fun updateProgress(status: Boolean?) {
        if (status == true) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    fun showProgress() = base { progressStatus(View.VISIBLE) }

    fun hideProgress() = base { progressStatus(View.GONE) }


    fun hideSoftKeyboard() = base { hideSoftKeyboard() }


    fun handleFailure(failure: Failure?) = base { handleFailure(failure) }

    fun showMessage(message: String) = base { showMessage(message) }


    inline fun base(block: BaseActivity.() -> Unit) {
        activity.base(block)
    }


    inline fun <reified T : ViewModel> viewModel(body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, viewModelFactory)[T::class.java]
        vm.body()
        return vm
    }

    fun close() = fragmentManager?.popBackStack()
}
