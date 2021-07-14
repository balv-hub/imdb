package com.balv.imdb.di;


import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.balv.imdb.data.Constant;
import com.balv.imdb.data.local.AppDb;
import com.balv.imdb.data.mapper.Mapper;
import com.balv.imdb.data.network.ApiService;
import com.balv.imdb.data.repository.AppRepo;
import com.balv.imdb.domain.repositories.IMovieRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Singleton
    @Provides
    public HttpLoggingInterceptor providesHttpLoggingInterceptor()  {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }


    @Provides
    @Singleton
    public Retrofit providesRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constant.DOMAIN)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public ApiService providesApiService(Retrofit retrofit) {
        Log.i("providesApiService", "providesApiService: ");
        return retrofit.create(ApiService.class);
    }

    @Singleton
    @Provides
    public AppDb providesAppDatabase(Application application) {
        return Room.databaseBuilder(application, AppDb.class, "imdb")
            .fallbackToDestructiveMigration()
                .build();
    }
    @Provides
    @Singleton
    IMovieRepository providesMovieRepository(AppDb db, ApiService apiService, Mapper mapper) {
        return new AppRepo(db, apiService, mapper);
    }
}
