package com.instant.mvi.data.di

import com.instant.mvi.domain.repository.UserRepository
import com.instant.mvi.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository


//    @Provides
//    @Singleton
//    fun provideNoteRepository(
//        dao: UserDao
//    ): UserRepository {
//        return UserRepositoryImpl(dao)
//    }

}
