package fr.epf.min1.paysss

import fr.epf.min1.paysss.models.Country

class QuestionGenerator(private val countries: List<Country>) {

    fun generateQuestion(): Question {
        val questionType = QuestionType.values().random()

        return when (questionType) {
            QuestionType.CAPITAL_CITY -> generateCapitalCityQuestion()
            QuestionType.CALLING_CODE -> generateCallingCodeQuestion()
            QuestionType.LANGUAGE -> generateLanguageQuestion()
            QuestionType.REGION -> generateRegionQuestion()
        }
    }


    private fun generateCapitalCityQuestion(): Question {
        val country = countries.random()
        val correctAnswer = country.capital ?: "Unknown"
        val wrongAnswers = countries.filter { it.capital != correctAnswer }.shuffled().take(3).map { it.capital ?: "Unknown" }

        return Question(
            questionText = "Quelle est la capitale de ce pays : ${country.name}?",
            correctAnswer = correctAnswer,
            answers = (wrongAnswers + correctAnswer).shuffled()
        )
    }

    private fun generateCallingCodeQuestion(): Question {
        val country = countries.random()
        val correctAnswer = country.callingCodes.firstOrNull() ?: "Unknown"
        val wrongAnswers = countries.filter { it.callingCodes.firstOrNull() != correctAnswer }.shuffled().take(3).map { it.callingCodes.firstOrNull() ?: "Unknown" }

        return Question(
            questionText = "Quel est le code d'appel de ce pays : ${country.name}?",
            correctAnswer = correctAnswer,
            answers = (wrongAnswers + correctAnswer).shuffled()
        )
    }

    private fun generateLanguageQuestion(): Question {
        val country = countries.random()
        val correctAnswer = country.languages.map { it.name }.joinToString(", ")
        val wrongAnswers = countries.filter { it.languages.map { it.name } != country.languages.map { it.name } }
            .shuffled().take(3)
            .map { it.languages.map { it.name }.joinToString(", ") }

        return Question(
            questionText = "Quelle(s) langue(s) parle t'on dans ce pays : ${country.name}?",
            correctAnswer = correctAnswer,
            answers = (wrongAnswers + correctAnswer).shuffled()
        )
    }

    private fun generateRegionQuestion(): Question {
        val country = countries.random()
        val correctAnswer = country.region
        val wrongAnswers = countries.filter { it.region != correctAnswer }.shuffled().take(3).map { it.region }

        return Question(
            questionText = "A quel continent appartient ce pays ${country.name}?",
            correctAnswer = correctAnswer,
            answers = (wrongAnswers + correctAnswer).shuffled()
        )
    }
}

enum class QuestionType {
    CAPITAL_CITY,
    CALLING_CODE,
    LANGUAGE,
    REGION
}

data class Question(
    val questionText: String,
    val correctAnswer: String,
    val answers: List<String>
)
