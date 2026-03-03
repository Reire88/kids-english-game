package com.example.kidsenglishgame

import kotlin.math.max

object SimilarityUtils {

    /**
     * Medium-forgiving:
     * - ignore a/the
     * - ignore punctuation
     * - allow singular/plural
     * - accept if (soundex matches) OR (levenshtein similarity >= 0.72)
     */
    fun isAcceptable(hypotheses: List<String>, target: String): Boolean {
        val t = normalize(target)
        val t2 = singularize(t)
        val tx = soundex(t2)

        for (h in hypotheses) {
            val n = singularize(normalize(h))
            if (n.isBlank()) continue

            if (n == t2) return true
            if (soundex(n) == tx) return true

            val sim = levenshteinSimilarity(n, t2)
            if (sim >= 0.72) return true
        }
        return false
    }

    private fun normalize(s: String): String {
        var x = s.lowercase()
        x = x.replace(Regex("[^a-z\\s]"), " ")
        x = x.replace(Regex("\\s+"), " ").trim()

        // remove simple articles
        x = x.split(" ").filter { it != "a" && it != "the" }.joinToString(" ").trim()

        // If speech returns multiple words, keep the first "meaningful" one
        // (kids often say "a cow" / "cow please")
        val parts = x.split(" ").filter { it.isNotBlank() }
        return parts.firstOrNull() ?: ""
    }

    private fun singularize(s: String): String {
        if (s.endsWith("s") && s.length > 3) return s.dropLast(1)
        return s
    }

    private fun levenshteinSimilarity(a: String, b: String): Double {
        val d = levenshtein(a, b)
        return 1.0 - d.toDouble() / max(a.length, b.length).toDouble()
    }

    private fun levenshtein(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j

        for (i in 1..a.length) {
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[a.length][b.length]
    }

    // Simple English Soundex (works surprisingly well for accent tolerance)
    private fun soundex(s: String): String {
        if (s.isBlank()) return ""
        val first = s[0]
        val map = mapOf(
            'b' to '1','f' to '1','p' to '1','v' to '1',
            'c' to '2','g' to '2','j' to '2','k' to '2','q' to '2','s' to '2','x' to '2','z' to '2',
            'd' to '3','t' to '3',
            'l' to '4',
            'm' to '5','n' to '5',
            'r' to '6'
        )

        val digits = StringBuilder()
        var prev: Char? = null
        for (ch in s.drop(1)) {
            val d = map[ch] ?: '0'
            if (d != '0' && d != prev) digits.append(d)
            prev = d
        }
        val code = ("" + first.uppercaseChar() + digits.toString()).padEnd(4, '0')
        return code.take(4)
    }
}
