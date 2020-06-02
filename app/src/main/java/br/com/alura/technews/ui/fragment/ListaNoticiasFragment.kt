package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.fragment.extensios.mostraErro
import br.com.alura.technews.ui.recyclerview.adapter.ListaNoticiasAdapter
import br.com.alura.technews.ui.viewmodel.ListaNoticiasViewModel
import kotlinx.android.synthetic.main.fragment_lista_noticias.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by felipebertanha on 26/May/2020
 */

private const val TITULO_APPBAR = "Notícias"

private const val MENSAGEM_FALHA_CARREGAR_NOTICIAS = "Não foi possível carregar as novas notícias"

class ListaNoticiasFragment : BaseFragment() {

    private val adapter by lazy {
        context?.let {
            ListaNoticiasAdapter(context = it)
        } ?: throw RuntimeException("Context cannot be null")
    }
    private val viewModel: ListaNoticiasViewModel by viewModel()

    var onNoticiaSelecionada: (noticia: Noticia) -> Unit = {}
    var onNovaNoticiaClicado: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaNoticias()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_lista_noticias
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraFabAdicionaNoticia()
        activity?.title = TITULO_APPBAR
    }

    private fun configuraFabAdicionaNoticia() {
        fragment_lista_noticias_fab_salva_noticia.setOnClickListener {
            onNovaNoticiaClicado()
        }
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        fragment_lista_noticias_recyclerview.addItemDecoration(divisor)
        fragment_lista_noticias_recyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraAdapter() {
        adapter.quandoItemClicado = onNoticiaSelecionada
    }

    private fun buscaNoticias() {
        viewModel.buscaTodos().observe(this, Observer { resource ->
            resource.dado?.let { adapter.atualiza(it) }
            resource.erro?.let {
                mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
            }
        })
    }
}