package com.adwardstark.dd_mini_assignment.di

import android.content.Context
import co.infinum.retromock.Retromock
import com.adwardstark.dd_mini_assignment.BuildConfig
import com.adwardstark.dd_mini_assignment.network.MockedOrderService
import com.adwardstark.dd_mini_assignment.network.OrderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Aditya Awasthi on 07/09/21.
 * @author github.com/adwardstark
 */
@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    @Named("baseURL")
    fun getBaseURL() = "https://exampleorders.com/v1/"

    @Provides
    @Singleton
    @Named("ProductionOrderService")
    fun provideProductionOrderService(@Named("baseURL") url: String): OrderService {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(OrderService::class.java)
    }

    @Provides
    @Singleton
    @Named("MockedOrderService")
    fun provideMockedOrderService(@Named("baseURL") url: String,
                                  @ApplicationContext context: Context): OrderService {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return Retromock.Builder()
            .retrofit(retrofitInstance)
            .defaultBodyFactory(context.assets::open)
            .build()
            .create(MockedOrderService::class.java)
    }

    @Provides
    @Singleton
    @Named("OrderService")
    fun provideOrderService(@Named("ProductionOrderService") prodService: OrderService,
                            @Named("MockedOrderService") mockedService: OrderService)
    : OrderService {
        return if(BuildConfig.DEBUG) {
            mockedService
        } else {
            prodService
        }
    }
}