package com.example.onetapauth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.onetapauth.data.remote.KtorApi
import com.example.onetapauth.data.repository.DataStoreOperationsImpl
import com.example.onetapauth.data.repository.RepositoryImpl
import com.example.onetapauth.domain.repository.DataStoreOperations
import com.example.onetapauth.domain.repository.Repository
import com.example.onetapauth.util.Constants.PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesDataStorePreferences(
        @ApplicationContext context : Context
    ) : DataStore<Preferences>{
        return PreferenceDataStoreFactory.create(
            produceFile = {context.preferencesDataStoreFile(PREFERENCES_NAME)}
        )
    }

    @Provides
    @Singleton
    fun providesDataStoreOperations(
        dataStore: DataStore<Preferences>
    ) : DataStoreOperations{
        return DataStoreOperationsImpl(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideRepository(
        dataStoreOperations: DataStoreOperations,
        ktorApi: KtorApi
    ) : Repository{
        return RepositoryImpl(
            dataStoreOperations = dataStoreOperations,
            ktorApi = ktorApi
        )
    }

}