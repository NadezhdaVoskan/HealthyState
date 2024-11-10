package com.example.healthystate.Navigations

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthystate.AddInfo.AddTrainingName
import com.example.healthystate.AutoAndReg.MainActivity
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import com.google.android.material.navigation.NavigationView
import java.time.LocalDate

class NavigationForThemes : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        setContentView(R.layout.activity_navigation_for_themes)

        val paperDbClass = PaperDbClass()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerlayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val headerView : View = navView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.headerName)
        val navUserEmail : TextView = headerView.findViewById(R.id.headerEmail)

        navUsername.text = paperDbClass.getUser()!!.firstName.toString() + " " + paperDbClass.getUser()!!.secondName.toString()
        navUserEmail.text = paperDbClass.getUser()!!.email.toString()

        toggle = ActionBarDrawerToggle(this@NavigationForThemes, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.nav_personal -> {
                    startActivity(Intent(applicationContext, PersonalAccount::class.java))
                }
                R.id.nav_rec ->
                {
                    startActivity(Intent(applicationContext, Recommendations::class.java))
                }
                R.id.nav_setting ->
                {
                    startActivity(Intent(applicationContext, AddTrainingName::class.java))
                }
                R.id.nav_exit ->
                {
                    paperDbClass.removeUser()
                    val intent: Intent =
                    Intent(applicationContext, MainActivity::class.java)
                    finish()
                    startActivity(intent)}
            }
            true
        }
//        val LoginUser = intent.getStringExtra("Login")
//        val PasswordUser = intent.getStringExtra("Password")

        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recycler_view)
        val structureList = ArrayList<StructureInfo>()
        structureList.add(StructureInfo(R.drawable.baseline_sport, "Активность"))
        structureList.add(StructureInfo(R.drawable.baseline_vaccines, "Лекарство"))
        structureList.add(StructureInfo(R.drawable.baseline_medical_information, "Врачи и обследования"))
        structureList.add(StructureInfo(R.drawable.baseline_food, "Питание"))
        structureList.add(StructureInfo(R.drawable.baseline_water, "Водный баланс"))
        structureList.add(StructureInfo(R.drawable.baseline_library_books, "Личный дневник"))
        structureList.add(StructureInfo(R.drawable.baseline_checklist, "Список задач"))
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(MyListAdapter(this, structureList))


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}