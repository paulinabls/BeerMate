package com.psc.beermate.data.network;

import android.content.Context;

import com.psc.beermate.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpClientProvider {
    private static final int HTTP_READ_TIMEOUT = 10000;
    private static final int HTTP_CONNECT_TIMEOUT = 6000;
    private static final int CACHE_SIZE_BYTES = 2 * 1024 * 1024;
    private static final int MAX_STALE_SECONDS = 90 * 24 * 3600;
    private static final String CACHE_FILE_NAME = "cacheFile";

    private OkHttpClient createWithCache(Cache cache) {
        return new OkHttpClient().newBuilder()
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(createLoggingInterceptor())
                .addInterceptor(new CacheInterceptor())
                .cache(cache)
                .build();
    }

    private static HttpLoggingInterceptor createLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    public OkHttpClient create(Context applicationContext) {
        File cacheFile = new File(applicationContext.getCacheDir(), CACHE_FILE_NAME);
        Cache cache = new Cache(cacheFile, HttpClientProvider.CACHE_SIZE_BYTES);
        return createWithCache(cache);
    }

    private static class CacheInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .cacheControl(new CacheControl.Builder()
                            .maxAge(365, TimeUnit.DAYS)
                            .maxStale(365, TimeUnit.DAYS)
                            .build())
//                        .header("Cache-Control",
//                                "public, only-if-cached, max-stale=" + MAX_STALE_SECONDS)
                    .build();

            return chain.proceed(request);
        }
    }
}
