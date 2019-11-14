package com.maangata.fillit_tionary.Interfaces

import android.database.Cursor
import com.maangata.fillit_tionary.Model.Mot
import com.maangata.fillit_tionary.Model.MotsList

interface MainContract {

    interface Langues {
        interface ViewCallback {
            // Aquí van los SETTERS.
            fun setLanguesArray(mLangues: ArrayList<String>)
        }

        interface ModelCallback {
            // AQUÍ VAN LOS GETTERS.
            fun getLangues(listener: OnFinishedLoadingLanguesListener)

            interface OnFinishedLoadingLanguesListener {
                fun onFinished(mList: ArrayList<String>)
            }
        }

        interface PresenterCallback {
            // AQUÍ VAN LAS INTERACCIONES CON LAS VISTAS
            fun onRequestingLangues()
        }
    }

    interface AllMots {
        interface ViewCallback {
            fun setTheMotList(mCursor: Cursor)
        }

        interface ModelCallback{
            fun getTheMotsList(listener: OnFinishedLoadingTheMotsList, mLangue: String)

            interface OnFinishedLoadingTheMotsList{
                fun onFinished(mCursor: Cursor)
            }
        }

        interface PresenterCallback {
             fun onRequestingAllMots(langue: String)
        }
    }

    interface CloserLook {
        interface ViewCallback {
            fun setTheMot(mMot: Mot)
        }

        interface ModelCallback {
            fun getTheMot(listener: OnFinishedLoadingMot, id: Long)

            fun updateSound(listener: OnFinishedLoadingMot, mSound: Mot, id: Long)

            interface OnFinishedLoadingMot {
                fun onFinished(mMot: Mot)
            }
        }

        interface PresenterCallback {
            fun OnRequestMot(mot: Mot?, id: Long)

            fun OnUpdateSound(mMot: Mot, id: Long)
        }
    }

    interface MotToEdit {
        interface ViewCallBack {
            fun setTheMot(mMotsList: MotsList)

            fun onFinishedSaving()

            fun onFinishedDeleting()
        }

        interface ModelCallback {
            fun getTheMot(listener: OnFinishedLoadingMotToEdit, id: Long, newMot: Boolean)

            fun deleteMot(listener: OnFinishedDeletingMot, id: Long)

            fun saveTheMot(listener: OnFinishedSavingMot, id: Long, newMot: Boolean)

            interface OnFinishedLoadingMotToEdit {
                fun onFinishedMot(mMotsList: MotsList)
            }

            interface OnFinishedSavingMot {
                fun onFinishedSavingMot()
            }

            interface OnFinishedDeletingMot {
                fun onFinishedDeletingMot()
            }
        }

        interface PresenterCallBack {
            fun OnRequestMotToEdit(id: Long, newMot: Boolean)

            fun OnRequestDelete(id: Long)

            fun OnRequestSaveMot(id: Long, newMot: Boolean)
        }
    }

}