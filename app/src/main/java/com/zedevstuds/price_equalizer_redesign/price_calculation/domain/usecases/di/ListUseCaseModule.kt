package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.di

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.AddListUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.DeleteListUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.GetAllListsUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.UpdateListUseCase
import org.koin.dsl.module

val listUseCaseModule = module {
    factory {
        AddListUseCase(productListRepository = get())
    }
    factory {
        DeleteListUseCase(productListRepository = get())
    }
    factory {
        GetAllListsUseCase(productListRepository = get())
    }
    factory {
        UpdateListUseCase(productListRepository = get())
    }
}