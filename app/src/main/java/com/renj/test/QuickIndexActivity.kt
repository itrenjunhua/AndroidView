package com.renj.test

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import com.renj.view.QuickIndexView

class QuickIndexActivity : AppCompatActivity() {
    private lateinit var quickIndexView: QuickIndexView
    private lateinit var tvLabel: TextView

    companion object {
        const val DEFAULT_DELAY = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_index)

        quickIndexView = findViewById(R.id.quick_index_view)
        tvLabel = findViewById(R.id.tv_label)

        quickIndexView.setOnIndexChangedListener { position, content ->
            showIndexLabel(position, content)
        }
    }


    private var mHandler = Handler()

    @SuppressLint("SetTextI18n")
    private fun showIndexLabel(position: Int, word: String) {
        tvLabel.visibility = View.VISIBLE
        tvLabel.text = "$position:$word"

        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({
            tvLabel.visibility = View.GONE
        }, DEFAULT_DELAY)
    }

}