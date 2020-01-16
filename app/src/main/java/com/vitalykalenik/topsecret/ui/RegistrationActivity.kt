package com.vitalykalenik.topsecret.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vitalykalenik.topsecret.R
import com.vitalykalenik.topsecret.data.Api
import com.vitalykalenik.topsecret.models.request.RegisterRequest
import com.vitalykalenik.topsecret.models.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    lateinit var loginEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        title = "Регистрация"

        loginEditText = findViewById(R.id.login)
        passwordEditText = findViewById(R.id.password)
        registerButton = findViewById(R.id.register_button)
        registerButton.setOnClickListener { callRegister() }
    }

    private fun callRegister() {
        Api.create().registerUser(RegisterRequest(loginEditText.text.toString(), passwordEditText.text.toString()))
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    Log.d("mylogs", response.toString())
                    Log.d("mylogs", response.body().toString())
                    when (response.body()!!.statusCode) {
                        "200" -> finish()
                        "401" -> Toast.makeText(
                            this@RegistrationActivity,
                            "Такой пользователь уже существует",
                            Toast.LENGTH_SHORT
                        ).show()
                        "402" -> Toast.makeText(
                            this@RegistrationActivity,
                            "Введены пустые поля",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("mylogs", "Error ${t.localizedMessage}")
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Ошибка",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, RegistrationActivity::class.java)
    }
}
