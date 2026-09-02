package com.josegonzalez.jetpackCrypto.data.local.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CryptoDatabaseTest {

    @Test
    fun getInstance_returnsSameInstance() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val instance1 = CryptoDatabase.getInstance(context)
        val instance2 = CryptoDatabase.getInstance(context)

        assertNotNull(instance1)
        assertSame(instance1, instance2)
    }

    @Test
    fun cryptoDao_returnsDao() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = CryptoDatabase.getInstance(context)
        assertNotNull(db.cryptoDao())
    }
}
