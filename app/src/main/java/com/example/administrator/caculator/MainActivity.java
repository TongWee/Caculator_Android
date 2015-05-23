package com.example.administrator.caculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;


public class MainActivity extends Activity {
    //标志
    private boolean new_input;
    private boolean clearflag;
    private String last_btn;
    private boolean outrange;
    //控件
    private Button[] btnNum = new Button[11];
    private Button[] btnCommand = new Button[5];
    private TextView text_result = null;//显示内容
    private Button btnCLear;
    //运算
    private String last_command = null;
    private String m_input = null;
    private double num1, num2, result;

    //构造函数进行初始化
    public MainActivity() {
        clear();
    }

    private void clear() {
        outrange=true;
        new_input = true;
        last_btn = "";
        last_command = "";
        m_input = "";
        num1 = 0;
        num2 = 0;
        result = 0;
        clearflag = false;
    }

    //数字按钮监听类
    private class NumberAction implements OnClickListener {
        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            String input_str = btn.getText().toString();
            last_btn = input_str;
            if (clearflag)
            {
                clear();
            }
            if(text_result.getText().toString().startsWith("0")&&!text_result.getText().toString().startsWith("0.")&&!input_str.equals("."))
                return;
            if(text_result.getText().toString().length()>19)
            {
                Toast.makeText(getApplicationContext(), "超出最大位数20", Toast.LENGTH_LONG).show();
                return;
            }
            if (!input_str.equals(".")) {//是数字
                if (new_input)
                {
                    m_input = m_input + input_str;
                    new_input = false;
                }
            } else {
                if (new_input) {
                    if (!m_input.contains(".")) {
                        if (text_result.getText().toString().equals("") || text_result.getText().toString().equals("0"))
                            m_input = "0.";
                        else {
                            m_input = m_input + ".";
                            if (m_input.equals("."))
                                m_input = "0.";
                        }
                    }
                    new_input = false;
                }
            }
            if (new_input == false) {
                new_input = true;
                text_result.setText(m_input);
            }
        }
    }
    //运算符按钮监听类
    public class CommandAction implements OnClickListener {
        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            String temp = text_result.getText().toString();
            String input_str = btn.getText().toString();
            if (!input_str.equals("=")) {//如果运算符不是等号
                if(last_btn.equals(""))
                    return;
                if(last_btn.equals("="))
                {
                    clear();
                    text_result.setText("");
                    return;
                }
                num1 = Double.parseDouble(text_result.getText().toString());
                last_command = input_str;
                new_input = true;
                outrange = false;
                m_input="";
                last_btn = input_str;
            } else if (last_command != null) {//如果运算符是等号
                if (text_result.getText().toString().equals(""))
                    num2 = 0;
                else
                    num2 = Double.parseDouble(text_result.getText().toString());
                switch (last_command) {
                    case "+":
                        result = add(num1, num2);
                        text_result.setText(result + "");
                        last_command = "";
                        break;
                    case "-":
                        result = sub(num1, num2);
                        text_result.setText(result + "");
                        last_command = "";
                        break;
                    case "*":
                        result = mul(num1, num2);
                        text_result.setText(result + "");
                        last_command = "";
                        break;
                    case "/":
                        if (num2 == 0) {
                            Toast.makeText(getApplicationContext(), "除数不能为零", Toast.LENGTH_LONG).show();
                            //text_result.setText("Error");
                            result = 0;
                        } else {
                            result = div(num1, num2, 9);
                            text_result.setText(result + "");
                        }
                        last_command = "";
                        break;
                    default:
                        text_result.setText(temp);
                        break;
                }
                new_input = true;
                m_input = "";
                last_command = "";
                clearflag = true;
                last_btn = "=";
            } else
                text_result.setText(temp);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取运算符
        btnCommand[0] = (Button) findViewById(R.id.add);
        btnCommand[1] = (Button) findViewById(R.id.minus);
        btnCommand[2] = (Button) findViewById(R.id.mutiply);
        btnCommand[3] = (Button) findViewById(R.id.devide);
        btnCommand[4] = (Button) findViewById(R.id.equal);
        //获取数字
        btnNum[0] = (Button) findViewById(R.id.zero);
        btnNum[1] = (Button) findViewById(R.id.one);
        btnNum[2] = (Button) findViewById(R.id.two);
        btnNum[3] = (Button) findViewById(R.id.three);
        btnNum[4] = (Button) findViewById(R.id.four);
        btnNum[5] = (Button) findViewById(R.id.five);
        btnNum[6] = (Button) findViewById(R.id.six);
        btnNum[7] = (Button) findViewById(R.id.seven);
        btnNum[8] = (Button) findViewById(R.id.eight);
        btnNum[9] = (Button) findViewById(R.id.nine);
        btnNum[10] = (Button) findViewById(R.id.point);
        //初始化显示区域
        text_result = (TextView) findViewById(R.id.text_result);
        text_result.setText("");
        //监听对象实例化
        btnCLear = (Button) findViewById(R.id.clear);
        NumberAction na = new NumberAction();
        CommandAction ca = new CommandAction();
        for (Button bc : btnCommand) {
            bc.setOnClickListener(ca);
        }
        for (Button bn : btnNum) {
            bn.setOnClickListener(na);
        }
        btnCLear = (Button) findViewById(R.id.clear);
        btnCLear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                text_result.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Caculator Beta 1.0", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2, int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
