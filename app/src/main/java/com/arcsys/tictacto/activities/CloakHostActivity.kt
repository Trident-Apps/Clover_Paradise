package com.arcsys.tictacto.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arcsys.tictacto.R
import com.arcsys.tictacto.databinding.CloakHostAcLayoutBinding

class CloakHostActivity : AppCompatActivity() {
    private lateinit var viewBinding: CloakHostAcLayoutBinding
    private var playerScore = 0
    private var competitorScore = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = CloakHostAcLayoutBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.tossButton.setOnClickListener {
            coinFlip(viewBinding.p1Coin)
            coinFlip(viewBinding.p2Coin)
        }
        viewBinding.playAgainButton.setOnClickListener {
            playAgain()
        }
    }

    private fun coinFlip(iv: ImageView) {
        if (playerScore == 10 || competitorScore == 10) {
            endGame()
        }
        iv.animate().apply {
            duration = 2000L
            rotationXBy(1080F)
        }.withEndAction {
            when (iv) {
                viewBinding.p1Coin -> {
                    if (randomize(listOf(1, 2))[0] == 1) {
                        playerScore++
                        viewBinding.playerScore.text = "Your score $playerScore/10"
                        iv.setImageResource(R.drawable.coin_tale)
                    } else {
                        iv.setImageResource(R.drawable.coin_head)
                    }
                }
                viewBinding.p2Coin -> {
                    if (randomize(listOf(1, 2))[0] == 1) {
                        competitorScore++
                        viewBinding.competitorScore.text = "Opponent's score $competitorScore/10"
                        iv.setImageResource(R.drawable.coin_tale)
                    } else {
                        iv.setImageResource(R.drawable.coin_head)
                    }
                }
            }
        }
    }

    private fun playAgain() {
        playerScore = 0
        competitorScore = 0
        with(viewBinding) {
            playerScore.visibility = View.VISIBLE
            playerScore.text = getString(R.string.your_score_0_10)
            competitorScore.visibility = View.VISIBLE
            competitorScore.text = getString(R.string.opponent_s_score_0_10)
            playerIv.visibility = View.VISIBLE
            player2Iv.visibility = View.VISIBLE
            p1Coin.visibility = View.VISIBLE
            p2Coin.visibility = View.VISIBLE
            tossButton.visibility = View.VISIBLE
            gameTitle.visibility = View.VISIBLE
            playAgainButton.visibility = View.INVISIBLE
            result.visibility = View.INVISIBLE
        }
    }

    private fun endGame() {
        hideGame()
        val result = viewBinding.result
        if (playerScore == 10) {
            showResult("Congrats, you won!", result)
        } else if (competitorScore == 10) {
            showResult("Better luck next time!", result)
        } else if (playerScore == 10 && competitorScore == 10) {
            showResult("It's a draw!", result)
        }
    }

    private fun showResult(text: String, textView: TextView) {
        textView.text = text
    }

    private fun hideGame() {
        with(viewBinding) {
            playerScore.visibility = View.INVISIBLE
            playerIv.visibility = View.INVISIBLE
            player2Iv.visibility = View.INVISIBLE
            competitorScore.visibility = View.INVISIBLE
            tossButton.visibility = View.INVISIBLE
            gameTitle.visibility = View.INVISIBLE
            p1Coin.visibility = View.INVISIBLE
            p2Coin.visibility = View.INVISIBLE
            playAgainButton.visibility = View.VISIBLE
            result.visibility = View.VISIBLE
        }
    }

    private fun randomize(list: List<Int>): List<Int> {
        return list.shuffled()
    }

    override fun onBackPressed() {
        //TODO
    }
}