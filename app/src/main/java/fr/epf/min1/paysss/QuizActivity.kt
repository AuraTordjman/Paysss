package fr.epf.min1.paysss

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import fr.epf.min1.paysss.models.Country
import fr.epf.min1.paysss.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var answerButton1: Button
    private lateinit var answerButton2: Button
    private lateinit var answerButton3: Button
    private lateinit var answerButton4: Button

    private lateinit var countries: List<Country>
    private lateinit var questionGenerator: QuestionGenerator
    private lateinit var currentQuestion: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionTextView = findViewById(R.id.questionTextView)
        answerButton1 = findViewById(R.id.answerButton1)
        answerButton2 = findViewById(R.id.answerButton2)
        answerButton3 = findViewById(R.id.answerButton3)
        answerButton4 = findViewById(R.id.answerButton4)

        loadCountries()
    }

    private fun loadCountries() {
        RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    countries = response.body() ?: return
                    if (countries.isNotEmpty()) {
                        questionGenerator = QuestionGenerator(countries)
                        loadNextQuestion()
                    }
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {

            }
        })
    }

    private fun loadNextQuestion() {
        currentQuestion = questionGenerator.generateQuestion()
        displayQuestion(currentQuestion)
    }

    private fun displayQuestion(question: Question) {
        questionTextView.text = question.questionText
        answerButton1.text = question.answers[0]
        answerButton2.text = question.answers[1]
        answerButton3.text = question.answers[2]
        answerButton4.text = question.answers[3]

        val buttons = listOf(answerButton1, answerButton2, answerButton3, answerButton4)

        buttons.forEach { button ->
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            button.isEnabled = true
            button.setOnClickListener {
                val selectedAnswer = button.text.toString()
                if (selectedAnswer == question.correctAnswer) {
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
                } else {
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_700))
                    buttons.first { it.text == question.correctAnswer }
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
                }
                buttons.forEach { it.isEnabled = false }

                Handler(Looper.getMainLooper()).postDelayed({
                    loadNextQuestion()
                }, 1000)
            }
        }
    }
}
