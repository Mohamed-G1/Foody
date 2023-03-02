package com.example.foody.justForTesting

import org.junit.Assert.*
import org.junit.Test

class ProductTest {


    // first test
//    Inside the test class you just created, declare a test function for Spaghetti that costs 20.00. The stock amount for this product is equal to 3
    @Test
    fun testSpaghetti (){
        val classUnderTest = Product(
            title = "Spaghetti",
            price = 20.00,
            amount = 3,
        )
        val result = classUnderTest.applyDiscount(20)
        assertEquals("",result)

    }

    @Test
    fun testStack (){
        val classUnderTest = Product(
            title = "Stack",
            price = 30.00,
            amount = 8,
        )
        val result = classUnderTest.applyDiscount(20)
        assertEquals("",result)

    }

    @Test
    fun lasagnaCosting (){
        val classUnderTest = Product(
            title = "Lasagna costing",
            price = 10.00,
            amount = 4,
        )
        val result = classUnderTest.applyDiscount(20)
        assertEquals("",result)

    }


}