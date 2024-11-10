package com.example.healthystate.AutoAndReg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.Models.*
import com.example.healthystate.Navigations.NavigationForThemes
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginDialog: DialogFragment()  {

    var emailUser: EditText? = null
    var passwordUser: EditText? = null
    var paperDbClass: PaperDbClass? = null
    var check: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.roundcornerdialog);
        val view: View = inflater!!.inflate(R.layout.login_wondow, container, false)

        emailUser = view.findViewById(R.id.EmailLogin);
        passwordUser = view.findViewById(R.id.PasswordLogin);

        Paper.init(activity?.applicationContext!!)
        paperDbClass = PaperDbClass()

        if (paperDbClass!!.getUser() != null) {
            val intent = Intent(activity?.applicationContext, NavigationForThemes::class.java)
            startActivity(intent)
        }


        val loginBtn = view.findViewById<Button>(R.id.toLoginBut)
        loginBtn.setOnClickListener{
            if (emailUser?.text.toString().equals(""))
            {
                emailUser?.requestFocus()
            }
            else if (passwordUser?.text.toString().equals(""))
            {
                passwordUser?.requestFocus()
            }
            else {
                login();
            }
        }
        return view;
    }

    private fun login() {
        val retrofit = APIClient.buildService(UserService::class.java)
        val getUsers: Call<List<User>> = retrofit.getUsers()
        getUsers.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val allUsers: List<User>? = response.body()
                    allUsers?.forEach {
                        if (it.email == emailUser?.text.toString() && it.password == passwordUser?.text.toString()) {

                            val user = User(
                                it.idLogPass,
                                it.firstName,
                                it.secondName,
                                it.middleName,
                                emailUser?.text.toString(),
                                it.weight,
                                passwordUser?.text.toString()
                            )
                            paperDbClass?.saveUser(user)
                            val intent: Intent =
                                Intent(activity?.applicationContext, NavigationForThemes::class.java)

                            intent.putExtra("idUserLog",it.idLogPass.toString())
                            startActivity(intent)
                            activity?.finish()

                            check = 1
                        }
                    }
                    if (check == 0){
                    Toast.makeText(activity?.applicationContext, "Некорректные данные или пользователь не существует!", Toast.LENGTH_LONG).show()}
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Некорректные данные или пользователь не существует!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(
                    activity?.applicationContext,
                    "Некорректные данные или пользователь не существует!",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}