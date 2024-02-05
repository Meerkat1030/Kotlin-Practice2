package com.example.class20240202

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 날짜 선택
//         DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener {
//             override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                 Log.d("test", "${year}년 ${month + 1}월 ${dayOfMonth}일")
//             }
//         }, 2024, 1, 2).show()

        // 시간 선택
//        TimePickerDialog(this, object: TimePickerDialog.OnTimeSetListener {
//            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//                Log.d("test", "$hourOfDay : $minute")
//            }
//        }, 15, 0, true).show()

        // 체크박스를 포함하는 알림창
        val items = arrayOf<String>("사과", "복숭아", "수박", "딸기")
        AlertDialog.Builder(this).run {
            setTitle("과일 리스트")
            setIcon(android.R.drawable.ic_dialog_info)
            setMultiChoiceItems(items, booleanArrayOf(true, false, true, false),
                object:DialogInterface.OnMultiChoiceClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
                        Log.d("test", "${items[which]} 이 " +
                                "${if(isChecked) "선택되었습니다." else "선택 해제되었습니다."}")
                    }
                })
            setPositiveButton("닫기", null)
            show()
        }

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        ringtone.play()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            // 채널 정보 설정
            channel.description = "채널에 대한 설명"
            channel.setShowBadge(true)
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)

            // 채널을 이용해서 빌더 생성
            builder = NotificationCompat.Builder(this, channelId)
        } else {
            builder = NotificationCompat.Builder(this, "")
        }

        // 알림 객체 설정
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("알림 제목")
        builder.setContentText("알림 내용입니다.")

        // 알림 취소 막기(동작 안함)
        // 알림 터치해도 알림 사라지지 않음
        //builder.setAutoCancel(false)
        // 알림 스와이프해도 사라지지 않음
        //builder.setOngoing(true)

        // 액션 등록
        val replyIntent = Intent(this, ReplyReceiver::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(this, 30, replyIntent,
            PendingIntent.FLAG_MUTABLE)
        val KEY_TEXT_REPLY = "key_text_reply"
        var replyLabel: String = "답장"
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }
        builder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.send, "답장", replyPendingIntent
            ).addRemoteInput(remoteInput).build()
        )

        // 프로그래스바
//        builder.setProgress(100,0,true)

        // 알림 스타일
        // 큰 이미지 스타일(BigPictureStyle)
        val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.dog1)
        val bigStyle = NotificationCompat.BigPictureStyle()
        bigStyle.bigPicture(bigPicture)
//        builder.setStyle(bigStyle)

        // 긴 텍스트 스타일(BigTextStyle)
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText(resources.getString(R.string.long_text))
//        builder.setStyle(bigTextStyle)

        // 상자 스타일(InboxStyle)
        val style = NotificationCompat.InboxStyle()
        style.addLine("1코스 - 수락.불암산코스")
        style.addLine("2코스 - 용마.아차산코스")
        style.addLine("3코스 - 고덕.일자산코스")
        style.addLine("4코스 - 대모.우면산코스")
//        builder.setStyle(style)

        // 메시지 스타일(Message)
        val sender1: Person = Person.Builder()
            .setName("Lee JaeHong")
            .build()

        val sender2: Person = Person.Builder()
            .setName("MeerKat")
            .build()

        val message1 = NotificationCompat.MessagingStyle.Message(
            "hello",
            System.currentTimeMillis(),
            sender1
        )

        val message2 = NotificationCompat.MessagingStyle.Message(
            "world",
            System.currentTimeMillis(),
            sender2
        )

        val messageStyle = NotificationCompat.MessagingStyle(sender1)
            .addMessage(message1)
            .addMessage(message2)
        builder.setStyle(messageStyle)

        // 알림 발생
        manager.notify(11, builder.build())

        // 알림 끄기 버튼 클릭시
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            // 알림 제거
            manager.cancel(11)
        }
    }
}