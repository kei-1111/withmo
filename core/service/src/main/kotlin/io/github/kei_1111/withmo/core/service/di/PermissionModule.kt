package io.github.kei_1111.withmo.core.service.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.service.permission.PermissionCheckerImpl
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
