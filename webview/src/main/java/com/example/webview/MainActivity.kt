package com.example.webview

import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val web1: WebView = findViewById(R.id.web)
        web1.webViewClient = WebViewClient()        /* 웹뷰 클라이언트 사용*/
        web1.loadUrl("https:/m.naver.com/")    /*읽어들일 URL*/
        web1.settings.javaScriptEnabled = true      /*자바 스크립트 허용*/

        // 뒤로가기 버튼
        val btn_back: Button = findViewById(R.id.btn_back)
        btn_back.setOnClickListener{
            web1.goBack()
        }

        // 홈으로 가기
        // 경고창
        val builder = AlertDialog.Builder(this)
        builder.setTitle("홈으로 가기")
            .setMessage("정말 홈으로 갈거임?")
            .setPositiveButton("예", {dialog, id ->
                // "예"를 눌렀을 때 실행
                web1.loadUrl("https:/m.naver.com/")
            })
            .setNegativeButton("아니요",
                DialogInterface.OnClickListener{ dialog, id ->
                    // "아니요"를 눌렀을 때 실행되는 기능
                    showToast()
                    Toast.makeText(this@MainActivity, "아니요를 선택하셨습니다.", Toast.LENGTH_SHORT).show()

                })
        val btn_home: Button = findViewById(R.id.btn_home)
        btn_home.setOnClickListener{
            builder.show()
        }

        // 새로고침 버튼
        val btn_reload: Button = findViewById(R.id.btn_reload)
        btn_reload.setOnClickListener{
            val anim = AnimationUtils.loadAnimation(this, R.anim.ani1)
            btn_reload.startAnimation(anim)
            web1.reload()
        }

        // 앞으로 가기 버튼
        val btn_go: Button = findViewById(R.id.btn_go)
        btn_go.setOnClickListener{
            web1.goForward()
        }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) {
            isGranted ->
            if(isGranted){
                Log.d("test", "callback, granted..")
            }else{
                Log.d("test", "callback, dinied..")
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun noti(){
        Log.d("test", "noti()에 대한 기능입니다.")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            Log.d("test", "31 버전 이상에서만 동작합니다.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun showToast(){
        val toast = Toast.makeText(this, "아니요를 선택하셧습니다.", Toast.LENGTH_LONG)
        toast.addCallback(
            object : Toast.Callback(){
                override fun onToastHidden() {
                    super.onToastHidden()
                    Log.d("test", "Toast Hidden")
                }

                override fun onToastShown() {
                    super.onToastShown()
                    Log.d("test", "Toast Shown")
                }
            }
        )
        toast.show()
    }




}