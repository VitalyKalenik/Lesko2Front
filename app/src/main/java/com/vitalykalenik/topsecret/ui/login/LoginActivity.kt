package com.vitalykalenik.topsecret.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vitalykalenik.topsecret.EncodeUtils
import com.vitalykalenik.topsecret.R
import com.vitalykalenik.topsecret.data.Api
import com.vitalykalenik.topsecret.data.TokenStore
import com.vitalykalenik.topsecret.models.request.RegisterRequest
import com.vitalykalenik.topsecret.models.response.LoginResponse
import com.vitalykalenik.topsecret.models.response.RegisterResponse
import com.vitalykalenik.topsecret.ui.MainActivity
import com.vitalykalenik.topsecret.ui.RegistrationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var loginEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var registerButton: Button

    var salt: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Вход"

        loginEditText = findViewById(R.id.login)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        registerButton = findViewById(R.id.register_button)
        registerButton.setOnClickListener { startActivity(RegistrationActivity.newIntent(this)) }
        loginButton.setOnClickListener { callGetSalt() }
    }

    private fun callLogin() {
        val encodedPassword = encodeSha256(passwordEditText.text.toString())
        Api.create().login("password", loginEditText.text.toString(), encodedPassword)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.code() == 200) {
                        TokenStore.getInstance().token = response.body()!!.access_token
                        startActivity(MainActivity.newIntent(this@LoginActivity))
                    } else {
                        Toast.makeText(this@LoginActivity, "Такой пользователь не зарегистрирован", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("mylogs", "Error ${t.localizedMessage}")
                }
            })
    }

    private fun callGetSalt() {
        Api.create().getSalt(RegisterRequest(loginEditText.text.toString(), passwordEditText.text.toString()))
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.code() == 200) {
                        salt = response.body()!!.message
                        callLogin()
                    } else {
                        Toast.makeText(this@LoginActivity, "Ошибка", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("mylogs", "Error ${t.localizedMessage}")
                }
            })
    }

    fun encodeSha256(originalString: String): String {
        return EncodeUtils.encodeSha256(originalString, salt)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
