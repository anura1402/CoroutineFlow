package ru.anura.coroutineflow.lessons.crypto_app_lesson_4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import ru.anura.coroutineflow.databinding.ActivityCryptoBinding

class CryptoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCryptoBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CryptoViewModel::class.java]
    }

    private val adapter = CryptoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCurrencyPriceList.adapter = adapter
        binding.recyclerViewCurrencyPriceList.itemAnimator = null
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            //первый способ остановки загрузки после сворачивания приложения
            //repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state
                    //новый flow
                    .transform {
                        Log.d("CryptoViewModel", "Transform")
                        delay(10_000)
                        emit(it)
                    }
                    //второй способ остановки загрузки после сворачивания приложения
                    //но действует только на то, что выше
                    //если будет новый flow ниже, то он не будет останавливаться
                    .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                    .collect {
                        when (it) {
                            is State.Initial -> {
                                binding.progressBarLoading.isVisible = false
                            }

                            is State.Loading -> {
                                binding.progressBarLoading.isVisible = true
                            }

                            is State.Content -> {
                                binding.progressBarLoading.isVisible = false
                                adapter.submitList(it.currencyList)
                            }
                        }
                    }
            //}

        }

    }

    companion object {

        fun newIntent(context: Context) = Intent(context, CryptoActivity::class.java)
    }
}
