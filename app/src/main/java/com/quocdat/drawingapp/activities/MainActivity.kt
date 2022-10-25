package com.quocdat.drawingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quocdat.drawingapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_view.setSizeForBrush(20.toFloat())
    }
}