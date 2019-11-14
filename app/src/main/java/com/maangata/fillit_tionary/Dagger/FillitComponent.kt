package com.maangata.fillit_tionary.Dagger

import android.app.Application
import com.maangata.fillit_tionary.Activities.EditActivity
import com.maangata.fillit_tionary.Activities.EditActivityForLangue
import com.maangata.fillit_tionary.Activities.MainActivity
import com.maangata.fillit_tionary.Activities.PreMainActivity
import com.maangata.fillit_tionary.Interfaces.MainContract
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = arrayOf(FillitDatabaseModule::class))
interface FillitComponent {

    @Component.Builder
    interface Builder {
        // provide Application instance into DI
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): FillitComponent
    }

    fun injectDatabase(activity: PreMainActivity)
    fun injectDatabase(activity: MainActivity)
    fun injectDatabase(activity: MainContract.CloserLook)
    fun injectDatabase(activity: EditActivity)
    fun injectDatabase(activity: EditActivityForLangue)

}