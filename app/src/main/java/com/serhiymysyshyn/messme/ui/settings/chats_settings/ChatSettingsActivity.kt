package com.serhiymysyshyn.messme.ui.settings.chats_settings

import android.content.res.ColorStateList
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import top.defaults.colorpicker.ColorPickerPopup;
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serhiymysyshyn.messme.R
import com.serhiymysyshyn.messme.cache.SharedPrefsManager
import com.serhiymysyshyn.messme.presentation.injection.CacheModule
import com.serhiymysyshyn.messme.ui.App
import kotlinx.android.synthetic.main.activity_chat_settings.*
import kotlinx.android.synthetic.main.toolbar_buttons.*

class ChatSettingsActivity : AppCompatActivity() {
    val cacheModule: CacheModule = CacheModule()
    private lateinit var sharedPrefsManager: SharedPrefsManager

    private lateinit var adapter: BackgroundImagesAdapter

    private var _previewBackgroundColor = 0
    private var _previewTintColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_settings)

        App.appComponent.inject(this)

        init()

        bgColorButton.setOnClickListener {
            createColorPicker(0,it)
        }

        tintColorButton.setOnClickListener {
            createColorPicker(1, it)
        }

        resetButton.setOnClickListener {
            changePreviewBackgroundColor(R.color.background_dark)
            changePreviewTintColor(R.color.black)
        }
    }







    private fun createColorPicker(mode: Int, it: View){
        ColorPickerPopup.Builder(this@ChatSettingsActivity).initialColor(Color.RED)
            .enableBrightness(true)
            .okTitle("OK")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(
                it,
                object : ColorPickerPopup.ColorPickerObserver() {
                    override fun onColorPicked(color: Int) {
                        when(mode){
                            0 -> {
                                _previewBackgroundColor = color
                                changePreviewBackgroundColor(_previewBackgroundColor)
                            }
                            1 -> {
                                _previewTintColor = color
                                changePreviewTintColor(_previewTintColor)
                            }
                        }
                    }
                })
    }

    private fun changePreviewBackgroundColor(color: Int){
        pre_Background.setCardBackgroundColor(color)
    }

    private fun changePreviewTintColor(color: Int){
        ImageViewCompat.setImageTintList(pre_Image, ColorStateList.valueOf(color));
    }

    private fun init(){
        setSupportActionBar(toolbar2)
        supportActionBar?.setTitle(R.string.chats_settings)
        toolbar2_save_button.visibility = View.GONE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPrefsManager = SharedPrefsManager(cacheModule.provideSharedPreferences(applicationContext!!))

        adapter = BackgroundImagesAdapter(this@ChatSettingsActivity)


        recyclerView2.layoutManager = LinearLayoutManager(this@ChatSettingsActivity)
        recyclerView2.adapter = adapter

        val imagesList: List<Int> = listOf<Int>(
            R.drawable.pattern1,
            R.drawable.pattern2,
            R.drawable.pattern3,
            R.drawable.pattern4,
            R.drawable.pattern5,
            R.drawable.pattern6,
            R.drawable.pattern7,
            R.drawable.pattern8,
            R.drawable.pattern9,
            R.drawable.pattern10,
            R.drawable.pattern11,
            R.drawable.pattern12,
            R.drawable.pattern13,
            R.drawable.pattern14,
            R.drawable.pattern15,
            R.drawable.pattern16,
            R.drawable.pattern17,
            R.drawable.pattern18,
            R.drawable.pattern19,
            R.drawable.pattern20,
            R.drawable.pattern21,
            R.drawable.pattern22,
            R.drawable.pattern23,
            R.drawable.pattern24,
            R.drawable.pattern25,
            R.drawable.pattern26,
            R.drawable.pattern27,
            R.drawable.pattern28,
            R.drawable.pattern29,
            R.drawable.pattern30,
            R.drawable.pattern31,
            R.drawable.pattern32,
            R.drawable.pattern33
        )

        adapter.updateAdapter(imagesList)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}