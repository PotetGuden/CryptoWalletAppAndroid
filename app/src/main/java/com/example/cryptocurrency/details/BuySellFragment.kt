package com.example.cryptocurrency.details


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import java.util.ArrayList;
import com.anychart.data.Set
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.enums.Anchor
import com.anychart.enums.TooltipPositionMode
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentCurrencyBinding
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue


class BuySellFragment() : Fragment(R.layout.fragment_currency){

    private lateinit var binding: FragmentCurrencyBinding

    private val viewModel: TransactionsListViewModel by viewModels()
    private val currencyListViewModel: MainViewModel by viewModels()

    private fun updateScreen(){
        val coinId: String? = arguments?.getString("coinId")
        val coinSymbol: String? = arguments?.getString("coinSymbol")
        val coinPrice: String? = arguments?.getString("coinPrice")

        if(coinSymbol == null || coinPrice == null || coinId == null){
            showError()
        } else {
            currencyListViewModel.LoadCoinByName(coinId)
            var updatedPrice : String = coinPrice



            //val correctPriceFormat: String = "$" + updatedPrice!!.substring(0, coinPrice.indexOf(".") + 3)  // BIG DECIMAL

            viewModel.init(requireContext())

            viewModel.fetchSumBalance()
            viewModel.sumBalance.observe(viewLifecycleOwner){
                binding.buyButton.isEnabled = it <= 0F
            }

            viewModel.fetchAmountOfCoinsByName(coinSymbol)
            viewModel.sumAmountOfCoinsByNameLiveData.observe(viewLifecycleOwner) { amountOfCoins ->
                binding.sellButton.isEnabled = amountOfCoins != 0F

                currencyListViewModel.specificCoin.observe(viewLifecycleOwner) {
                    updatedPrice = it.data.priceUsd
                    val correctPriceFormat = BigDecimal(updatedPrice.toDouble()).setScale(2,RoundingMode.HALF_EVEN)

                    val value: Float = amountOfCoins * updatedPrice.toFloat()
                    val valueFormatted = BigDecimal(value.toDouble()).setScale(2, RoundingMode.HALF_EVEN)

                    val amountOfCoinsFormatted = if (amountOfCoins.absoluteValue % 1.0 <= 0.01) {
                        BigDecimal(amountOfCoins.toDouble()).setScale(4, RoundingMode.HALF_EVEN) // If the amount has more then 2 zero's (0.00xx)
                    } else {
                        BigDecimal(amountOfCoins.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                    }
                    binding.balanceMessage.text = "You have ${amountOfCoinsFormatted} ${coinSymbol}\n${amountOfCoinsFormatted} x ${correctPriceFormat}\nValue ${valueFormatted} USD"
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrencyBinding.bind(view)

        val coinId: String? = arguments?.getString("coinId")
        val coinSymbol: String? = arguments?.getString("coinSymbol")
        val coinName: String? = arguments?.getString("coinName")
        val coinPrice: String? = arguments?.getString("coinPrice")

        if(coinName == null || coinSymbol == null || coinPrice == null || coinId == null){
            showError()
        } else {
            updateScreen()

            binding.buyButton.setOnClickListener{
                parentFragmentManager.beginTransaction().apply{
                    replace(
                        R.id.purchase_fragment_container, BuyCurrencyFragment.newInstance(
                            coinId,
                            coinName,
                            coinSymbol,
                            coinPrice
                        )
                    )
                        .addToBackStack("Currency")
                        .commit()
                }
            }

            binding.sellButton.setOnClickListener{
                parentFragmentManager.beginTransaction().apply{
                    replace(
                        R.id.purchase_fragment_container, SellCurrencyFragment.newInstance(
                            coinSymbol,
                            coinPrice,
                            coinId
                        )
                    )
                        .addToBackStack("Currency")
                        .commit()
                }
            }
            loadGraph(coinId, coinName)
        }
    }

    private fun loadGraph(coinId: String, coinName: String){
        val anyChartView: AnyChartView = binding.anyChartView
        anyChartView.setProgressBar(binding.progressBar)

        val cartesian: Cartesian = AnyChart.line()

        cartesian.animation(true)  // draw animation, set false or delete this line to remove it
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("${coinName} last 60 days")

        cartesian.yAxis(0).title("")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        // Observing chart data from API and passing it to Any Chart
        currencyListViewModel.LoadChartById(coinId)
        currencyListViewModel.specificChart.observe(viewLifecycleOwner){    chartApiData ->
            val seriesData: MutableList<DataEntry> = ArrayList()

            for(i in chartApiData.data.size-60 until chartApiData.data.size){ // Get the x latest data
                val date = chartApiData.data[i].date.substring(5,10)
                val priceUsd : Number = chartApiData.data[i].priceUsd.toFloat()
                seriesData.add(CustomDataEntry(date, priceUsd))
            }

            val set: Set = Set.instantiate()
            set.data(seriesData)
            val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")

            val seriesCurrency: Line = cartesian.line(series1Mapping)
            seriesCurrency.name("$")
            seriesCurrency.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5.0)
                .offsetY(5.0)
            seriesCurrency.color("#4169E1")

            anyChartView.setChart(cartesian)
        }
    }

    private class CustomDataEntry constructor(
        x: String?,
        value: Number?
    ) :
        ValueDataEntry(x, value) {
    }

    private fun showError() {
        Log.d("BuySellFragment", "One of the values from newInstance is null")
    }

    companion object {
        fun newInstance(
            coinName: String?,
            coinSymbol: String?,
            coinPrice: String?,
            coinId: String?
        ): BuySellFragment = BuySellFragment().apply{
            arguments = Bundle().apply{
                putString("coinName", coinName)
                putString("coinSymbol", coinSymbol)
                putString("coinPrice", coinPrice)
                putString("coinId", coinId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        currencyListViewModel.LoadCoinFromList()
        updateScreen()

        Log.d("BuySellFragment", "onResume")
    }
}