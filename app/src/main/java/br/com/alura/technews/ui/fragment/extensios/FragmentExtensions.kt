package br.com.alura.technews.ui.fragment.extensios

import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created by felipebertanha on 26/May/2020
 */


fun Fragment.mostraErro(mensagem: String) {
    Toast.makeText(
        context,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}