package com.felippeneves.tutorial_android_application.common.di;

import com.felippeneves.tutorial_android_application.common.Constants;
import com.felippeneves.tutorial_android_application.common.time.TimeProvider;
import com.felippeneves.tutorial_android_application.networking.StackoverflowApi;
import com.felippeneves.tutorial_android_application.networking.questions.FetchQuestionDetailsEndpoint;
import com.felippeneves.tutorial_android_application.questions.FetchQuestionDetailsUseCase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompositionRoot {

    private Retrofit mRetrofit;
    private FetchQuestionDetailsUseCase mFetchQuestionDetailsUseCase;

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public StackoverflowApi getStackoverflowApi() {
        return getRetrofit().create(StackoverflowApi.class);
    }

    public TimeProvider getTimeProvider() {
        return new TimeProvider();
    }

    private FetchQuestionDetailsEndpoint getFetchQuestionDetailsEndpoint() {
        return new FetchQuestionDetailsEndpoint(getStackoverflowApi());
    }

    public FetchQuestionDetailsUseCase getFetchQuestionDetailsUseCase() {
        if (mFetchQuestionDetailsUseCase == null) {
            mFetchQuestionDetailsUseCase = new FetchQuestionDetailsUseCase(getFetchQuestionDetailsEndpoint(), getTimeProvider());
        }
        return mFetchQuestionDetailsUseCase;
    }
}
