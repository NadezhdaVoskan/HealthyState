package com.example.healthystate.AutoAndReg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.healthystate.API.APIClient
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.RequestModel
import com.example.healthystate.R
import com.example.healthystate.API.UserService
import com.example.healthystate.Models.User
import com.example.healthystate.Navigations.NavigationForThemes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegDialog : DialogFragment() {

    var firstnameUser: EditText? = null
    var secondnameUser: EditText? = null
    var middlenameUser: EditText? = null
    var emailUser: EditText? = null
    var passwordUser: EditText? = null
    var check: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.roundcornerdialog);
        val view: View = inflater!!.inflate(R.layout.register_window, container, false)

        firstnameUser = view.findViewById(R.id.FirstNameReg);
        secondnameUser = view.findViewById(R.id.SecondNameReg);
        middlenameUser = view.findViewById(R.id.MiddleNameReg);
        emailUser = view.findViewById(R.id.EmailReg);
        passwordUser = view.findViewById(R.id.PasswordReg);

        val retrofit = APIClient.buildService(UserService::class.java)
        val regexEmail = Regex("[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}")

        val regexFIO = Regex("^(?!.*\\d)[a-zA-Zа-яА-Я]{2,}\$")

        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&*()])(?=\\S+\$).{8,}\$")


        val registrBtn = view.findViewById<Button>(R.id.registrBut)
        registrBtn.setOnClickListener{

            val firstname = firstnameUser?.text.toString()
            val middlename = middlenameUser?.text.toString()
            val secondname = secondnameUser?.text.toString()
            val email = emailUser?.text.toString()
            val password = passwordUser?.text.toString()

            if (firstnameUser?.text.toString().equals(""))
            {
             firstnameUser?.requestFocus()
            }
            else if (!regexFIO.matches(firstname)) {
                firstnameUser?.requestFocus()
                Toast.makeText(activity?.applicationContext, "Имя введено некорректно!", Toast.LENGTH_LONG).show()
            }
            else if (secondnameUser?.text.toString().equals(""))
            {
                secondnameUser?.requestFocus()
            }
            else if (!regexFIO.matches(secondname)) {
                secondnameUser?.requestFocus()
                Toast.makeText(activity?.applicationContext, "Фамилия введена некорректно!", Toast.LENGTH_LONG).show()
            }
            else if (emailUser?.text.toString().equals(""))
            {
                emailUser?.requestFocus()
            }
            else if (passwordUser?.text.toString().equals(""))
            {
                passwordUser?.requestFocus()
            }

            else {

                val obj = RequestModel(firstname, secondname, middlename, email, password)


                if (!email.matches(regexEmail))
                {
                    emailUser?.requestFocus()
                    Toast.makeText(activity?.applicationContext, "Почта введена некорректно!", Toast.LENGTH_LONG).show()
                }
                else if (password.length < 8)
                {
                    passwordUser?.requestFocus()
                    Toast.makeText(activity?.applicationContext, "Длина пароля может быть мнимум 8 символов!", Toast.LENGTH_LONG).show()
                }
                else if (!password.matches(passwordRegex))
                {
                    passwordUser?.requestFocus()
                    Toast.makeText(activity?.applicationContext, "Некорректные данные пароля. (Должна быть хотя бы одна заглавная и маленькая буква, и любой символ!", Toast.LENGTH_LONG).show()
                }

                else if (emailUser?.text.toString().matches(regexEmail) &&
                    passwordUser?.text.toString().matches(passwordRegex) )
                {

                    val getUsers: Call<List<User>> = retrofit.getUsers()
                    getUsers.enqueue(object : Callback<List<User>> {
                        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                            if (response.isSuccessful) {
                                val allUsers: List<User>? = response.body()
                                allUsers?.forEach {
                                    if (it.email == emailUser?.text.toString()) {
                                        emailUser?.requestFocus()
                                        Toast.makeText(activity?.applicationContext, "Такой пользователь уже существует!", Toast.LENGTH_LONG).show()
                                        check = true
                                        return@forEach
                                    }
                                }
                                if (!check) {
                                    retrofit.createUser(obj)
                                        .enqueue(object : Callback<DefaultResponse> {
                                            override fun onFailure(
                                                call: Call<DefaultResponse>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    activity?.applicationContext,
                                                    "Mistake. Try again!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                            override fun onResponse(
                                                call: Call<DefaultResponse>,
                                                response: Response<DefaultResponse>
                                            ) {

                                                Toast.makeText(
                                                    activity?.applicationContext,
                                                    "Вы успешно зарегистировались!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                startActivity(
                                                    Intent(
                                                        activity?.applicationContext,
                                                        MainActivity::class.java
                                                    )
                                                )
                                                activity?.finish()

                                            }
                                        })
                                }
                                check = false

                            } else {
                                Toast.makeText(activity?.applicationContext, "Некорректные данные!", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Toast.makeText(activity?.applicationContext, "Некорректные данные!", Toast.LENGTH_LONG).show()
                        }

                    })

                }
        }}
        return view;

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}