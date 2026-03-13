package com.example.kidsenglishgame

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var prizeImage: ImageView
    private lateinit var feedbackText: TextView
    private lateinit var scoreText: TextView
    private lateinit var progressText: TextView
    private lateinit var micButton: Button
    private lateinit var restartButton: Button
    private lateinit var spinner: ProgressBar

    private lateinit var speech: SpeechHelper
    private lateinit var sounds: SoundPlayer

    private val game = GameManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemImage = findViewById(R.id.itemImage)
        prizeImage = findViewById(R.id.prizeImage)
        feedbackText = findViewById(R.id.feedbackText)
        scoreText = findViewById(R.id.scoreText)
        progressText = findViewById(R.id.progressText)
        micButton = findViewById(R.id.micButton)
        restartButton = findViewById(R.id.restartButton)
        spinner = findViewById(R.id.listeningSpinner)

        sounds = SoundPlayer(this)

        speech = SpeechHelper(
            this,
            onListeningChanged = { listening ->
                spinner.visibility = if (listening) ProgressBar.VISIBLE else ProgressBar.GONE
                micButton.isEnabled = !listening
            },
            onResult = { hypotheses ->
                handleSpeech(hypotheses)
            },
            onError = {
                game.markNeedsRetry()
                showWrongOrRetryMessage()
            }
        )

        micButton.setOnClickListener {
            ensureMicPermissionAndStart()
        }

        restartButton.setOnClickListener {
            startNewGame()
        }

        startNewGame()
    }

    private fun startNewGame() {
        game.startNewRound()
        prizeImage.visibility = ImageView.GONE
        restartButton.visibility = Button.GONE
        micButton.visibility = Button.VISIBLE
        itemImage.visibility = ImageView.VISIBLE
        feedbackText.text = ""
        updateUIForCurrent()
    }

    private fun updateUIForCurrent() {
        val item = game.currentItem() ?: return
        val resId = item.drawableResId(this)

        try {
            if (resId != 0) {
                itemImage.setImageResource(resId)
                feedbackText.text = item.word
            } else {
                feedbackText.text = "Missing image: ${item.word}"
                itemImage.setImageDrawable(null)
            }
        } catch (e: Exception) {
            feedbackText.text = "Image error: ${item.word}"
            itemImage.setImageDrawable(null)
        }

        scoreText.text = "Score: ${game.score}"
        progressText.text = "${game.questionNumber()} / 10"
    }

    private fun ensureMicPermissionAndStart() {
        val perm = android.Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {
            speech.startListening()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(perm), 200)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            speech.startListening()
        }
    }

    private fun handleSpeech(hypotheses: List<String>) {
        try {
            val item = game.currentItem() ?: return
            val ok = SimilarityUtils.isAcceptable(hypotheses, item.word)

            if (ok) {
                feedbackText.text = "Right ✔"
                game.markCorrect()

                itemImage.postDelayed({
                    if (isFinishing || isDestroyed) return@postDelayed

                    try {
                        if (game.isFinished()) {
                            showFinal()
                        } else {
                            feedbackText.text = ""
                            updateUIForCurrent()
                        }
                    } catch (e: Exception) {
                        feedbackText.text = "Error after right answer"
                    }
                }, 900)
            } else {
                game.markWrong()
                showWrongOrRetryMessage()
            }
        } catch (e: Exception) {
            feedbackText.text = "Speech error"
        }
    }

    private fun showWrongOrRetryMessage() {
        try {
            if (game.canRetryCurrent()) {
                feedbackText.text = "Wrong ✖ Try again"
            } else {
                feedbackText.text = "Wrong ✖"

                itemImage.postDelayed({
                    if (isFinishing || isDestroyed) return@postDelayed

                    try {
                        if (game.isFinished()) {
                            showFinal()
                        } else {
                            feedbackText.text = ""
                            updateUIForCurrent()
                        }
                    } catch (e: Exception) {
                        feedbackText.text = "Error after wrong answer"
                    }
                }, 900)
            }
        } catch (e: Exception) {
            feedbackText.text = "Retry error"
        }
    }

    private fun showFinal() {
        itemImage.visibility = ImageView.GONE
        micButton.visibility = Button.GONE
        restartButton.visibility = Button.VISIBLE
        prizeImage.visibility = ImageView.VISIBLE

        val score = game.score
        scoreText.text = "Score: $score"
        progressText.text = "Done"
        feedbackText.text = "Great job!"

        prizeImage.setImageResource(game.prizeForScore(score).drawableResId(this))
    }

    override fun onDestroy() {
        try {
            speech.destroy()
        } catch (_: Exception) {
        }

        try {
            sounds.release()
        } catch (_: Exception) {
        }

        super.onDestroy()
    }
}
