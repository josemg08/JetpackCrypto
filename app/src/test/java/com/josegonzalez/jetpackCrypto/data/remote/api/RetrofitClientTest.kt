package com.josegonzalez.jetpackCrypto.data.remote.api

import org.junit.Assert.assertNotNull
import org.junit.Test

class RetrofitClientTest {

    @Test
    fun `apiService is not null`() {
        assertNotNull(RetrofitClient.apiService)
    }
}
