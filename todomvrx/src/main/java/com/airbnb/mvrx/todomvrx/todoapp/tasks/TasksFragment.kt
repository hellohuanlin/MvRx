/*
 * Copyright 2016, The Android Open Source Project
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

package com.airbnb.mvrx.todomvrx.todoapp.tasks

import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.todomvrx.todoapp.R
import com.airbnb.mvrx.todomvrx.todoapp.core.BaseFragment
import com.airbnb.mvrx.todomvrx.todoapp.data.Task
import com.airbnb.mvrx.todomvrx.todoapp.views.header

/**
 * Display a grid of [Task]s. User can choose to view all, active or completed tasks.
 */
class TasksFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setImageResource(R.drawable.ic_add)
    }

    override fun EpoxyController.buildModels() {
        header {
            id("header")
            title("Tasks Fragment")
        }
    }

//    private lateinit var viewDataBinding: TasksFragBinding
//    private lateinit var listAdapter: TasksAdapter
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?): View? {
//        viewDataBinding = TasksFragBinding.inflate(inflater, container, false).apply {
//            viewmodel = (activity as TasksActivity).obtainViewModel()
//        }
//        setHasOptionsMenu(true)
//        return viewDataBinding.root
//    }
//
//    override fun onResume() {
//        super.onResume()
//        viewDataBinding.viewmodel?.start()
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem) =
//            when (item.itemId) {
//                R.id.menu_clear -> {
//                    viewDataBinding.viewmodel?.clearCompletedTasks()
//                    true
//                }
//                R.id.menu_filter -> {
//                    showFilteringPopUpMenu()
//                    true
//                }
//                R.id.menu_refresh -> {
//                    viewDataBinding.viewmodel?.loadTasks(true)
//                    true
//                }
//                else -> false
//            }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.tasks_fragment_menu, menu)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewDataBinding.viewmodel?.let {
//            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
//        }
//        setupFab()
//        setupListAdapter()
//        setupRefreshLayout()
//    }
//
//    private fun showFilteringPopUpMenu() {
//        PopupMenu(context, activity.findViewById<View>(R.id.menu_filter)).run {
//            menuInflater.inflate(R.menu.filter_tasks, menu)
//
//            setOnMenuItemClickListener {
//                viewDataBinding.viewmodel?.run {
//                    currentFiltering =
//                            when (it.itemId) {
//                                R.id.active -> TasksFilterType.ACTIVE_TASKS
//                                R.id.completed -> TasksFilterType.COMPLETED_TASKS
//                                else -> TasksFilterType.ALL_TASKS
//                            }
//                    loadTasks(false)
//                }
//                true
//            }
//            show()
//        }
//    }
//
//    private fun setupFab() {
//        activity.findViewById<FloatingActionButton>(R.id.fab_add_task).run {
//            setImageResource(R.drawable.ic_add)
//            setOnClickListener {
//                viewDataBinding.viewmodel?.addNewTask()
//            }
//        }
//    }
//
//    private fun setupListAdapter() {
//        val viewModel = viewDataBinding.viewmodel
//        if (viewModel != null) {
//            listAdapter = TasksAdapter(ArrayList(0), viewModel)
//            viewDataBinding.tasksList.adapter = listAdapter
//        } else {
//            Log.w(TAG, "ViewModel not initialized when attempting to set up adapter.")
//        }
//    }
//
//    private fun setupRefreshLayout() {
//        viewDataBinding.refreshLayout.run {
//            setColorSchemeColors(
//                    ContextCompat.getColor(activity, R.color.colorPrimary),
//                    ContextCompat.getColor(activity, R.color.colorAccent),
//                    ContextCompat.getColor(activity, R.color.colorPrimaryDark)
//            )
//            // Set the scrolling view in the custom SwipeRefreshLayout.
//            scrollUpChild = viewDataBinding.tasksList
//        }
//    }

    companion object {
        fun newInstance() = TasksFragment()
        private const val TAG = "TasksFragment"

    }
}
