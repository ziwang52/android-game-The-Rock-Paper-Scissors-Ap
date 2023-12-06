package com.example.viewanimationexample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import kotlin.random.Random

class MyView : View

{



  private var animal: ImageView? = null
  private val paint = Paint()
  private val random: Random
    get() = Random
  private var phone: ImageView? = null
  private var lion: ImageView? = null
  private var cobra: ImageView? = null
  private var rabbit: ImageView? = null
  private var animals: ImageView? = null
  private var isInBlueRectangular = false
  private var image_touched =false
  private var lion_touched =false
  private var rabbit_touched =false
  private var cobra_touched =false
  private var incomplete =false

  private var phonecount =0
  private var gameInProgress = false
  private var playercount =0
  private var currentAnimalResource: Int? = null
  private var originalCoords: Rect? = null
  private var ballCoords : Rect = Rect(0,0,0,0)
  private var bluerect : Rect = Rect(0,0,0,0)
  private var temprect : Rect = Rect(0,0,0,0)
  private var weight: Int? = null
  private var  height: Int? = null
  private val lionImages = arrayOf(
    R.drawable.lion0,
    R.drawable.lion1,
    R.drawable.lion2,
    R.drawable.lion3
  )
  private val animalImages = arrayOf(
    R.drawable.rabbit,
    R.drawable.lion0,
    R.drawable.cobra
  )
  private var handler = Handler(Looper.getMainLooper())
  private val redrawRunnable = object : Runnable {
    override fun run() {
      invalidate() // Redraw the view
      handler.postDelayed(this, 1000) // Adjust the delay as needed
    }
  }
  companion object
  {
    private var instance : MyView? = null
    public fun getInstance() : MyView
    {
      return instance!!
    }
  }
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  {
    this.setWillNotDraw(false)
    instance = this

  }
  @SuppressLint("ClickableViewAccessibility")
  private fun initializeGameElements() {

    lion = MainActivity.getInstance().findViewById<ImageView>(R.id.lion)
    cobra = MainActivity.getInstance().findViewById<ImageView>(R.id.cobra)
    rabbit = MainActivity.getInstance().findViewById<ImageView>(R.id.rabbit)
    // Log statements for debugging
    Log.d("MyView", "lion: $lion, cobra: $cobra, rabbit: $rabbit")
// Set touch listeners for animal images
   //lion?.setOnTouchListener { _, event -> onTouchEvent(event) }
 //   cobra?.setOnTouchListener { _, event -> onTouchEvent(event ) }
   // rabbit?.setOnTouchListener { _, event -> onTouchEvent(event ) }


  }
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
  {
    super.onSizeChanged(w, h, oldw, oldh)
    initializeGameElements()
    lion?.tag = Rect(lion?.left!!, lion?.top!!, lion?.right!!, lion?.bottom!!)
    cobra?.tag = Rect(cobra?.left!!, cobra?.top!!, cobra?.right!!, cobra?.bottom!!)
    rabbit?.tag = Rect(rabbit?.left!!, rabbit?.top!!, rabbit?.right!!, rabbit?.bottom!!)

    val title = MainActivity.getInstance().findViewById<ImageView>(R.id.title1)
    val text1=  MainActivity.getInstance().findViewById<TextView>(R.id.phone)
    val text2=  MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    val text3=  MainActivity.getInstance().findViewById<TextView>(R.id.player)
    val text4=  MainActivity.getInstance().findViewById<TextView>(R.id.playerscore)
// Load zoom-in animation
    val zoomInAnimation = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.zoom_in)

// Load fade-out animation
    val fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)

// Combine both animations in an AnimationSet
    val animationSet = AnimationSet(false)
    animationSet.addAnimation(zoomInAnimation)
    animationSet.addAnimation(fadeOutAnimation)



// Set AnimationListener to handle final state after animation completes
    animationSet.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Optionally set final state after both animations complete
        title.scaleX = 3f
        title.scaleY = 3f
        title.visibility = INVISIBLE  // or View.GONE if you want to hide it
        // Start fade-in animations on TextViews
        val fadeIn = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_in)
        text1.startAnimation(fadeIn)
        text2.startAnimation(fadeIn)
        text3.startAnimation(fadeIn)
        text4.startAnimation(fadeIn)
      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

// Start the combined animation
    title.startAnimation(animationSet)

  }
  @SuppressLint("DrawAllocation")
  override fun onDraw(canvas: Canvas)
  {
    super.onDraw(canvas)
    var width = getWidth()
    var height = getHeight()

    paint.color = Color.BLUE
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 10f

    if (lion_touched) {
      drawContinuousLionImages(canvas)
    }

    val canvasWidth = canvas.width
    val canvasHeight = canvas.height

    val rectWidth = canvasWidth * 0.6f
    val rectHeight = canvasHeight * 0.7f

    val left = (canvasWidth - rectWidth) / 2
    val top = canvasHeight / 2 - rectHeight / 2
    val right = left + rectWidth
    val bottom = top + rectHeight

    bluerect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    canvas.drawRect(bluerect, paint)

    val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.jungleframe)
    val rectToFill = RectF(left, top, right, bottom)
    canvas.drawBitmap(bitmap, null, rectToFill, null)

    canvas.drawLine(left, top, left, bottom, paint)
    canvas.drawLine(right, top, right, bottom, paint)
    paint.strokeWidth = 30f
    canvas.drawLine(left, top, right, top, paint)
    canvas.drawLine(left, bottom, right, bottom, paint)

    animals?.setLeftTopRightBottom(ballCoords.left, ballCoords.top, ballCoords.right,
      ballCoords.bottom)


  }
  private fun drawContinuousLionImages(canvas: Canvas) {
    // Get the dimensions of the canvas
    val canvasWidth = canvas.width
    val canvasHeight = canvas.height

    // Calculate the position to draw lion images based on guidelines
    val lionXGuide = MainActivity.getInstance().findViewById<Guideline>(R.id.lionx)
    val lionYGuide = MainActivity.getInstance().findViewById<Guideline>(R.id.liony)
    if (lionXGuide == null || lionYGuide == null) {
      Log.e("MyView", "Guidelines not found")
      return
    }
    val lionX = (lionXGuide.layoutParams as ConstraintLayout.LayoutParams).guidePercent * canvasWidth
    val lionY = (lionYGuide.layoutParams as ConstraintLayout.LayoutParams).guidePercent * canvasHeight

    // Calculate the coordinates for the lion images
    val left = lionX
    val top = lionY-(height!! * 1.5).toInt()
    val right = left +(weight!! * 1.5).toInt()
    val bottom = top + (height!! * 2.0).toInt()

    // Draw the lion image at the calculated position
    val lionImageIndex = (System.currentTimeMillis() / 100) % lionImages.size
    val lionImageResource = lionImages[lionImageIndex.toInt()]
    val lionBitmap: Bitmap = BitmapFactory.decodeResource(resources, lionImageResource)
    val lionRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    canvas.drawBitmap(lionBitmap, null, lionRect, null)
  }

  private fun moveImageToOriginalPosition(imageView: ImageView, originalRect: Rect) {
    // Move the image back to its original position
    imageView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom)
  }
  fun resetGame() {
    // Reset variables
    gameInProgress = false


    // Reset views
    val resultTextView = MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    val phoneImageView = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)
    val lionImageView = MainActivity.getInstance().findViewById<ImageView>(R.id.lion)
    val cobraImageView = MainActivity.getInstance().findViewById<ImageView>(R.id.cobra)
    val rabbitImageView = MainActivity.getInstance().findViewById<ImageView>(R.id.rabbit)



    // Apply animation to the animal ImageView

    phoneImageView.setImageResource(0) // Reset the phone image

    // Optionally, reset other game elements and views as needed

      // Move images back to their original positions using temp variables
    moveImageToOriginalPosition(phoneImageView, MainActivity.getInstance().phonetemp)
    moveImageToOriginalPosition(lionImageView, MainActivity.getInstance().liontemp)
    moveImageToOriginalPosition(cobraImageView, MainActivity.getInstance().cobratemp)
    moveImageToOriginalPosition(rabbitImageView, MainActivity.getInstance().rabbittemp)

    // Reset ballCoords positions
    //ballCoords.set(0, 0, 0, 0)

    animals  = null
    isInBlueRectangular = false
    image_touched =false
    lion_touched =false
    rabbit_touched =false
    cobra_touched =false

    gameInProgress = false

    currentAnimalResource  = null
    originalCoords  = null
    //ballCoords  = Rect(0,0,0,0)


    phoneImageView.clearAnimation()
    animals?.clearAnimation()
    lionImageView.clearAnimation()
    cobraImageView.clearAnimation()
    rabbitImageView.clearAnimation()



  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    var x = event.rawX
    var y = event.rawY
       if (event.getAction() == MotionEvent.ACTION_DOWN )
    {
      println("touched down initially - $x, $y")

      //Verify if touch inside the ball
        if ( (x > lion?.left!!) && (x < lion?.right!!) &&
        (y > lion?.top!!) && (y < lion?.bottom!!))
      {    lion?.tag = Rect(lion?.left!!, lion?.top!!, lion?.right!!, lion?.bottom!!)

        ballCoords = Rect(lion?.left!!,lion?.top!!,lion?.right!!,lion?.bottom!!)
        temprect=Rect(lion?.left!!,lion?.top!!,lion?.right!!,lion?.bottom!!)
       weight=lion!!.width
        height= lion!!.height
        animals=lion
        image_touched = true
        lion_touched=true
        gameInProgress=true
      }
      else if ( (x > cobra?.left!!) && (x < cobra?.right!!) &&
          (y > cobra?.top!!) && (y < cobra?.bottom!!))
        {
          cobra?.tag  = Rect(cobra?.left!!,cobra?.top!!,cobra?.right!!,cobra?.bottom!!)
          ballCoords = Rect(cobra?.left!!,cobra?.top!!,cobra?.right!!,cobra?.bottom!!)
          temprect=Rect(cobra?.left!!,cobra?.top!!,cobra?.right!!,cobra?.bottom!!)

          weight=cobra!!.width
          height= cobra!!.height
          animals=cobra
          image_touched = true
          lion_touched=false
          cobra_touched =true
          gameInProgress=true


        }
        else if ( (x > rabbit?.left!!) && (x < rabbit?.right!!) &&
          (y > rabbit?.top!!) && (y < rabbit?.bottom!!))
        {
          rabbit?.tag=Rect(rabbit?.left!!,rabbit?.top!!,rabbit?.right!!,rabbit?.bottom!!)
          ballCoords = Rect(rabbit?.left!!,rabbit?.top!!,rabbit?.right!!,rabbit?.bottom!!)
          temprect=Rect(rabbit?.left!!,rabbit?.top!!,rabbit?.right!!,rabbit?.bottom!!)

          weight=rabbit!!.width
          height= rabbit!!.height
          animals=rabbit
          rabbit_touched=true
          image_touched = true
          lion_touched=false
          gameInProgress=true

        }
      if (lion_touched) {
      handler.post(redrawRunnable) // Start redrawing at regular intervals
    }
    }
    else if (event.getAction() ==  MotionEvent.ACTION_MOVE)
    {
      println("moving $x, $y")
    if(gameInProgress){
      if (image_touched)
      {

        image_touched=true
        ballCoords.offsetTo(x.toInt(), y.toInt())
        this.invalidate()
      }
      }
    }
    else if (event.getAction() == MotionEvent.ACTION_UP) {
      println("touched up - $x, $y")
      var myFadeout = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)

      handler.removeCallbacks(redrawRunnable) // Stop redrawing
      if(gameInProgress)
      {
      image_touched = true
      if (x < bluerect?.left!! || x > bluerect?.right!! || y < bluerect?.top!! || y > bluerect?.bottom!!) {
        val delta_x = (temprect.left - ballCoords.left).toFloat()
        val delta_y = (temprect.top - ballCoords.top).toFloat()

        val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
        moveAnim.duration = 500
        moveAnim.fillAfter = true

        // Apply animation to the animal ImageView
        animals?.startAnimation(moveAnim)
       // resetGame()

      }



      // Calculate the position to draw   images based on guidelines
      val animal_y = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocationy)
      val animal_x = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)


      // Calculate the position to move the animal
      val moveToX = animal_x?.left?.toFloat() ?: 0f
      val moveToY = animal_y?.top?.toFloat() ?: 0f
      val delta_x = (moveToX - ballCoords?.left!!).toFloat()
      val delta_y = (moveToY - ballCoords?.top!!).toFloat()

      val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
      moveAnim.duration = 500
      moveAnim.fillAfter = true

      // Apply animation to the animal ImageView
      animals?.startAnimation(moveAnim)



      if (rabbit_touched) {
        animals?.scaleX = -1.0f
      } else {
        animals?.scaleX = 1.0f
      }
      // Randomly select another animal
      var phone = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)

// Get a random animal resource ID
      val randomAnimalResource = animalImages[random.nextInt(animalImages.size)]

// Set the image resource of the phone ImageView
      phone.setImageResource(randomAnimalResource)
      if (randomAnimalResource == R.drawable.cobra || randomAnimalResource == R.drawable.lion0) {
        // Flips horizontally
        phone.scaleX = -1.0f

      } else {
        phone.scaleX = 1.0f
      }

      var myFadeIn = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_in)

        myFadeIn.setAnimationListener(object : Animation.AnimationListener {
          override fun onAnimationStart(animation: Animation?) {

          }

          override fun onAnimationEnd(animation: Animation?) {

            when {

              lion_touched && (randomAnimalResource == R.drawable.cobra) -> startCobraDefeatsLionAnimation()
              lion_touched && (randomAnimalResource == R.drawable.rabbit) -> startLionDefeatsRabbitAnimation()
              lion_touched && (randomAnimalResource == R.drawable.lion0) -> startTieAnimation()
              cobra_touched && (randomAnimalResource == R.drawable.cobra) -> startTieAnimation()
              cobra_touched && (randomAnimalResource == R.drawable.rabbit) -> startrabbitDefeatsCobraAnimation()
              cobra_touched && (randomAnimalResource == R.drawable.lion0) -> cobrawinlion()
              rabbit_touched && (randomAnimalResource == R.drawable.cobra) -> rabbitwincobra()
              rabbit_touched && (randomAnimalResource == R.drawable.rabbit) -> tie()
              rabbit_touched && (randomAnimalResource == R.drawable.lion0) -> rabbitloselion()



              // Add more cases if needed
            }

          }

          override fun onAnimationRepeat(animation: Animation?) {}
        })

        phone.startAnimation(myFadeIn)



    }
    }

    return true  //Use up this event prevents the bubbling to a parent of this view
  }
  private fun rabbitloselion()
  {phonecount+=1
    val result = MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    result.text = phonecount.toString()
    val phone = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)
    val animal_x = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)
    val animal_y = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocationy)
    val phonex = MainActivity.getInstance().findViewById<Guideline>(R.id.phonex)

    // Calculate the position to move the animal
    val moveToX = animal_x?.left?.toFloat() ?: 0f
    val moveToY = animal_y?.top?.toFloat() ?: 0f
    val delta_x = (moveToX - phonex.left).toFloat()
    val delta_y = (moveToY - animal_y.top).toFloat()

    // Create move animation
    val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
    moveAnim.duration = 500
    moveAnim.fillAfter = true

    phone?.startAnimation(moveAnim)
    // Start fade-out animation for the lion after the move animation completes
    moveAnim.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Start fade-out animation for the animal


        // Update result text with fade-out effect
        val resultTextView = MainActivity.getInstance().findViewById<TextView>(R.id.result)
        resultTextView.text = " Lion defeats Rabbit – You Lose! "
        val fadeOutResult = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
        resultTextView.startAnimation(fadeOutResult)

        // Reset the game or perform additional actions
       resetGame()


      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

    val fadeOutAnim = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
    // Start the fade-out animation for the animal
    animals?.startAnimation(fadeOutAnim)


  }


  private fun tie()
  {
    val result1 = MainActivity.getInstance().findViewById<TextView>(R.id.result)

    val fadeOut = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
    phone?.startAnimation(fadeOut)
    animals?.startAnimation(fadeOut)
    // Optionally, set a listener to handle actions after the fade-out animation
    fadeOut.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
      result1.text = "Both animals and 'Tie!'"
        result1.startAnimation(fadeOut)
        // Actions to perform after the fade-out animation completes
        resetGame()
      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

    // Start fade-out animation for phone, lion, and result text


  }
  private fun rabbitwincobra()
  {playercount++
    val result = MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    result.text = playercount.toString()
    val phone = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)
    val animal_x = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)
    val animal_y = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocationy)

    // Calculate the position to move the animal
    val moveToX = phone?.left?.toFloat() ?: 0f
    val moveToY = phone?.top?.toFloat() ?: 0f
    val delta_x = (moveToX - animal_x.left).toFloat()
    val delta_y = (moveToY - animal_y.top).toFloat()

    // Create move animation
    val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
    moveAnim.duration = 500
    moveAnim.fillAfter = true

    animals?.startAnimation(moveAnim)
    // Start fade-out animation for the lion after the move animation completes
    moveAnim.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Start fade-out animation for the animal


        // Update result text with fade-out effect
        val resultTextView = MainActivity.getInstance().findViewById<TextView>(R.id.result)
        resultTextView.text = " Rabbit defeats Cobra – You Win! "
        val fadeOutResult = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
        resultTextView.startAnimation(fadeOutResult)

        // Reset the game or perform additional actions
         resetGame()


      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

    val fadeOutAnim = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
    // Start the fade-out animation for the animal
    phone?.startAnimation(fadeOutAnim)


  }
  private fun cobrawinlion()
  { playercount+=1
    val result = MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    result.text = phonecount.toString()
    val phone = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)
    val animal_x = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)
    val animal_y = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocationy)
    val phonex = MainActivity.getInstance().findViewById<Guideline>(R.id.phonex)

    // Calculate the position to move the animal
    val moveToX = phonex?.left?.toFloat() ?: 0f
    val moveToY = animal_y?.top?.toFloat() ?: 0f
    val delta_x = (moveToX - animal_x.left).toFloat()
    val delta_y = (moveToY - animal_y.top).toFloat()

    // Create move animation
    val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
    moveAnim.duration = 500
    moveAnim.fillAfter = true

    animals?.startAnimation(moveAnim)
    // Start fade-out animation for the lion after the move animation completes
    moveAnim.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Start fade-out animation for the animal


        // Update result text with fade-out effect
        val resultTextView = MainActivity.getInstance().findViewById<TextView>(R.id.result)
        resultTextView.text = " Cobra defeats Lion – You Win! "
        val fadeOutResult = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
        resultTextView.startAnimation(fadeOutResult)

        // Reset the game or perform additional actions
        resetGame()


      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

    val fadeOutAnim = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
    // Start the fade-out animation for the animal
    phone?.startAnimation(fadeOutAnim)

  }

  private fun startrabbitDefeatsCobraAnimation()
  {   phonecount += 1
    val result = MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    result.text = phonecount.toString()
    val phone = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)
    val animal_x = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)
    val animal_y = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocationy)
    val phonex = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)

    // Calculate the position to move the animal
    val moveToX = animal_x?.left?.toFloat() ?: 0f
    val moveToY = animal_y?.top?.toFloat() ?: 0f
    val delta_x = (moveToX - phonex.left).toFloat()
    val delta_y = (moveToY - animal_y.top).toFloat()

    // Create move animation
    val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
    moveAnim.duration = 500
    moveAnim.fillAfter = true

    phone?.startAnimation(moveAnim)
    // Start fade-out animation for the lion after the move animation completes
    moveAnim.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Start fade-out animation for the animal

        val fadeOutAnim = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
        // Start the fade-out animation for the animal
       fadeOutAnim.setAnimationListener(object : Animation.AnimationListener {
         override fun onAnimationStart(animation: Animation?) {}

         override fun onAnimationEnd(animation: Animation?) {
           val resultTextView = MainActivity.getInstance().findViewById<TextView>(R.id.result)
           resultTextView.text = " Rabbit defeats Cobra – You Lose! "
           val fadeOutResult = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
           fadeOutResult.setAnimationListener(object : Animation.AnimationListener {
             override fun onAnimationStart(animation: Animation?) {}

             override fun onAnimationEnd(animation: Animation?) {

               // Reset the game or perform additional actions
               resetGame()


             }

             override fun onAnimationRepeat(animation: Animation?) {}
           })

           resultTextView.startAnimation(fadeOutResult)

         }

         override fun onAnimationRepeat(animation: Animation?) {}
       })
        animals?.startAnimation(fadeOutAnim)
        // Update result text with fade-out effect

      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })



  }



  @SuppressLint("WrongViewCast")
  private fun startCobraDefeatsLionAnimation() {
    phonecount += 1

    val phone = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)
    val result = MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    result.text = phonecount.toString()

    // Calculate the position to move the animal
    val animal_x = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)
    val animal_y = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocationy)
    val phonex = MainActivity.getInstance().findViewById<Guideline>(R.id.phonex)


    // Calculate the position to move the animal
    val moveToX = animal_x?.left?.toFloat() ?: 0f
    val moveToY = animal_y?.top?.toFloat() ?: 0f
    val delta_x = (moveToX - phonex.left).toFloat()
    val delta_y = (moveToY - animal_y.top ).toFloat()
    // Move the lion to the cobra position
    val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
    moveAnim.duration = 500
    moveAnim.fillAfter = true
    // Apply animation to the lion ImageView
    phone?.startAnimation(moveAnim)
    // Start fade-out animation for the lion after the move animation completes
    moveAnim.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Start fade-out animation for the animal


           // Update result text with fade-out effect
            val resultTextView = MainActivity.getInstance().findViewById<TextView>(R.id.result)
            resultTextView.text = "Cobra defeats Lion – You lose! "
            val fadeOutResult = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
            resultTextView.startAnimation(fadeOutResult)


            // Reset the game or perform additional actions
              resetGame()



      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

    val fadeOutAnim = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
    // Start the fade-out animation for the animal
    animals?.startAnimation(fadeOutAnim)

  }
  private fun startLionDefeatsRabbitAnimation() {
    playercount += 1
    val phone = MainActivity.getInstance().findViewById<ImageView>(R.id.phone_img)
    val result = MainActivity.getInstance().findViewById<TextView>(R.id.pscore)
    result.text = playercount.toString()

    // Calculate the position to move the animal
    val animal_x = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocation_x)
    val animal_y = MainActivity.getInstance().findViewById<Guideline>(R.id.fixlocationy)
    val phonex = MainActivity.getInstance().findViewById<Guideline>(R.id.phonex)


    // Calculate the position to move the animal
    val moveToX = phonex?.left?.toFloat() ?: 0f
    val moveToY = phone?.top?.toFloat() ?: 0f
    val delta_x = (moveToX - animal_x.left).toFloat()
    val delta_y = (moveToY - animal_y.top ).toFloat()
    // Move the lion to the cobra position
    val moveAnim = TranslateAnimation(0f, delta_x, 0f, delta_y)
    moveAnim.duration = 500
    moveAnim.fillAfter = true
    // Apply animation to the lion ImageView
    animals?.startAnimation(moveAnim)
    // Start fade-out animation for the lion after the move animation completes
    moveAnim.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Start fade-out animation for the animal


        // Update result text with fade-out effect
        val resultTextView = MainActivity.getInstance().findViewById<TextView>(R.id.result)
        resultTextView.text = "Lion defeats Rabbit – You Win! "
        val fadeOutResult = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
        resultTextView.startAnimation(fadeOutResult)


        // Reset the game or perform additional actions
        resetGame()



      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

    val fadeOutAnim = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
    // Start the fade-out animation for the animal
    phone?.startAnimation(fadeOutAnim)
  }

  private fun startTieAnimation() {
  //  val phone = findViewById<ImageView>(R.id.phone_img)
    val lion = findViewById<ImageView>(R.id.lion)
    val result1 = MainActivity.getInstance().findViewById<TextView>(R.id.result)

    val fadeOut = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)

    // Optionally, set a listener to handle actions after the fade-out animation
    fadeOut.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        // Actions to perform after the fade-out animation completes
         resetGame()
      }

      override fun onAnimationRepeat(animation: Animation?) {}
    })

    // Start fade-out animation for phone, lion, and result text
    phone?.startAnimation(fadeOut)
    animals?.startAnimation(fadeOut)
    result1.text = "Both animals and 'Tie!'"
    result1.startAnimation(fadeOut)
  }



}

