package br.com.equipequatro.traveltips.util


import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import br.com.equipequatro.traveltips.R

class LoadingDialog(val atvd: FragmentActivity?) {

    lateinit var dialog: AlertDialog

    fun openDialog() {
        val inflater = atvd?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(atvd)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun closeDialog() {
        dialog.dismiss()
    }

}