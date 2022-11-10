package com.cursokotlin.mvvmexample.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cursokotlin.mvvmexample.domain.GetQuotesUseCase
import com.cursokotlin.mvvmexample.domain.GetRandomQuoteUseCase
import com.cursokotlin.mvvmexample.domain.model.Quote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class QuoteViewModelTest  {

    @RelaxedMockK
    private lateinit var  getQuotesUseCase: GetQuotesUseCase

    @RelaxedMockK
    private  lateinit var getRandomQuoteUseCase: GetRandomQuoteUseCase

    private lateinit var quoteViewModel: QuoteViewModel

    @get:Rule
    var rule:InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore(){

        MockKAnnotations.init(this)
        quoteViewModel = QuoteViewModel(getQuotesUseCase, getRandomQuoteUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter(){
        Dispatchers.resetMain()
    }

    @Test
    fun `When viewmodel is created at the first time, get all quotes and set the first value`() = runTest {
        //Given
        val quoteList = listOf(Quote("Hola", "Mishu"), Quote("Aloha", "Neco"))
        coEvery { getQuotesUseCase()} returns  quoteList

        //When
        quoteViewModel.onCreate()

        //Then
        assert(quoteViewModel.quoteModel.value == quoteList.first())


    }

    @Test
    fun `when randomQuoteUseCase return a quote set on the livedata`() = runTest {
        //Given
        val quote = Quote("Ejemplos no tengo", "Walter")
        coEvery { getRandomQuoteUseCase() } returns  quote


        //When
        quoteViewModel.randomQuote()

        //Then
        assert(quoteViewModel.quoteModel.value == quote)
    }

    @Test
    fun `if randomQuoteUseCase return null keep the last value`()= runTest{
        //Given
        val quote = Quote("Ejemplos no tengo", "Walter")
        quoteViewModel.quoteModel.value  = quote
        coEvery { getRandomQuoteUseCase()  }returns null
        //when
        quoteViewModel.randomQuote()

        //Then
        assert(quoteViewModel.quoteModel.value== quote)

    }

}