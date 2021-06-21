package com.app.calculator

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var screen : TextView? = null
    private var answer : TextView? = null
    private var formula : String = "0"
    private var isNew : Boolean = true
    private var isNum : Boolean = false
    private var symbolList : ArrayList<Int> = arrayListOf()
    private var symbolIndex : Int = 0
    private var numList : ArrayList<Int> = arrayListOf()
    private var numIndex : Int = 0
    private val symbol : Map<String, Int> = mapOf("+" to 0, "-" to 1, "×" to 2, "÷" to 3)

    private val add = {a : Int, b : Int -> a + b}
    private val minus = {a : Int, b : Int -> a - b}
    private val multiply = {a : Int, b : Int -> a * b}
    private val divide = {a : Int, b : Int -> a / b}
    private var cal : Array<(Int, Int) -> Int> = arrayOf(add, minus, multiply, divide)

    private fun calculate(lastCalc : Boolean) : Int? {
        if (lastCalc) {
            if (numIndex == 0 && symbolIndex == 0) return 0
            if (numIndex == 0) return null
            var current : Int = numList[--numIndex]

            while (numIndex > 0 && symbolIndex > 0) {
                current = cal[symbolList[--symbolIndex]](numList[--numIndex], current)
            }
            return if (numIndex == 0 && symbolIndex == 0) current
            else null
        } else {
            if (symbolIndex == 0) return null
            if (numIndex <= 1) return null
            var current : Int = numList[--numIndex]
            while (symbolIndex > 0 && symbolList[symbolIndex - 1] > 1) {
                current = cal[symbolList[--symbolIndex]](numList[--numIndex], current)
            }
            numList[numIndex] = current
            numIndex ++
            return null
        }
    }

    private fun initialize() {
        formula = ""
        answer!!.text = ""
        numList.clear()
        numIndex = 0
        symbolList.clear()
        symbolIndex = 0
        isNum = false
    }

    public fun onButtonClick(view : View) {
        var button : Button? = view as? Button
        var temp : String = button!!.text.toString()
        if (isNew) {
            initialize()
        }
        if (temp != "=") {
            if (temp == "C") {
                initialize()
                screen!!.text = "0"
                return
            };
            formula += temp
            if (symbol.containsKey(temp)) {
                if (symbolIndex >= symbolList.size) {
                    symbolList.add(symbol[temp]!!)
                } else {
                    symbolList[symbolIndex] = symbol[temp]!!
                }
                symbolIndex ++
                isNum = false
            } else {
                if (!isNum) {
                    if (numIndex >= numList.size) {
                        numList.add(temp.toInt())
                    } else {
                        numList[numIndex] = temp.toInt()
                    }
                    numIndex++
                } else {
                    numList[numIndex - 1] = numList[numIndex - 1] * 10 + temp.toInt()
                }
                isNum = true
                calculate(false)
            }
            isNew = false
        } else{
            var ans : Int? = calculate(true)
            var ansst : String = ""
            ansst = ans?.toString() ?: "Invalid"
            answer!!.text = ansst
            isNew = true
        }
        if (screen != null) screen!!.text = formula
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screen = findViewById<TextView>(R.id.screen)
        answer = findViewById<TextView>(R.id.answer)

    }
}