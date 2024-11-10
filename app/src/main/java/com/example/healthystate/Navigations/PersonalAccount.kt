package com.example.healthystate.Navigations

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AutoAndReg.MainActivity
import com.example.healthystate.ChangeInfo.ChangePassword
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Models.User
import com.example.healthystate.Models.UserModel
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.mail.*
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class PersonalAccount : AppCompatActivity() {
    lateinit var txtFirstName: EditText
    lateinit var txtSecondName: EditText
    lateinit var  txtMiddleName: EditText
    lateinit var  txtWeight: EditText
    lateinit var txtEmail: EditText
    lateinit var txtPass: EditText
    var weightint: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.fragment_personal_account)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }



        val paperDbClass = PaperDbClass()
        val retrofit = APIClient.buildService(UserService::class.java)

        txtFirstName = findViewById(R.id.firstName)
        txtSecondName = findViewById(R.id.secondName)
        txtMiddleName = findViewById(R.id.middleName)
        txtWeight = findViewById(R.id.weight)
        txtEmail = findViewById(R.id.email)
        txtPass = findViewById(R.id.password)

        txtFirstName.setText(paperDbClass.getUser()!!.firstName.toString())
        txtSecondName.setText(paperDbClass.getUser()!!.secondName.toString())
        txtMiddleName.setText(paperDbClass.getUser()!!.middleName.toString())
        txtWeight.setText(paperDbClass.getUser()!!.weight.toString())
        if (txtWeight.text.toString() == "null") {
            txtWeight.setText("")
        }
        val getUsers: Call<List<User>> = retrofit.getUsers()
        getUsers.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val allUsers: List<User>? = response.body()
                    allUsers?.forEach {
                        if (it.email == txtEmail?.text.toString() && it.password == txtPass?.text.toString()) {

                            val user = User(
                                it.idLogPass,
                                it.firstName,
                                it.secondName,
                                it.middleName,
                                txtEmail.text.toString(),
                                it?.weight,
                                txtPass?.text.toString()
                            )
                            paperDbClass?.saveUser(user)
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Недостоверные данные!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        txtWeight.addTextChangedListener(object : TextWatcher {
            var editing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!editing) {
                    editing = true
                    if (s?.toString()?.startsWith("0") == true) {
                        txtWeight.setText(s.subSequence(1, s.length))
                        txtWeight.setSelection(txtWeight.text.length)
                    }
                    editing = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!editing) {
                    editing = true
                    if (s.toString().isEmpty()) {
                        s?.replace(0, s.length, "0")
                    }
                    editing = false
                }
            }
        })

        txtEmail.setText(paperDbClass.getUser()!!.email.toString())
        txtPass.setText(paperDbClass.getUser()!!.password.toString())

        val updateItem = findViewById<Button>(R.id.saveChange)
        updateItem.setOnClickListener{
            weightint = txtWeight.text.toString().trim().toInt();
            val obj = UserModel(paperDbClass.getUser()!!.idLogPass, paperDbClass.getUser()!!.firstName.toString(),
                paperDbClass.getUser()!!.secondName.toString(), paperDbClass.getUser()!!.middleName.toString(),
                paperDbClass.getUser()!!.email.toString(), weightint, paperDbClass.getUser()!!.password.toString())

        if (txtWeight.text.toString().equals(""))
        {
            txtWeight?.requestFocus()
            Toast.makeText(applicationContext, "Введите вес!", Toast.LENGTH_SHORT).show()
        }
        else if (txtWeight.text.startsWith("0"))
        {
            txtWeight?.requestFocus()
            Toast.makeText(applicationContext, "Вес не может быть отрицательным числом или равен 0!", Toast.LENGTH_SHORT).show()
        }
        else if (txtWeight.text.toString().toInt() <= 0)
        {
            txtWeight?.requestFocus()
            Toast.makeText(applicationContext, "Вес не может быть отрицательным числом или равен 0!", Toast.LENGTH_SHORT).show()
        }
        else {
            retrofit.changeUser(paperDbClass.getUser()!!.idLogPass, obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Вес успешно добавлен!", Toast.LENGTH_SHORT).show()
                    }

                })
        }}

        val delItem = findViewById<Button>(R.id.deleteAccount)
        delItem.setOnClickListener{
            val builder = AlertDialog.Builder(this@PersonalAccount)
            builder.setMessage("Вы действительно хотите навсегда удалить свой аккаунт?")
                .setCancelable(false)
                .setPositiveButton("Да") { dialog, id ->
            retrofit.deleteUser(paperDbClass.getUser()!!.idLogPass)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Аккаунт удален!", Toast.LENGTH_LONG).show()
                        paperDbClass.removeUser()
                        val intent: Intent =
                            Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
                }
                .setNegativeButton("Нет") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        val changePassword = findViewById<Button>(R.id.changePass)
        changePassword.setOnClickListener{
            val intent: Intent =
                Intent(applicationContext, ChangePassword::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

    }
}