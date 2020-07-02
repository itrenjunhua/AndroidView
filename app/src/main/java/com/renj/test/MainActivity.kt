package com.renj.test

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.renj.view.CenterItemView
import com.renj.view.TitleView
import com.renj.view.dialog.CustomDialog
import com.renj.view.radius.RadiusFrameLayout
import com.renj.view.radius.RadiusImageView
import com.renj.view.radius.ShaderUtils

internal class MainActivity : AppCompatActivity() {
    private lateinit var showDialog: RadiusFrameLayout
    private lateinit var centerItemView: CenterItemView
    private lateinit var textView: TextView
    private lateinit var imageView2: RadiusImageView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDialog = findViewById(R.id.fl_radius)
        centerItemView = findViewById(R.id.cv_item_view)
        textView = findViewById(R.id.textView)
        imageView2 = findViewById(R.id.imageView2)

        textView.setOnClickListener {
            showDialog.setRadius(50)
            // showDialog.setBackgroundColor(Color.BLUE)
            showDialog.setShaderInfo(ShaderUtils.SHADER_TYPE_LINEAR, intArrayOf(Color.YELLOW, Color.GREEN))

            // 修改图片圆角
            imageView2.setRadius(12)

            centerItemView.modifyNameText("修改后内容")
                    .modifyNameTextColor(Color.parseColor("#FF0000"))
                    .modifyValueText("RenJunhua")
                    .applyModifyAllInfo() // 应用修改信息
        }

        centerItemView.setOnClickListener {
            Toast.makeText(this@MainActivity, "点击", Toast.LENGTH_SHORT).show()
        }

        var customDialogListener = CustomDialog.newInstance(this@MainActivity)
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
        showDialog.setOnClickListener {
            customDialogListener.show()
        }


        // TitleView
        var titleViewOne = findViewById<TitleView>(R.id.title_view_one)
        var titleViewTwo = findViewById<TitleView>(R.id.title_view_two)

        titleViewOne.setOnRightTextClickListener { Toast.makeText(this@MainActivity, "右边文字", Toast.LENGTH_SHORT).show() }

        titleViewTwo.setTitleContent("标题2")
        titleViewTwo.setRightImgShow(true)
        titleViewTwo.setRightImageIcon(R.mipmap.ic_launcher)
        titleViewTwo.setOnBackViewClickListener { Toast.makeText(this@MainActivity, "返回", Toast.LENGTH_SHORT).show() }
        titleViewTwo.setOnRightImageClickListener { Toast.makeText(this@MainActivity, "右边图片", Toast.LENGTH_SHORT).show() }
    }
}
