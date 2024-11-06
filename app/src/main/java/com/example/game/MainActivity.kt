package com.example.game

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.game.R
import com.example.game.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var solde = 100

    // Tableau des images
    private val images = arrayOf(
        R.drawable.banane, R.drawable.charbon, R.drawable.diamant,
        R.drawable.emeraude, R.drawable.fsa, R.drawable.img7,
        R.drawable.piece, R.drawable.rubis, R.drawable.sacargent,
        R.drawable.tresor
    )

    // Définir le code secret
    private val secretCode = "1234" // Code secret, vous pouvez le modifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSolde.text = "Solde: $solde$"

        // Listener pour le bouton JOUER
        binding.btnJouer.setOnClickListener {
            jouer()
        }

        // Listener pour le bouton de validation du code secret
        binding.btnValidateCode.setOnClickListener {
            val enteredCode = binding.etCodeSecret.text.toString()
            if (enteredCode == secretCode) {
                ajouter100Dollars()
            } else {
                Toast.makeText(this, "Code secret incorrect.", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener pour ajouter 100$ via le code secret
        binding.etCodeSecret.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ajouter100Dollars()
                true
            } else {
                false
            }
        }
    }

    private fun jouer() {
        if (solde <= 0) {
            Toast.makeText(this, "Vous n'avez plus de solde !", Toast.LENGTH_SHORT).show()
            return
        }

        val mise = when {
            binding.rb1.isChecked -> 1
            binding.rb2.isChecked -> 2
            binding.rb5.isChecked -> 5
            else -> 1
        }

        if (solde < mise) {
            Toast.makeText(this, "Solde insuffisant pour cette mise.", Toast.LENGTH_SHORT).show()
            return
        }

        // Déduire la mise du solde
        solde -= mise

        // Changer les images de façon aléatoire
        val img1 = images.random()
        val img2 = images.random()
        val img3 = images.random()

        binding.image1.setImageResource(img1)
        binding.image2.setImageResource(img2)
        binding.image3.setImageResource(img3)

        // Logique pour calculer les gains
        val gain = calculerGain(img1, img2, img3, mise)
        solde += gain
        binding.tvSolde.text = "Solde: $solde$"

        if (gain > 0) {
            Toast.makeText(this, "Vous avez gagné $gain$ !", Toast.LENGTH_SHORT).show()
        }

        // Désactiver le bouton JOUER si le solde est insuffisant
        binding.btnJouer.isEnabled = solde > 0
    }

    private fun calculerGain(img1: Int, img2: Int, img3: Int, mise: Int): Int {
        val modeCasseCou = binding.cbCasseCou.isChecked

        return if (modeCasseCou) {
            // Logique pour le mode Casse-cou
            if (img1 == img2 && img2 == img3 && img3 == R.drawable.fsa) {
                mise * 100 // Exemple: 100x la mise si les trois images sont "fsa"
            } else if ((img1 == R.drawable.fsa && img2 == R.drawable.fsa) ||
                (img2 == R.drawable.fsa && img3 == R.drawable.fsa) ||
                (img1 == R.drawable.fsa && img3 == R.drawable.fsa)) {
                mise * 10 // Exemple: 10x la mise si deux images sont "fsa"
            } else {
                0
            }
        } else {
            // Logique pour le mode Normal
            if (img1 == img2 && img2 == img3) {
                mise * 25 // Exemple: 25x la mise si les trois images sont identiques
            } else if (img1 == img2 || img2 == img3 || img1 == img3) {
                mise * 1 // Exemple: 1x la mise si deux images sont identiques
            } else {
                0
            }
        }
    }

    private fun ajouter100Dollars() {
        solde += 100
        binding.tvSolde.text = "Solde: $solde$"
        binding.etCodeSecret.text.clear() // Effacer le champ après ajout
        Toast.makeText(this, "100$ ajoutés au solde!", Toast.LENGTH_SHORT).show()
    }
}
