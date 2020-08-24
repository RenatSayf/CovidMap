package com.test.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.covidmap.R



class BottomDialog : BottomSheetDialogFragment()
{
    companion object
    {
        const val TAG = "bottom_dialog"
    }

    

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val dialogView = inflater.inflate(R.layout.dialog_layout, container)


        return dialogView
    }
}