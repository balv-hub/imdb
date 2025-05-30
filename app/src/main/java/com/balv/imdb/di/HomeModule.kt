package com.balv.imdb.di

import androidx.fragment.app.Fragment
import com.balv.imdb.ui.home.listener.IHomeItemClickListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class HomeModule {
    @Provides
    fun provideHomeItemClickListener(fragment: Fragment?): IHomeItemClickListener? {
        return fragment as IHomeItemClickListener?
    }
}
