package com.felippeneves.tutorial_android_application.networking.questions;

import com.felippeneves.tutorial_android_application.networking.StackoverflowApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchQuestionDetailsEndpoint {

    public interface Listener {
        void onQuestionDetailsFetched(QuestionSchema question);

        void onQuestionDetailsFetchFailed();
    }

    private final StackoverflowApi mStackoverflowApi;

    public FetchQuestionDetailsEndpoint(StackoverflowApi stackoverflowApi) {
        mStackoverflowApi = stackoverflowApi;
    }

    public void fetchQuestionDetails(String questionId, final Listener listener) {
        mStackoverflowApi.fetchQuestionDetails(questionId)
                .enqueue(new Callback<>() {
                             @Override
                             public void onResponse(Call<QuestionDetailsResponseSchema> call, Response<QuestionDetailsResponseSchema> response) {
                                 if (response.isSuccessful()) {
                                     listener.onQuestionDetailsFetched(response.body().getQuestion());
                                 } else {
                                     listener.onQuestionDetailsFetchFailed();
                                 }
                             }

                             @Override
                             public void onFailure(Call<QuestionDetailsResponseSchema> call, Throwable t) {
                                 listener.onQuestionDetailsFetchFailed();
                             }
                         }
                );
    }
}
