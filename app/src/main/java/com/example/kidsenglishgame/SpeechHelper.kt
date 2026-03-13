package com.example.kidsenglishgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

class SpeechHelper(
    context: Context,
    private val onListeningChanged: (Boolean) -> Unit,
    private val onResult: (List<String>) -> Unit,
    private val onError: (Int) -> Unit
) {

    private val appContext = context.applicationContext
    private var recognizer: SpeechRecognizer? = null
    private var listening = false

    init {
        try {
            if (SpeechRecognizer.isRecognitionAvailable(appContext)) {
                recognizer = SpeechRecognizer.createSpeechRecognizer(appContext)
                recognizer?.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        setListening(true)
                    }

                    override fun onBeginningOfSpeech() {}

                    override fun onRmsChanged(rmsdB: Float) {}

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        setListening(false)
                    }

                    override fun onError(error: Int) {
                        setListening(false)
                        onError(error)
                    }

                    override fun onResults(results: Bundle?) {
                        setListening(false)
                        val matches = results
                            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            ?.toList()
                            ?: emptyList()
                        onResult(matches)
                    }

                    override fun onPartialResults(partialResults: Bundle?) {}

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        } catch (_: Exception) {
            recognizer = null
        }
    }

    fun startListening() {
        try {
            if (listening) return
            val localRecognizer = recognizer ?: run {
                onError(-1)
                return
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toLanguageTag())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Say it in English")
            }

            localRecognizer.startListening(intent)
        } catch (_: Exception) {
            setListening(false)
            onError(-1)
        }
    }

    private fun setListening(value: Boolean) {
        listening = value
        onListeningChanged(value)
    }

    fun destroy() {
        try {
            recognizer?.cancel()
            recognizer?.destroy()
        } catch (_: Exception) {
        }
        recognizer = null
    }
}
