package com.zedevstuds.price_equalizer.price_calculation.ui.drawer

import android.content.Context
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list.GetAllListsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class DrawerViewModel(
    getAllListsUseCase: GetAllListsUseCase,
    context: Context,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
) {

    private val defaultList = ListModel(
        id = DEFAULT_LIST_ID,
        name = context.getString(R.string.default_list_title)
    )

    val selectedItem = MutableStateFlow(defaultList)

    val listsOfProducts = getAllListsUseCase.execute().map {
        (listOf(defaultList) + it).also { totalList ->
            setSelectedItem(totalList)
        }
    }

    private var previousListSize: Int? = null

    fun onListClicked(listModel: ListModel) {
        selectedItem.value = listModel
    }

    private fun setSelectedItem(totalList: List<ListModel>) {
        previousListSize?.let { listSize ->
            when {
                totalList.size > listSize -> selectedItem.value = totalList.last()
                totalList.size < listSize -> selectedItem.value = totalList.first()
                else -> selectedItem.value = totalList.first { it.id == selectedItem.value.id }
            }
        }
        previousListSize = totalList.size
    }

    companion object {
        const val DEFAULT_LIST_ID = -1
    }
}