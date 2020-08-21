package com.neona.todolist.main_screen.item_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.neona.todolist.database.ShoppingDatabaseDao
import com.neona.todolist.database.ShoppingItem
import kotlinx.coroutines.*

class ItemListViewModel(dataSource: ShoppingDatabaseDao, application: Application) : ViewModel() {

    private val database: ShoppingDatabaseDao = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val items = dataSource.getAllRows()

    // insert item
    fun onNewItemInsert(itemName: String) {
        uiScope.launch {
            val item = ShoppingItem()
            item.itemName = itemName
            insertOneItem(item)
        }
    }

    private suspend fun insertOneItem(item: ShoppingItem) {
        withContext(Dispatchers.IO) {
            database.insert(item)
        }
    }

    // update isBought
    fun onUpdateIsBought(isBought:Boolean, id:Long){
        uiScope.launch {
            updateIsBought(isBought,id)
        }
    }

    private suspend fun updateIsBought(isBought:Boolean, id:Long){
        database.updateIsBought(isBought,id)
    }

    private suspend fun getItems(dataSource: ShoppingDatabaseDao): LiveData<List<ShoppingItem>> {

        return withContext(Dispatchers.IO) {
            val itemList = dataSource.getAllRows()
            itemList
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}