package com.alexandr.deadlineapp.di.component

import com.alexandr.deadlineapp.Repository.Database.DAO.DeadlineDAO
import com.alexandr.deadlineapp.di.module.DAOModule
import dagger.Component

@Component(modules = [DAOModule::class])
interface DAOComponent {
    val deadlineDAO: DeadlineDAO
}