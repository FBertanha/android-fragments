package br.com.alura.technews.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.transactionFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment


class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)

        if (savedInstanceState == null) {
            transactionFragment {
                add(R.id.activity_noticias_container_primario, ListaNoticiasFragment())
            }
        }

    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is ListaNoticiasFragment -> {
                fragment.onNoticiaSelecionada = this::abreVisualizadorNoticia
                fragment.onNovaNoticiaClicado = this::abreFormularioModoCriacao

            }

            is VisualizaNoticiaFragment -> {
                fragment.onFechar = this::finish
                fragment.onRemoveNoticia = this::finish
                fragment.onEditaNoticia = this::abreFormularioEdicao
            }

        }


    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }

    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(noticia: Noticia) {

        transactionFragment {
            val container =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    R.id.activity_noticias_container_secundario
                } else {
                    addToBackStack(null)
                    R.id.activity_noticias_container_primario
                }
            replace(container, VisualizaNoticiaFragment.newInstance(noticia.id))
        }

    }

}
