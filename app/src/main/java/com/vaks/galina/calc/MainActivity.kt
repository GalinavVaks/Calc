package com.vaks.galina.calc

import android.content.ClipData
import android.content.ClipDescription.*
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private var calc: Calculator = Calculator(this) // Logic. this - to update output

    // Keys, for saved states
    // TODO check whether it's advisable to just store everything in the calc object, instead of restoring
    val OUTPUT_KEY = "output"
    val WAS_OPERATOR_JUST_PRESSED = "was an operator just pressed"
    val OPERATOR_PRESSED = "operator pressed"
    val FIRST_OPERAND = "first operand"
    val WAS_EQUAL_JUST_PRESSED = "was equal just pressed"
    val IS_TO_THE_RIGHT_OF_DECIMAL_POINT = "we are to the right of the decimal point"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        restoreStateHelper(savedInstanceState)

        // Start button listeners
        //Start digit listeners
        findViewById<Button>(R.id.digitOne).setOnClickListener {
            calc.addDigit(1)
        }
        findViewById<Button>(R.id.digitTwo).setOnClickListener {
            calc.addDigit(2)
        }
        findViewById<Button>(R.id.digitThree).setOnClickListener {
            calc.addDigit(3)
        }
        findViewById<Button>(R.id.digitFour).setOnClickListener {
            calc.addDigit(4)
        }
        findViewById<Button>(R.id.digitFive).setOnClickListener {
            calc.addDigit(5)
        }
        findViewById<Button>(R.id.digitSix).setOnClickListener {
            calc.addDigit(6)
        }
        findViewById<Button>(R.id.digitSeven).setOnClickListener {
            calc.addDigit(7)
        }
        findViewById<Button>(R.id.digitEight).setOnClickListener {
            calc.addDigit(8)
        }
        findViewById<Button>(R.id.digitNine).setOnClickListener {
            calc.addDigit(9)
        }
        findViewById<Button>(R.id.digitZero).setOnClickListener {
            calc.addDigit(0)
        }

        // Start operator listeners
        findViewById<Button>(R.id.operatorPlus).setOnClickListener {
            calc.pressOperator("+")
        }
        findViewById<Button>(R.id.operatorMinus).setOnClickListener {
            calc.pressOperator("-")
        }
        findViewById<Button>(R.id.operatorMultiply).setOnClickListener {
            calc.pressOperator("*")
        }
        findViewById<Button>(R.id.operatorDivide).setOnClickListener {
            calc.pressOperator("/")
        }

        findViewById<Button>(R.id.buttonDecimalPoint).setOnClickListener {
            calc.decimalPointPressed()
        }

        findViewById<Button>(R.id.buttonEqual).setOnClickListener {
            calc.equalPressed()
        }

        findViewById<Button>(R.id.buttonMemPlus).setOnClickListener {
            calc.memPlus()
        }

        findViewById<Button>(R.id.buttonMemMinus).setOnClickListener {
            calc.memMinus()
        }

        findViewById<Button>(R.id.buttonMemR).setOnClickListener {
            calc.memR()
        }

        findViewById<Button>(R.id.buttonChangeSign).setOnClickListener {
            calc.changeSign()
        }

        // Yes, I know it's better in newer APIs, where you can get cool percentages
        // For now, we'll do it the hard way
        adjustButtonSizes()
    }

    // Callback is later in the file
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Saving for return, just ic case
        val retValue = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.mainmenu, menu)
        return retValue
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(OUTPUT_KEY, calc.output)
        outState.putBoolean(WAS_OPERATOR_JUST_PRESSED, calc.isOperatorJustPressed)
        outState.putString(OPERATOR_PRESSED, calc.operatorPressed)
        outState.putString(FIRST_OPERAND, calc.firstOperand)
        outState.putBoolean(WAS_EQUAL_JUST_PRESSED, calc.isEqualJustPressed)
        outState.putBoolean(IS_TO_THE_RIGHT_OF_DECIMAL_POINT, calc.isBeyondDecimal)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)

        restoreStateHelper(savedInstanceState)
    }

    // Retrieve for screen orientation and other destructions
    private fun restoreStateHelper (bundle: Bundle?) {
        calc.output = bundle?.getString(OUTPUT_KEY) ?: "0"
        updateOutput()
        calc.isOperatorJustPressed = bundle?.getBoolean(WAS_OPERATOR_JUST_PRESSED) ?: false
        calc.operatorPressed = bundle?.getString(OPERATOR_PRESSED) ?: ""
        calc.firstOperand = bundle?.getString(FIRST_OPERAND) ?: ""
        calc.isEqualJustPressed = bundle?.getBoolean(WAS_EQUAL_JUST_PRESSED) ?: false
        calc.isBeyondDecimal = bundle?.getBoolean(IS_TO_THE_RIGHT_OF_DECIMAL_POINT) ?: false
    }

    // Sync output from the calculator to here (public cause called from there)
    fun updateOutput() {
        findViewById<TextView>(R.id.outputText).text = calc.output
    }

     private fun adjustButtonSizes() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels
        findViewById<TextView>(R.id.outputText).layoutParams.width = (screenWidth*0.9).toInt()

        // Button lookups, to avoid repetition
        val digitOne = findViewById<Button>(R.id.digitOne)
        val digitTwo = findViewById<Button>(R.id.digitTwo)
        val digitThree = findViewById<Button>(R.id.digitThree)
        val operatorPlus = findViewById<Button>(R.id.operatorPlus)
        val digitFour = findViewById<Button>(R.id.digitFour)
        val digitFive = findViewById<Button>(R.id.digitFive)
        val digitSix = findViewById<Button>(R.id.digitSix)
        val operatorMinus = findViewById<Button>(R.id.operatorMinus)
        val digitSeven = findViewById<Button>(R.id.digitSeven)
        val digitEight = findViewById<Button>(R.id.digitEight)
        val digitNine = findViewById<Button>(R.id.digitNine)
        val operatorMultiply = findViewById<Button>(R.id.operatorMultiply)
        val buttonDecimalPoint = findViewById<Button>(R.id.buttonDecimalPoint)
        val buttonEqual = findViewById<Button>(R.id.buttonEqual)
        val operatorDivide = findViewById<Button>(R.id.operatorDivide)
        val buttonMemPlus = findViewById<Button>(R.id.buttonMemPlus)
        val buttonMemMinus = findViewById<Button>(R.id.buttonMemMinus)
        val buttonMemR = findViewById<Button>(R.id.buttonMemR)
        val buttonChangeSign = findViewById<Button>(R.id.buttonChangeSign)

        digitOne.layoutParams.width = (screenWidth*0.24).toInt()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            digitOne.layoutParams.height = (screenHeight*0.10).toInt()
        else
            digitOne.layoutParams.height = (screenHeight*0.12).toInt()
        // Rest of buttons should be the same size
        digitTwo.layoutParams.width = digitOne.layoutParams.width
        digitThree.layoutParams.width = digitOne.layoutParams.width
        operatorPlus.layoutParams.width = digitOne.layoutParams.width
        digitFour.layoutParams.width = digitOne.layoutParams.width
        digitFive.layoutParams.width = digitOne.layoutParams.width
        digitSix.layoutParams.width = digitOne.layoutParams.width
        operatorMinus.layoutParams.width = digitOne.layoutParams.width
        digitSeven.layoutParams.width = digitOne.layoutParams.width
        digitEight.layoutParams.width = digitOne.layoutParams.width
        digitNine.layoutParams.width = digitOne.layoutParams.width
        operatorMultiply.layoutParams.width = digitOne.layoutParams.width
        buttonDecimalPoint.layoutParams.width = digitOne.layoutParams.width
        digitZero.layoutParams.width = digitOne.layoutParams.width
        buttonEqual.layoutParams.width = digitOne.layoutParams.width
        operatorDivide.layoutParams.width = digitOne.layoutParams.width
        buttonMemPlus.layoutParams.width = digitOne.layoutParams.width
        buttonMemMinus.layoutParams.width = digitOne.layoutParams.width
        buttonMemR.layoutParams.width = digitOne.layoutParams.width
        buttonChangeSign.layoutParams.width = digitOne.layoutParams.width

        digitTwo.layoutParams.height = digitOne.layoutParams.height
        digitThree.layoutParams.height = digitOne.layoutParams.height
        operatorPlus.layoutParams.height = digitOne.layoutParams.height
        digitFour.layoutParams.height = digitOne.layoutParams.height
        digitFive.layoutParams.height = digitOne.layoutParams.height
        digitSix.layoutParams.height = digitOne.layoutParams.height
        operatorMinus.layoutParams.height = digitOne.layoutParams.height
        digitSeven.layoutParams.height = digitOne.layoutParams.height
        digitEight.layoutParams.height = digitOne.layoutParams.height
        digitNine.layoutParams.height = digitOne.layoutParams.height
        operatorMultiply.layoutParams.height = digitOne.layoutParams.height
        digitZero.layoutParams.height = digitOne.layoutParams.height
        buttonEqual.layoutParams.height = digitOne.layoutParams.height
        operatorDivide.layoutParams.height = digitOne.layoutParams.height
        buttonDecimalPoint.layoutParams.height = digitOne.layoutParams.height
        buttonMemPlus.layoutParams.height = digitOne.layoutParams.height
        buttonMemMinus.layoutParams.height = digitOne.layoutParams.height
        buttonMemR.layoutParams.height = digitOne.layoutParams.height
        buttonChangeSign.layoutParams.height = digitOne.layoutParams.height
     }

    // In any case of problems handling, run and return super.onOptionsItemSelected(item),
    // so that it can handle it... Not...
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_copy -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // Copy output
                val clip: ClipData = ClipData.newPlainText("Calculator output", calc.output)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(applicationContext, "Copied", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.menu_paste -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clippedItem =
                    clipboard.primaryClip?.getItemAt(0) ?: return super.onOptionsItemSelected(item)
                // Let's see if we have text that can be converted into an integer
                if (clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) == true) {
                    val copiedString = clippedItem.text.toString()
                    val value: BigDecimal
                    try {
                        value = copiedString.toBigDecimal()
                    }
                    catch (e: NumberFormatException) {
                        return super.onOptionsItemSelected(item)
                    }
                    calc.output = value.toString() // Getting rid of leading zeros
                    updateOutput()
                    return true
                }
                else return super.onOptionsItemSelected(item)
            }

            R.id.menu_backspace -> {return calc.backspace() || super.onOptionsItemSelected(item)}
            R.id.menu_easter_egg -> { return easterEgg() || super.onOptionsItemSelected(item)}
        }
        return super.onOptionsItemSelected(item)
    }

    // https://www.haaretz.co.il/captain/software/.premium-1.8961286
    private fun easterEgg(): Boolean {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clippedItem = clipboard.primaryClip?.getItemAt(0) ?: return false
        var textInClipboard =""
        when (clipboard.primaryClipDescription?.getMimeType(0)) {
            null -> return false
            MIMETYPE_TEXT_PLAIN -> textInClipboard = clippedItem.text.toString()
            MIMETYPE_TEXT_HTML -> textInClipboard = clippedItem.htmlText
            MIMETYPE_TEXT_URILIST -> textInClipboard = clippedItem.uri.toString()
            MIMETYPE_TEXT_INTENT -> textInClipboard = clippedItem.intent.toString()
            // Here's hoping Intent.toString doesn't return something long and unreadable
        }
        if (textInClipboard != "")
            Toast.makeText(applicationContext,
                "Apps can see everything you copy, anywhere: $textInClipboard", Toast.LENGTH_LONG).show()
        return true
    }
}