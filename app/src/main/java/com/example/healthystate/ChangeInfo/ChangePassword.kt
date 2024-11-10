package com.example.healthystate.ChangeInfo

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.Models.DefaultResponse
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
import kotlin.random.Random

class ChangePassword : AppCompatActivity() {
    lateinit var txtOldPass: EditText
    lateinit var txtNewPass: EditText
    lateinit var txtCode: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.change_password)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val paperDbClass = PaperDbClass()
        val retrofit = APIClient.buildService(UserService::class.java)

        val myRandomValues = List(1) { Random.nextInt(1000, 9999) }

        txtOldPass = findViewById(R.id.oldpass)
        txtNewPass = findViewById(R.id.newpass)
        txtCode = findViewById(R.id.codepass)

        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&*()])(?=\\S+\$).{8,}\$")

        val sendCode = findViewById<Button>(R.id.sendCode)
        sendCode.setOnClickListener{
            try {
                var stringSenderEmail: String = "isip_n.n.voskan@mpt.ru"
                var stringReceiverEmail = paperDbClass.getUser()!!.email.toString()
                var stringPasswordSenderEmail: String = "9010YFNFIFdjcrfy"

                var stringHost: String = "smtp.gmail.com"

                var properties: Properties = System.getProperties()

                properties.put("mail.smtp.host", stringHost)
                properties.put("mail.smtp.port", "465")
                properties.put("mail.smtp.ssl.enable", "true")
                properties.put("mail.smtp.auth", "true")

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication? {
                        return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
                    }
                })
                val mimeMessage = MimeMessage(session)
                mimeMessage.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(stringReceiverEmail)
                )

                mimeMessage.subject = "Изменение пароля 'HealthyState'"
                mimeMessage.setText("Привет, "+paperDbClass.getUser()!!.secondName.toString()+" "+paperDbClass.getUser()!!.firstName.toString()+
                        "\n\n Ваш код для изменения пароля: "+ myRandomValues[0]+" \n\n Никому не сообщайте этот код.")

                val thread = Thread {
                    try {
                        Transport.send(mimeMessage)
                    } catch (e: MessagingException) {
                        e.printStackTrace()
                    }
                }
                thread.start();

            } catch (e: AddressException) {
                e.printStackTrace();
            } catch (e: AddressException) {
                e.printStackTrace();
            }
        }
        val changePassword = findViewById<Button>(R.id.changePass)
        changePassword.setOnClickListener{
            if (txtOldPass.text.toString().equals(""))
            {
                txtOldPass?.requestFocus()
                Toast.makeText(applicationContext, "Введите старый пароль!", Toast.LENGTH_SHORT).show()
            }
            else if (txtOldPass.text.toString() != paperDbClass.getUser()!!.password.toString()){
                txtOldPass?.requestFocus()
                Toast.makeText(applicationContext, "Старый пароль не совпадает с настоящим!", Toast.LENGTH_SHORT).show()
            }
            else  if (txtNewPass.text.toString().equals(""))
            {
                txtNewPass?.requestFocus()
                Toast.makeText(applicationContext, "Введите новый пароль!", Toast.LENGTH_SHORT).show()
            }else if (txtNewPass.text.toString().length < 8)
            {
                txtNewPass?.requestFocus()
                Toast.makeText(applicationContext, "Длина пароля может быть минимум 8 символов!", Toast.LENGTH_SHORT).show()
            }
            else if (!txtNewPass.text.toString().matches(passwordRegex))
            {
                txtNewPass?.requestFocus()
                Toast.makeText(applicationContext, "Некорректные данные пароля. (Должна быть хотя бы одна заглавная и маленькая буква, и любой символ!", Toast.LENGTH_SHORT).show()
            }
            else if (txtOldPass.text.toString() == txtNewPass.text.toString())
            {
                txtNewPass?.requestFocus()
                Toast.makeText(applicationContext, "Пароли не могут совпадать!", Toast.LENGTH_SHORT).show()
            }
            else  if (txtCode.text.toString().equals(""))
            {
                txtCode?.requestFocus()
                Toast.makeText(applicationContext, "Введите код!", Toast.LENGTH_SHORT).show()
            } else if (txtCode.text.toString() != myRandomValues[0].toString())
            {
                txtCode?.requestFocus()
                Toast.makeText(applicationContext, "Введенный код не совпадает с отправленным кодом!", Toast.LENGTH_SHORT).show()
            }
            else{
            if (txtOldPass.text.toString() == paperDbClass.getUser()!!.password.toString() && txtCode.text.toString() == myRandomValues[0].toString())
            {
            val obj = UserModel(paperDbClass.getUser()!!.idLogPass, paperDbClass.getUser()!!.firstName.toString(),
                paperDbClass.getUser()!!.secondName.toString(), paperDbClass.getUser()!!.middleName.toString(),
                paperDbClass.getUser()!!.email.toString(), paperDbClass.getUser()!!.weight!!.toInt(), txtNewPass.text.toString())

            retrofit.changeUser(paperDbClass.getUser()!!.idLogPass, obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Пароль успешно изменен!", Toast.LENGTH_SHORT).show()
                    }

                })
        }
            else {
                Toast.makeText(applicationContext, "Недостоверные данные!", Toast.LENGTH_SHORT).show()
            }
        }}

    }
}