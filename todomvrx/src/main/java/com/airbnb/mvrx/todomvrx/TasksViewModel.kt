package com.airbnb.mvrx.todomvrx

import android.support.v4.app.FragmentActivity
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.todomvrx.core.MvRxViewModel
import com.airbnb.mvrx.todomvrx.data.Task
import com.airbnb.mvrx.todomvrx.data.Tasks
import com.airbnb.mvrx.todomvrx.data.findTask
import com.airbnb.mvrx.todomvrx.data.source.TasksDataSource
import com.airbnb.mvrx.todomvrx.data.source.db.DatabaseDataSource
import com.airbnb.mvrx.todomvrx.data.source.db.ToDoDatabase
import io.reactivex.Single

data class TasksState(
        val tasks: Tasks = emptyList(),
        val taskRequest: Async<Tasks> = Uninitialized,
        val isLoading: Boolean = false,
        val lastEditedTask: String? = null
) : MvRxState

class TasksViewModel(override val initialState: TasksState, private val sources: List<TasksDataSource>) : MvRxViewModel<TasksState>() {

    init {
        logStateChanges()
        refreshTasks()
    }

    fun refreshTasks() {
        Single.concat(sources.map { it.getTasks() })
                .toObservable()
                .doOnSubscribe { setState { copy(isLoading = true) } }
                .doOnComplete { setState { copy(isLoading = false) } }
                .execute { copy(taskRequest = it, tasks = it() ?: tasks, lastEditedTask = null) }
    }

    fun saveTask(task: Task) {
        setState {
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index >= 0) {
                copy(tasks = tasks.copy(index, task), lastEditedTask = task.id)
            } else {
                copy(tasks = tasks + task, lastEditedTask = task.id)
            }
        }
        sources.forEach { it.saveTask(task) }
    }

    fun setComplete(id: String, complete: Boolean) {
        setState {
            tasks.findTask(id)?.let { task ->
                copy(tasks = tasks.copy(tasks.indexOf(task), task.copy(complete = complete)), lastEditedTask = id)
            } ?: this

        }
        sources.forEach { it.setComplete(id, complete) }
    }

    fun clearCompletedTasks() = setState {
        sources.forEach { it.clearCompletedTasks() }
        copy(tasks = tasks.filter { !it.complete }, lastEditedTask = null)
    }

    fun deleteTask(id: String) {
        setState { copy(tasks = tasks.delete { it.id == id }, lastEditedTask = id) }
        sources.forEach { it.deleteTask(id) }
    }

    private fun <T> List<T>.copy(i: Int, value: T): List<T> = toMutableList().apply { set(i, value) }

    private inline fun <T> List<T>.delete(filter: (T) -> Boolean): List<T> = toMutableList().apply { removeAt(indexOfFirst(filter)) }

    companion object : MvRxViewModelFactory<TasksState> {
        override fun create(activity: FragmentActivity, state: TasksState): BaseMvRxViewModel<TasksState> {
            val database = ToDoDatabase.getInstance(activity)
            val dataSource = DatabaseDataSource(database.taskDao())
            return TasksViewModel(state, listOf(dataSource/*, TasksRemoteDataSource*/))
        }

    }
}

