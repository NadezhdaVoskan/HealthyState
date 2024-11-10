package com.example.healthystate.ChangeInfo

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.healthystate.API.APIClient
import com.example.healthystate.API.UserService
import com.example.healthystate.AdaptersLists.EatingList
import com.example.healthystate.Models.ChangeFoodModel
import com.example.healthystate.Models.DefaultResponse
import com.example.healthystate.Pages.Food
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChangeFoodDialog : AppCompatActivity() {

    lateinit var dateFood: EditText
    lateinit  var spinner: Spinner
    lateinit var namefood: EditText
    lateinit var countgrams: EditText
    lateinit var countcalories: EditText
    lateinit var descript: EditText
    var countgramsint: Int = 0
    var countcaloriesint: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.changefood_dialog)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        dateFood = findViewById(R.id.edtDate)
        namefood = findViewById(R.id.Name_Food)
        countgrams = findViewById(R.id.edtGrams)
        countcalories = findViewById(R.id.edtCalories)
        descript = findViewById(R.id.edtDescription)

        spinner = findViewById(R.id.spinEating)

        val eatings: ArrayList<FoodEating> = ArrayList()

        val retrofit = APIClient.buildService(UserService::class.java)
        val getEating: Call<List<EatingList>> = retrofit.getEatings()
        getEating.enqueue(object : Callback<List<EatingList>> {
            override fun onResponse(call: Call<List<EatingList>>, response: Response<List<EatingList>>) {
                if (response.isSuccessful) {
                    val allEating: List<EatingList>? = response.body()
                    allEating?.forEach {

                        eatings.add(FoodEating(it.eatingName))

                        val adapter = ArrayAdapter(
                            applicationContext,
                            android.R.layout.simple_spinner_dropdown_item,
                            eatings
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        spinner.setAdapter(adapter)

                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Недостоверные данные!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<EatingList>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ошибка со стороны клиента",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        val idFood = intent.getIntExtra("idFood",0)
        val nameFood = intent.getStringExtra("nameFood")
        val grams = intent.getIntExtra("grams", 0)
        val calories = intent.getIntExtra("calories",0)
        val descriptionFood = intent.getStringExtra("descriptionFood")
        val dayFood = intent.getStringExtra("dayFood")
        val logPassId = intent.getIntExtra("logPassId", 0)
        val eatingId = intent.getIntExtra("eatingId", 0)

        dateFood.setText(dayFood?.dropLast(9))
        namefood.setText(nameFood)
        countgrams.setText(grams.toString())
        countcalories.setText(calories.toString())
        descript.setText(descriptionFood)
        spinner.post(Runnable { spinner.setSelection(eatingId - 1) })

        countcalories.addTextChangedListener(object : TextWatcher {
            var editing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!editing) {
                    editing = true
                    if (s?.toString()?.startsWith("0") == true) {
                        countcalories.setText(s.subSequence(1, s.length))
                        countcalories.setSelection(countcalories.text.length)
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

        countgrams.addTextChangedListener(object : TextWatcher {
            var editing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!editing) {
                    editing = true
                    if (s?.toString()?.startsWith("0") == true) {
                        countgrams.setText(s.subSequence(1, s.length))
                        countgrams.setSelection(countgrams.text.length)
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

        dateFood.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat: String
                    if (dayOfMonth <= 10 && monthOfYear > 10) {
                        dat =
                            (year.toString() + "-" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateFood.setText(dat)
                    } else if (dayOfMonth < 10 && monthOfYear < 10) {
                        dat =
                            (year.toString() + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth.toString())
                        dateFood.setText(dat)
                    } else if (monthOfYear < 9 && dayOfMonth > 10) {
                        dat =
                            (year.toString() + "-0" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateFood.setText(dat)
                    } else {
                        dat =
                            (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                        dateFood.setText(dat)
                    }
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }


        val paperDbClass = PaperDbClass()

        val delItem = findViewById<Button>(R.id.DeleteButFood)
        delItem.setOnClickListener{
            retrofit.deleteFoods(idFood)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, "Питание удалено!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, Food::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
        }


        val updateItem = findViewById<Button>(R.id.ChangeButFood)
        updateItem.setOnClickListener{

            countgramsint = countgrams.getText().toString().trim().toInt();
            countcaloriesint = countcalories.getText().toString().trim().toInt();

            val obj = ChangeFoodModel(idFood,namefood.text.toString().trim(), countgramsint, countcaloriesint, descript.text.toString().trim(), dateFood.text.toString().trim(), paperDbClass.getUser()!!.idLogPass,
                spinner.selectedItemId.toInt()+1)

            if (namefood.text.toString().equals(""))
            {
                namefood?.requestFocus()
                Toast.makeText(applicationContext, "Введите название еды!", Toast.LENGTH_SHORT).show()
            }
            else if (countgrams.text.toString().equals(""))
            {
                countgrams?.requestFocus()
                Toast.makeText(applicationContext, "Введите количество грамм!", Toast.LENGTH_SHORT).show()
            }
            else if (countgrams.text.startsWith("0"))
            {
                countgrams?.requestFocus()
                Toast.makeText(applicationContext, "Количество грамм не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
            }
            else if (countgrams.text.toString().toInt() <= 0)
            {
                countgrams?.requestFocus()
                Toast.makeText(applicationContext, "Количество грамм не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
            }
            else if (countcalories.text.toString().equals(""))
            {
                countcalories?.requestFocus()
                Toast.makeText(applicationContext, "Введите количество калорий!", Toast.LENGTH_SHORT).show()
            }
            else if (countcalories.text.startsWith("0"))
            {
                countcalories?.requestFocus()
                Toast.makeText(applicationContext, "Количество калорий не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
            }
            else if (countcalories.text.toString().toInt() <= 0)
            {
                countcalories?.requestFocus()
                Toast.makeText(applicationContext, "Количество калорий не может быть отрицательным числом или равно 0!", Toast.LENGTH_SHORT).show()
            }
            else if (dateFood.text.toString().equals(""))
            {
                dateFood?.requestFocus()
                Toast.makeText(applicationContext, "Выберите дату приёма пищи!", Toast.LENGTH_SHORT).show()
            }
            else {
            retrofit.changeFoods(idFood, obj)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Mistake. Try again!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext,"Питание изменено!", Toast.LENGTH_SHORT).show()
                        val intent: Intent =
                            Intent(applicationContext, Food::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }

                })
        }}
    }

    private class FoodEating {
        var eating_name: String? = null

        constructor() {}
        constructor(eating_name: String?) {
            this.eating_name = eating_name
        }

        override fun toString(): String {
            return eating_name!!
        }
    }
}