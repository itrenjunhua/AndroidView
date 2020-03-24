package com.renj.view.radius;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-25   0:49
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RadiusDrawable extends Drawable {
    private ColorStateList mBackground;
    private Path mPath;
    private final Paint mPaint;
    private PorterDuffColorFilter mTintFilter;
    private ColorStateList mTint;
    private PorterDuff.Mode mTintMode;

    RadiusDrawable(ColorStateList backgroundColor, Path path) {
        this.mPath = path;
        this.mTintMode = PorterDuff.Mode.SRC_IN;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.setBackground(backgroundColor);
    }

    private void setBackground(ColorStateList color) {
        this.mBackground = color == null ? ColorStateList.valueOf(0) : color;
        this.mPaint.setColor(this.mBackground.getColorForState(this.getState(), this.mBackground.getDefaultColor()));
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = this.mPaint;
        boolean clearColorFilter;
        if (this.mTintFilter != null && paint.getColorFilter() == null) {
            paint.setColorFilter(this.mTintFilter);
            clearColorFilter = true;
        } else {
            clearColorFilter = false;
        }

        canvas.drawPath(mPath, paint);
        if (clearColorFilter) {
            paint.setColorFilter((ColorFilter) null);
        }

    }

    @Override
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setColor(@Nullable ColorStateList color) {
        this.setBackground(color);
        this.invalidateSelf();
    }

    public ColorStateList getColor() {
        return this.mBackground;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        this.mTint = tint;
        this.mTintFilter = this.createTintFilter(this.mTint, this.mTintMode);
        this.invalidateSelf();
    }

    @Override
    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mTintMode = tintMode;
        this.mTintFilter = this.createTintFilter(this.mTint, this.mTintMode);
        this.invalidateSelf();
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        int newColor = this.mBackground.getColorForState(stateSet, this.mBackground.getDefaultColor());
        boolean colorChanged = newColor != this.mPaint.getColor();
        if (colorChanged) {
            this.mPaint.setColor(newColor);
        }

        if (this.mTint != null && this.mTintMode != null) {
            this.mTintFilter = this.createTintFilter(this.mTint, this.mTintMode);
            //this.invalidateSelf();
            return true;
        } else {
            return colorChanged;
        }
    }

    @Override
    public boolean isStateful() {
        return this.mTint != null && this.mTint.isStateful() || this.mBackground != null && this.mBackground.isStateful() || super.isStateful();
    }

    private PorterDuffColorFilter createTintFilter(ColorStateList tint, PorterDuff.Mode tintMode) {
        if (tint != null && tintMode != null) {
            int color = tint.getColorForState(this.getState(), 0);
            return new PorterDuffColorFilter(color, tintMode);
        } else {
            return null;
        }
    }
}
