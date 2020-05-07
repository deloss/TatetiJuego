package com.example.tatetijuego

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val gameController = GameController()
    private lateinit var tablero: Array<Array<Char>>
    private var gameOver = false
    private var turnoJugador1 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarPartida()
        inicializarListeners()
    }

    private fun inicializarPartida() {
        tablero = gameController.nuevoTablero()
        gameOver = false
        turnoJugador1 = true
        for(i in 0 until tableroLayout.childCount) {
            val fila = tableroLayout[i] as LinearLayout
            for(j in 0 until fila.childCount) {
                val ficha = fila[j] as ImageView
                ficha.setImageDrawable(getDrawable(R.drawable.cuadro_vacio))
            }
        }
    }

    private fun inicializarListeners() {
        reiniciarPartida.setOnClickListener {
            inicializarPartida()
        }

        for(i in 0 until tableroLayout.childCount) {
            val fila = tableroLayout[i] as LinearLayout
            for(j in 0 until fila.childCount) {
                val ficha = fila[j] as ImageView
                ficha.setOnClickListener {
                    if(!gameOver && tablero[i][j] == '-') {
                        setFicha(ficha, i, j)
                        val estadoPartida = gameController.estadoPartida(turnoJugador1)
                        if(turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR1_GANO) {
                            showGameOverDialog("El jugador 1 gano!")
                            gameOver = true
                        } else if(!turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR2_GANO) {
                            showGameOverDialog("El jugador 2 gano!")
                            gameOver = true
                        } else if(estadoPartida == GameController.EstadoPartida.EMPATE) {
                            showGameOverDialog("Es un empate!")
                            gameOver = true
                        } else {
                            turnoJugador1 = !turnoJugador1
                        }
                    }
                }
            }
        }
    }

    private fun showGameOverDialog(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(mensaje)
            .setPositiveButton("Jugar de nuevo", { dialog, which -> inicializarPartida() })
            .setNegativeButton("Cancelar", { dialog, which -> dialog.dismiss() })
            .show()
    }

    private fun setFicha(view: ImageView, posicionFila: Int, posicionColumna: Int) {
        if(turnoJugador1) {
            tablero[posicionFila][posicionColumna] = 'O'
            view.setImageDrawable(getDrawable(R.drawable.circulo))
        } else {
            tablero[posicionFila][posicionColumna] = 'X'
            view.setImageDrawable(getDrawable(R.drawable.cruz))
        }
    }

}
