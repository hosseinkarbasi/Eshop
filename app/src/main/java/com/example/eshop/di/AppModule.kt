//package com.example.eshop.di
//
//import android.content.Context
//import androidx.room.Room
//import com.example.eshop.data.local.ILocalDataSource
//import com.example.eshop.data.local.LocalDataSource
//import com.example.eshop.data.local.db.AppDataBase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(
//        @ApplicationContext context: Context
//    ): AppDataBase = Room.databaseBuilder(
//        context,
//        AppDataBase::class.java,
//        "product_database"
//    ).fallbackToDestructiveMigration()
//        .build()
//
//    @Provides
//    @Singleton
//    fun provideDao(db: AppDataBase) = db.ProductDao()
//
//    @Provides
//    @Singleton
//    fun provideLocalDataSource(@ApplicationContext context: Context): ILocalDataSource =
//        LocalDataSource(provideDao(provideDatabase(context)))
//
//}