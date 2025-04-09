package com.example.domain.usecases.news

interface GetSourcesUseCase {
    suspend operator fun invoke(): Result<List<Pair<String, String>>>
}