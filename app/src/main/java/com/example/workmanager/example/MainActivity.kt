package com.example.workmanager.example

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.workmanager.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var simplequest: OneTimeWorkRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        btnStart.setOnClickListener {
            WorkManager.getInstance().enqueue(simplequest!!)
        }
        btnCancel.setOnClickListener {
            WorkManager.getInstance().cancelWorkById(simplequest!!.id)
        }
    }

    @SuppressLint("SetTextI18n")
    fun init() {
        val constraint = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val data = Data.Builder().putString(MyWorker.EXTRA_TITLE, "This is title")
            .putString(MyWorker.EXTRA_TEXT, "This is text")
            .build()
        simplequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .setConstraints(constraint)
            .build()

        WorkManager.getInstance().getWorkInfoByIdLiveData(simplequest!!.id)
            .observe(this, Observer { workInfo ->
                workInfo?.let {
                    textView.append("/--/simple work manager" + workInfo.state)
                    if (workInfo.state.isFinished) {
                        val message = workInfo.outputData.getString(MyWorker.EXTRA_OUTPUT_MESSAGE)
                        textView.append("work Data$message")
                    }
                }
            })
/*
* Chaining Task
*
1
2
3
4
5
WorkManager.getInstance()
    .beginWith(workA)
    .then(workB)
    .then(workC)
    .enqueue();

* */
    }
}
