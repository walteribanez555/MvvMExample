package com.cursokotlin.mvvmexample.domain

import com.cursokotlin.mvvmexample.data.QuoteRepository
import com.cursokotlin.mvvmexample.domain.model.Quote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetRandomQuoteUseCaseTest {

    @RelaxedMockK
    private lateinit var quoteRepository: QuoteRepository

    lateinit var getRandomQuoteUseCase: GetRandomQuoteUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getRandomQuoteUseCase = GetRandomQuoteUseCase(quoteRepository)
    }



    @Test
    fun `when database is empty then return null`() = runBlocking{


        //Given
        coEvery { quoteRepository.getAllQuotesFromDatabase() } returns emptyList()

        //When
        val response = getRandomQuoteUseCase()

        //Then
        assert(response == null)

    }

    @Test
    fun `When the database is not empty return Quote`() = runBlocking {

        //Given
        val quoteList = listOf(Quote("Hola","Walter"))
        coEvery { quoteRepository.getAllQuotesFromDatabase() }returns quoteList

        //When
        val response = getRandomQuoteUseCase()

        //Then
        assert(response == quoteList.first())


    }

}