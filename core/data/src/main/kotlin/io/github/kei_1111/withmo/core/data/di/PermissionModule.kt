package io.github.kei_1111.withmo.core.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.core.data.permission.PermissionCheckerImpl
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PermissionModule {
    @Provides
    @Singleton
    fun providePermissionChecker(
        @ApplicationContext context: Context,
    ): PermissionChecker = PermissionCheckerImpl(context)
}
