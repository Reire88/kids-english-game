package com.example.kidsenglishgame

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
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
        speech = SpeechHelper(this,
            onListeningChanged = { listening ->
                spinner.visibility = if (listening) ProgressBar.VISIBLE else ProgressBar.GONE
                micButton.isEnabled = !listening
            },
            onResult = { hypotheses ->
                handleSpeech(hypotheses)
            },
            onError = {
                // Treat as "no result": allow retry without penalty
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

    private fun ensureMicPermissionAndStart() {
        val perm = android.Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {
            speech.startListening()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(perm), 200)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            speech.startListening()
        }
    }

    private fun handleSpeech(hypotheses: List<String>) {
        val item = game.currentItem() ?: return
        val ok = SimilarityUtils.isAcceptable(hypotheses, item.word)

        if (ok) {
            sounds.success()
            feedbackText.text = "Right ✔"
            game.markCorrect()
            // short pause then next
            itemImage.postDelayed({
                if (game.isFinished()) {
                    showFinal()
                } else {
                    feedbackText.text = ""
                    updateUIForCurrent()
                }
            }, 900)
        } else {
            sounds.fail()
            game.markWrong()
            showWrongOrRetryMessage()
        }
    }

    private fun showWrongOrRetryMessage() {
        if (game.canRetryCurrent()) {
            feedbackText.text = "Wrong ✖  Try again"
        } else {
            feedbackText.text = "Wrong ✖"
            itemImage.postDelayed({
                if (game.isFinished()) {
                    showFinal()
                } else {
                    feedbackText.text = ""
                    updateUIForCurrent()
                }
            }, 900)
        }
    }

    private fun updateUIForCurrent() {
        val item = game.currentItem() ?: return
        itemImage.setImageResource(item.drawableResId(this))
        scoreText.text = "Score: ${game.score}"
        progressText.text = "${game.questionNumber()} / 10"
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
        super.onDestroy()
        speech.destroy()
        sounds.release()
    }
}
