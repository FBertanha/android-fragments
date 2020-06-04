package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.transactionFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment
import kotlinx.android.synthetic.main.activity_noticias.*


class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        configuraFragmentPeloEstado(savedInstanceState)
    }

    private fun configuraFragmentPeloEstado(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            transactionFragment {
                add(R.id.activity_noticias_container_primario, ListaNoticiasFragment())
            }
        } else {
            tentaReabrirFragmentVisualizaNoticia()
        }
    }

    private fun tentaReabrirFragmentVisualizaNoticia() {
        supportFragmentManager.findFragmentByTag(VisualizaNoticiaFragment.TAG)?.let {
            val newInstance = VisualizaNoticiaFragment.newInstance(it.arguments)

            removeFragmentVisualizaNoticia(it)

            transactionFragment {
                replace(
                    configuraContainerFragmentVisualizaNoticia(),
                    newInstance,
                    VisualizaNoticiaFragment.TAG
                )
            }

        }
    }

    private fun FragmentTransaction.configuraContainerFragmentVisualizaNoticia(): Int {
        if (activity_noticias_container_secundario != null) {
            return R.id.activity_noticias_container_secundario
        }
        addToBackStack(null)
        return R.id.activity_noticias_container_primario
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is ListaNoticiasFragment -> {
                fragment.onNoticiaSelecionada = this::abreVisualizadorNoticia
                fragment.onNovaNoticiaClicado = this::abreFormularioModoCriacao

            }

            is VisualizaNoticiaFragment -> {
                fragment.onFechar = {
                    supportFragmentManager.findFragmentByTag(VisualizaNoticiaFragment.TAG)?.let {
                        removeFragmentVisualizaNoticia(it)
                    }
                }
                fragment.onEditaNoticia = this::abreFormularioEdicao
            }

        }


    }

    private fun removeFragmentVisualizaNoticia(it: Fragment) {
        transactionFragment {
            remove(it)
        }

        supportFragmentManager.popBackStack()
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
            replace(
                configuraContainerFragmentVisualizaNoticia(),
                VisualizaNoticiaFragment.newInstance(noticia.id),
                VisualizaNoticiaFragment.TAG
            )
        }

    }

}
