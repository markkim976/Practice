package com.example.practice

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.abs
import java.util.*

class MainActivity : AppCompatActivity() {
    var p_num = 3 //참가자 수
    var k = 1 //참가자 번호
    val point_list = mutableListOf<Float>()
    var isBlind = false //초깃값 : false 블라인드 모드 변수 생성 (전체 로직에 적용되기 때문에 메인 함수에다가 설정)

    //start 함수 정의
    fun start() {
        setContentView(R.layout.activity_start) //레이아웃 호출
        val tv_pnum : TextView = findViewById(R.id.tv_pnum)
        val btn_minus : Button = findViewById(R.id.btn_minus)
        val btn_plus : Button = findViewById(R.id.btn_plus)
        val btn_start : Button = findViewById(R.id.btn_start)
        val btn_blind : Button = findViewById(R.id.btn_blind)

        btn_blind.setOnClickListener {
            isBlind =! isBlind
            if (isBlind == true) {
                btn_blind.text = "Blind 모드 ON"
            }else {
                btn_blind.text = "Blind 모드 OFF"
            }
        }

        tv_pnum.text = p_num.toString()
        btn_minus.setOnClickListener { // -Button
            p_num --
            if (p_num ==0) { //-를 계속하면 음수까지 나오기 때문에 p_num이라면 
                p_num = 1 //1까지만 나올 수 있도록
            }
            tv_pnum.text = p_num.toString()
        }
        btn_plus.setOnClickListener { // -Button
            p_num ++
            tv_pnum.text = p_num.toString()
        }
        btn_start.setOnClickListener {
            main() //btn_start 버튼을 클릭 시 메인 함수 호출
        }

    }

    fun main() { //main 함수를 생성
        setContentView(R.layout.activity_main)

        //변수 선언
        var timerTask: Timer? = null
        var stage = 1
        var sec: Int = 0
        val tv: TextView = findViewById(R.id.tv_random)
        val tv_t: TextView = findViewById(R.id.tv_pnum)
        val tv_p: TextView = findViewById(R.id.tv_point)
        val tv_people: TextView = findViewById(R.id.tv_people)
        val btn: Button = findViewById(R.id.btn_minus)
        val btn_i : Button =findViewById(R.id.btn_i)
        var random_box = Random()
        val num = random_box.nextInt(1001) //0~1001사이 수 반환

        tv.text = ((num.toFloat()) / 100).toString()
        btn.text = "시작"
        tv_people.text = "참가자 $k 번"

        btn_i.setOnClickListener { //Button i를 눌렀을 때
            point_list.clear() //초기화
            k = 1
            start()
        }

        btn.setOnClickListener {
            stage++
            if (stage == 2) {
                timerTask = kotlin.concurrent.timer(period = 10) {
                    sec++
                    runOnUiThread {
                        if (isBlind == false) {
                            tv_t.text = (sec.toFloat() / 100).toString() //블라인드 모드가 off 일때
                        } else if (isBlind == true && stage == 2) {
                            tv_t.text = "???"
                        }
                    }
                }
                btn.text = "정지"

            } else if (stage == 3) {
                tv_t.text = (sec.toFloat() / 100).toString()
                timerTask?.cancel()
                val point = abs(sec - num).toFloat() / 100
                point_list.add(point)
                println(point)
                tv_p.text = point.toString()
                btn.text = "다음"
                stage = 0 //다음이 나오는 stage3이 되면 다시 처음으로 돌아갈 수 있도록 stage = 0 해줌
            } else if (stage == 1) {
                if (k < p_num) {
                    k++
                    main()
                } else {
                    end()
                }
            }
        }
    }

    fun end() {
        setContentView(R.layout.activity_end)
        val tv_last: TextView = findViewById(R.id.tv_last)
        val tv_lpoint: TextView = findViewById(R.id.tv_lpoint)
        val btn_init: Button = findViewById(R.id.btn_init)

        tv_lpoint.text = (point_list.maxOrNull()).toString() //포인트 리스트를 들고 올 수 있는 이유는 함수 바깥에서 변수를
    // 선언했기 때문
        var index_last =  point_list.indexOf(point_list.maxOrNull())
        tv_last.text = "참가자" +(index_last+1).toString()

        btn_init.setOnClickListener {
            point_list.clear() //초기화
            k = 1
            start() //btn_init(처음으로) 버튼을 누르면 start()로 이동
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start()
    }
}