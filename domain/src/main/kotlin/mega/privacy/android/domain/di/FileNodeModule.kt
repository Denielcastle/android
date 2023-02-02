package mega.privacy.android.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Domain file node module
 */
@Module(includes = [InternalFileNodeModule::class])
@InstallIn(SingletonComponent::class)
abstract class FileNodeModule