package com.psplog.whereisraspberry.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.psplog.whereisraspberry.R
import kotlinx.android.synthetic.main.dialog_search.view.*

class Dialog(val listener: Search) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_search, null)
            builder.setView(initView(view))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initView(view: View): View {

        return view.apply {
            this.btn_search.setOnClickListener {
                var decibel = 0
                if (!et_decibel.text.toString().isNullOrBlank())
                    decibel = et_decibel.text.toString().toInt()
                listener.onClickSearch(et_tag.text.toString(), decibel)
                dismiss()
            }
        }
    }

    interface Search {
        fun onClickSearch(tag: String, decibel: Int)
    }
}