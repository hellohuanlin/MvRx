/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.airbnb.mvrx.todomvrx.data.source.db

import android.support.annotation.VisibleForTesting
import com.airbnb.mvrx.todomvrx.data.Task
import com.airbnb.mvrx.todomvrx.data.Tasks
import com.airbnb.mvrx.todomvrx.data.source.TasksDataSource
import com.airbnb.mvrx.todomvrx.util.AppExecutors
import io.reactivex.Single
import java.util.concurrent.TimeUnit


/**
 * Concrete implementation of a data source as a db.
 */
class TasksLocalDataSource private constructor(
        private val appExecutors: AppExecutors,
        private val tasksDao: TasksDao
) : TasksDataSource {

    override fun getTasks(): Single<Tasks> = tasksDao.getTasks().delay(2000, TimeUnit.MILLISECONDS)


    override fun saveTask(task: Task) {
        appExecutors.diskIO.execute { tasksDao.insertTask(task) }
    }

    override fun setComplete(id: String, complete: Boolean) {
        appExecutors.diskIO.execute { tasksDao.updateCompleted(id, complete) }
    }

    override fun clearCompletedTasks() {
        appExecutors.diskIO.execute { tasksDao.deleteCompletedTasks() }
    }

    override fun refreshTasks() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllTasks() {
        appExecutors.diskIO.execute { tasksDao.deleteTasks() }
    }

    override fun deleteTask(id: String) {
        appExecutors.diskIO.execute { tasksDao.deleteTaskById(id) }
    }

    companion object {
        private var INSTANCE: TasksLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, tasksDao: TasksDao): TasksLocalDataSource {
            if (INSTANCE == null) {
                synchronized(TasksLocalDataSource::javaClass) {
                    INSTANCE = TasksLocalDataSource(appExecutors, tasksDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
