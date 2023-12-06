package com.example.viewanimationexample

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity()
{

  private var imageView : ImageView? = null
  var phonetemp : Rect = Rect(0,0,0,0)
  var liontemp : Rect = Rect(0,0,0,0)
  var cobratemp : Rect = Rect(0,0,0,0)
  var rabbittemp : Rect = Rect(0,0,0,0)


  companion object
  {
    private var instance : MainActivity? = null
    public fun getInstance() : MainActivity
    {
      return instance!!
    }
  }


  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    instance = this

    setContentView(R.layout.activity_main)
    // Get references to your image views
    val phone = findViewById<ImageView>(R.id.phone_img)
    val lion = findViewById<ImageView>(R.id.lion)
    val cobra = findViewById<ImageView>(R.id.cobra)
    val rabbit = findViewById<ImageView>(R.id.rabbit)

    // Store initial positions in temporary variables
    phonetemp = Rect(phone.left, phone.top, phone.right, phone.bottom)
    liontemp = Rect(lion.left, lion.top, lion.right, lion.bottom)
    cobratemp = Rect(cobra.left, cobra.top, cobra.right, cobra.bottom)
    rabbittemp = Rect(rabbit.left, rabbit.top, rabbit.right, rabbit.bottom)

  }

}