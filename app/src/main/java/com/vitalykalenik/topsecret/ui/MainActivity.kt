package com.vitalykalenik.topsecret.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vitalykalenik.topsecret.R
import com.vitalykalenik.topsecret.data.Api
import com.vitalykalenik.topsecret.data.TokenStore
import com.vitalykalenik.topsecret.models.request.Secret
import com.vitalykalenik.topsecret.models.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var editText: EditText
    lateinit var updateButton: FloatingActionButton
    lateinit var progressBar: ProgressBar
    lateinit var getSecretButton: Button
    lateinit var keyEdittext: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Секрет"

        editText = findViewById(R.id.secret)
        progressBar = findViewById(R.id.progress)
        updateButton = findViewById(R.id.update)
        getSecretButton = findViewById(R.id.get_secret)
        keyEdittext = findViewById(R.id.key)
        updateButton.setOnClickListener {
            if (keyEdittext.text.length == 4)
                updateSecret()
        }

        getSecretButton.setOnClickListener {
            if (keyEdittext.text.length == 4)
                getSecret()
        }
    }

    private fun updateSecret() {
        progressBar.visibility = View.VISIBLE
        Api.create().updateSecret("Bearer ${TokenStore.getInstance().token}", Secret(editText.text.toString()))
            .enqueue(object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    progressBar.visibility = View.INVISIBLE
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    Log.d("myLogs", response.toString() + " ${response.body()!!.message}")
                    progressBar.visibility = View.INVISIBLE
                }
            })
    }

    private fun getSecret() {
        progressBar.visibility = View.VISIBLE
        Api.create().getSecret("Bearer ${TokenStore.getInstance().token}").enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.code() == 200) {
                    Log.d("myLogs", response.toString() + " ${response.body()!!.message}")
                    progressBar.visibility = View.INVISIBLE
                    editText.setText(response.body()!!.message)
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
