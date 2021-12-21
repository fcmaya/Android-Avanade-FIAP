package br.com.equipequatro.traveltips

import android.app.AlertDialog
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class ForgotPasswordDialog(val atvd: FragmentActivity?) {

    lateinit var dialog: AlertDialog
    lateinit var editLogin: EditText

    fun openDialog() {
        val inflater = atvd?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.dialog_forgot_password, null)
        val builder = AlertDialog.Builder(atvd)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()

        editLogin = dialogView!!.findViewById<EditText>(R.id.edtEsqueciSenhaEmail)

        val btnFechar = dialogView?.findViewById<ImageButton>(R.id.btnEqueciSenhaFechar)
        btnFechar?.setOnClickListener {
            dialog.dismiss()
        }

        val btnRecuperarSenha = dialogView?.findViewById<Button>(R.id.btnRecuperarSenha)
        btnRecuperarSenha?.setOnClickListener {
            recuperar()
        }

        val dlgShow = dialog.show()

    }

    fun validar(): Boolean {
        var bit_return: Boolean = true

        if (editLogin!!.text!!.isEmpty()) {
            editLogin!!.error = "E-mail é obrigatório!"
            bit_return = false
        }

        return bit_return
    }

    fun recuperar()
    {
        if (validar()){
            dialog.dismiss()

            val loadingDialog = LoadingDialog(atvd)

            loadingDialog.openDialog()

            val handler = Handler()
            handler.postDelayed({

                loadingDialog.closeDialog()

                //TODO: fazer codigo de recuperação de senha

            }, 5000)
        }
        else {
            Toast
                .makeText(dialog.context, "Ops! Campos obrigatórios", Toast.LENGTH_LONG)
                .show()
        }
    }


}