package com.test.digitec.presentation.setup.di

import com.test.digitec.domain.usecase.SortUseCase
import com.test.digitec.presentation.mapper.PresentationModelMapper
import com.test.digitec.presentation.view.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        MainViewModel(context = get(), mapper = get(), sortUseCase = get())
    }
    factory { PresentationModelMapper(context = get()) }
    factory { SortUseCase(context = get()) }
}