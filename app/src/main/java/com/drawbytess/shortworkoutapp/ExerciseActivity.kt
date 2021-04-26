package com.drawbytess.shortworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 10 // Good for testing

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0 // Variable for exercise timer progress. As initial the exercise progress is set to 0. As we are about to start.
    private var exerciseTimerDuration: Long = 30 // Good for testing

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1  // Current Position of Exercise.

    private var tts: TextToSpeech? = null // Variable for Text to Speech

    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // Make the toolbar function like an action bar
        setSupportActionBar(toolbar_exercise_activity)

        // Gives warning that you aren't done with something are you sure you want to continue
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigate the activity on click on back button of action bar.
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogFunction()

        }

        // Initialise text to speech variable
        tts = TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()

        setupRestView() // REST View is set in this function

        // Sets up status tracker
        setupExerciseRecyclerView()

    }

    /**
     * Here is Destroy function we will reset the rest timer to initial if it is running.
     */
    public override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
        }

        super.onDestroy()
    }
    /**
     * TextToSpeech Member that needs to be called with TextToSpeech
     * Called once setUpExerciseView is initialized
     */
    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS){
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
                Toast.makeText(
                        this,
                        "The Language specified is not supported!",
                        Toast.LENGTH_LONG).show()
            }
        } else {
            Log.e("TTS", "Initialization failed! No audio available at this time.")
            Toast.makeText(
                    this,
                    "Initialization failed!",
                    Toast.LENGTH_LONG).show()
        }
    }
    /**
     * Function is used to set the timer for REST.
     */
    private fun setupRestView(){

        try {
            // MediaPlayer sound when in rest mode
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false // Stops sound from constantly playing
            player!!.start() // Starts Playback.
        } catch (e: Exception){
            e.printStackTrace()
        }
        // Here according to the view make it visible as this is Rest View so rest view is visible and exercise view is not.
        ll_restview.visibility = View.VISIBLE
        ll_exerciseview.visibility = View.GONE

        /**
         * Here firstly we will check if the timer is running the and it is not null then cancel the running timer and start the new one.
         * And set the progress to initial which is 0.
         */
        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }
    // Set up countdown for rest timer
    private fun setRestProgressBar() {

        progressBar.progress = restProgress // Sets the current progress to the specified value.

        restTimer = object: CountDownTimer(restTimerDuration * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++ // It is increased to ascending order
                progressBar.progress = 10 - restProgress // Indicates progress bar progress
                tv_Timer.text = (10 - restProgress).toString() // Current progress is set to text view in terms of seconds.
            }

            override fun onFinish() {
                currentExercisePosition++

                // Use establisj boolean (setIsSelected) to progress recyclerView
                exerciseList!![currentExercisePosition].setIsSelected(true)
                // Make changes based on changed value
                exerciseAdapter!!.notifyDataSetChanged()

                setUpExerciseView()
            }
        }.start()
    }

    private fun setUpExerciseView(){
        ll_restview.visibility = View.GONE
        ll_exerciseview.visibility = View.VISIBLE

        /**
         * Here firstly we will check if the timer is running the and it is not null then cancel the running timer and start the new one.
         * And set the progress to initial which is 0.
         */
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        /**
         * Here current exercise name and image is set to exercise view.
         */
        iv_exercise.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tv_exerciseText.text = exerciseList!![currentExercisePosition].getName()

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()
    }
    private fun setExerciseProgressBar() {

        progressBar_exercise.progress = exerciseProgress

        exerciseTimer = object: CountDownTimer(exerciseTimerDuration* 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBar_exercise.progress = 30 - exerciseProgress
                tv_exercise_timer.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < (exerciseList?.size!!- 1)){

                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setupRestView()
                } else {

                    finish()

                    val intent = Intent(
                        this@ExerciseActivity,
                        FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    // Text to speech set up after tts variable creation
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // Create Linear Layout on code
    private fun setupExerciseRecyclerView(){
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rvExerciseStatus.adapter = exerciseAdapter
    }

    // Warning before exiting the exercise
    private fun customDialogFunction() {
        val customDialog = Dialog(this)

        customDialog.setContentView(R.layout.dialog_custom) // Set reference page

        customDialog.btnYes.setOnClickListener(View.OnClickListener {
            finish() // Ends the exercise
            customDialog.dismiss()

        })

        customDialog.btnNo.setOnClickListener(View.OnClickListener {
            customDialog.dismiss()
        })
        customDialog.show()
    }
}