package com.zedevstuds.price_equalizer.price_calculation.ui.drawer

import android.content.Context
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list.AddListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list.DeleteListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list.GetAllListsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DrawerViewModel(
    getAllListsUseCase: GetAllListsUseCase,
    private val addListUseCase: AddListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
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

    suspend fun deleteCurrentProductList() {
        deleteListUseCase.execute(selectedItem.value)
    }

    fun addList(listName: String) {
        scope.launch {
            addListUseCase.execute(listName)
        }
    }

    private fun setSelectedItem(totalList: List<ListModel>) {
        previousListSize?.let { listSize ->
            when {
                totalList.size > listSize -> selectedItem.value = totalList.last()
                totalList.size < listSize -> selectedItem.value = totalList.first()
            }
        }
        previousListSize = totalList.size
    }

    companion object {
        const val DEFAULT_LIST_ID = -1
    }
}