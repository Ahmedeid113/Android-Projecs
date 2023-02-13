package com.example.pexwalls2.view.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class DialogFRagment :DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        dialog.apply {
            setTitle("Error")
            setMessage("No Internet Connection !! \nPlease Make Sure You Are Online And Try Again.")
            setNeutralButton("Exit"
            ) { _, _ -> requireActivity().finish() }
        }
        return dialog.create()
    }

    companion object {
        fun getInstance(): DialogFRagment = DialogFRagment()
    }
}