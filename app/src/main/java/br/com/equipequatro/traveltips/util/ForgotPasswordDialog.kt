package br.com.equipequatro.traveltips.util

import android.app.AlertDialog
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import br.com.equipequatro.traveltips.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordDialog(private val atvd: FragmentActivity?) {

    lateinit var dialog: AlertDialog
    lateinit var editLogin: EditText
    lateinit var firebaseAuth: FirebaseAuth

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
            recuperar(it)
        }

        val dlgShow = dialog.show()

    }

    fun validar(view: View): Boolean {
        var bit_return: Boolean = true

        if (editLogin!!.text!!.isEmpty()) {
            editLogin!!.error = view.context.getString(R.string.error_email_obrigatorio)
            bit_return = false
        }

        return bit_return
    }

    fun recuperar(view: View)
    {
        if (validar(view)){
            dialog.dismiss()

            val loadingDialog = LoadingDialog(atvd)

            loadingDialog.openDialog()

            val handler = Handler()
            handler.postDelayed({

                loadingDialog.closeDialog()

                firebaseAuth = FirebaseAuth.getInstance()

                firebaseAuth.sendPasswordResetEmail(editLogin.text.toString())
                    .addOnCompleteListener (OnCompleteListener {
                        if(it.isSuccessful){
                            editLogin.setText("")
                            Toast
                                .makeText(dialog.context, view.context.getString(R.string.recuperacao_senha), Toast.LENGTH_LONG)
                                .show()
                        }else{
                            Toast
                                .makeText(dialog.context, view.context.getString(R.string.error_generico), Toast.LENGTH_LONG)
                                .show()
                        }
                    })

            }, 5000)
        }
        else {
            Toast
                .makeText(dialog.context, view.context.getString(R.string.msg_campos_obrigatorios), Toast.LENGTH_LONG)
                .show()
        }
    }


}