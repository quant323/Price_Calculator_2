package com.zedevstuds.price_equalizer.price_calculation.ui

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list.GetAllListsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DrawerViewModel(
    getAllListsUseCase: GetAllListsUseCase,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
) {

    val listOfProduct = getAllListsUseCase.execute()
    val events = MutableSharedFlow<DrawerEvent>()

    fun onListClicked(listModel: ListModel) {
        scope.launch {
            events.emit(DrawerEvent.OnListClickEvent(listModel))
        }
    }

    sealed class DrawerEvent {
        class OnListClickEvent(val listModel: ListModel) : DrawerEvent()
    }
}