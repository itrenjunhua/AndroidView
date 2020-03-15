package com.renj.test

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.renj.view.CenterItemView
import com.renj.view.dialog.CustomDialog

internal class MainActivity : AppCompatActivity() {
    private lateinit var showDialog: Button
    private lateinit var centerItemView: CenterItemView
    private lateinit var textView: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDialog = findViewById(R.id.bt_show_dialog)
        centerItemView = findViewById(R.id.cv_item_view)
        textView = findViewById(R.id.textView)

        textView.setOnClickListener {
            centerItemView.modifyNameText("修改后内容")
                    .modifyNameTextColor(Color.parseColor("#FF0000"))
                    .modifyValueText("RenJunhua")
                    .applyModifyAllInfo() // 应用修改信息
        }

        centerItemView.setOnClickListener {
            Toast.makeText(this@MainActivity, "点击", Toast.LENGTH_SHORT).show()
        }

        showDialog.setOnClickListener {
            CustomDialog.newInstance(this@MainActivity)
                    .isShowTitle(true)
                    .setTitleContent("自定义标题")
                    .setDialogContent("自定义对话框内容")
                    .setCancelText("取消")
                    .setConfirmText("确定")
                    .setCanceledOnTouchOut(false)
                    .setCustomDialogListener(object : CustomDialog.CustomDialogListener {
                        override fun onCancel(dialog: CustomDialog) {
                            Toast.makeText(this@MainActivity, "取消", Toast.LENGTH_SHORT).show()
                        }

                        override fun onConfirm(dialog: CustomDialog) {
                            Toast.makeText(this@MainActivity, "确定", Toast.LENGTH_SHORT).show()
                        }
                    })
                    .show()
        }
    }
}
