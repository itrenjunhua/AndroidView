package com.renj.view.radius;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import java.util.List;

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
    // 背景颜色相关
    private ColorStateList mBgColorsList;
    private Shader mBgShader; // 渐变优先级更高
    private Path mBgPath;
    private Paint mBgPaint;
    private PorterDuffColorFilter mBgTintFilter;
    private ColorStateList mBgTint;
    private PorterDuff.Mode mBgTintMode;
    // 边框线相关
    private boolean mDrawSolid;
    private ColorStateList mSolidColorsList;
    private Shader mSolidShader; // 渐变优先级更高
    private List<Path> mSolidPath;
    private Paint mSolidPaint;
    private PorterDuffColorFilter mSolidTintFilter;
    private ColorStateList mSolidTint;
    private PorterDuff.Mode mSolidTintMode;

    public RadiusDrawable(ColorStateList bgColorsList, Shader bgShader, Path path) {
        this(bgColorsList, bgShader, path, 0, null, null, null, null);
    }

    public RadiusDrawable(ColorStateList bgColorsList, Shader bgShader, Path path, int solidWidth, ColorStateList solidColorsList,
                          Shader solidShader, List<Path> solidPath, DashPathEffect dashPathEffect) {
        this.mBgPath = path;
        this.mBgTintMode = PorterDuff.Mode.SRC_IN;
        this.mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mBgPaint.setDither(true);
        this.mBgShader = bgShader;

        this.mDrawSolid = solidWidth > 0;
        if (mDrawSolid) {
            this.mSolidPath = solidPath;
            this.mSolidTintMode = PorterDuff.Mode.SRC_IN;
            this.mSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.mSolidPaint.setDither(true);
            this.mSolidPaint.setStyle(Paint.Style.STROKE);
            this.mSolidPaint.setStrokeWidth(solidWidth);
            this.mSolidPaint.setPathEffect(dashPathEffect);
            this.mSolidShader = solidShader;
        }

        this.setBackground(bgColorsList, solidColorsList);
    }

    void setBgShader(Shader shader) {
        if (shader != null) {
            this.mBgShader = shader;
            this.invalidateSelf();
        }
    }

    void setBackground(ColorStateList bgColorsList, ColorStateList solidColorsList) {
        if (mBgShader == null) {
            this.mBgColorsList = bgColorsList == null ? ColorStateList.valueOf(0) : bgColorsList;
            this.mBgPaint.setColor(this.mBgColorsList.getColorForState(this.getState(), this.mBgColorsList.getDefaultColor()));
        }
        if (mDrawSolid && mSolidShader == null) {
            this.mSolidColorsList = solidColorsList == null ? ColorStateList.valueOf(0) : solidColorsList;
            this.mSolidPaint.setColor(this.mSolidColorsList.getColorForState(this.getState(), this.mSolidColorsList.getDefaultColor()));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Paint bgPaint = this.mBgPaint;
        boolean bgClearColorFilter = false;
        boolean hasBgShader = mBgShader != null;
        if (hasBgShader) {
            bgPaint.setShader(mBgShader);
        } else {
            if (this.mBgTintFilter != null) {
                bgPaint.setColorFilter(this.mBgTintFilter);
                bgClearColorFilter = true;
            } else {
                bgClearColorFilter = false;
            }
        }

        canvas.drawPath(mBgPath, bgPaint);
        if (bgClearColorFilter) {
            bgPaint.setColorFilter(null);
        }


        if (mDrawSolid) {
            Paint solidPaint = this.mSolidPaint;
            boolean solidClearColorFilter = false;
            boolean hasSolidShader = mSolidShader != null;
            if (hasSolidShader) {
                solidPaint.setShader(mSolidShader);
            } else {
                if (this.mSolidTintFilter != null) {
                    solidPaint.setColorFilter(this.mSolidTintFilter);
                    solidClearColorFilter = true;
                } else {
                    solidClearColorFilter = false;
                }
            }

            for (Path solidPath : mSolidPath) {
                canvas.drawPath(solidPath, solidPaint);
            }
            if (solidClearColorFilter) {
                solidPaint.setColorFilter(null);
            }
        }

    }

    @Override
    public void setAlpha(int alpha) {
        if (mBgShader == null) {
            this.mBgPaint.setAlpha(alpha);
        }

        if (mDrawSolid) {
            this.mSolidPaint.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mBgShader == null) {
            this.mBgPaint.setColorFilter(cf);
        }

        if (mDrawSolid) {
            this.mSolidPaint.setColorFilter(cf);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setColor(@Nullable ColorStateList backgroundColor, @Nullable ColorStateList solidColorsList) {
        this.setBackground(backgroundColor, solidColorsList);
        this.invalidateSelf();
    }

    public ColorStateList getBackGroundColor() {
        return this.mBgColorsList;
    }

    public ColorStateList getSolidColor() {
        return this.mSolidColorsList;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        if (mBgShader == null) {
            this.mBgTint = tint;
            this.mBgTintFilter = this.createTintFilter(this.mBgTint, this.mBgTintMode);
        }

        if (mDrawSolid) {
            this.mSolidTint = tint;
            this.mSolidTintFilter = this.createTintFilter(this.mSolidTint, this.mSolidTintMode);
        }

        this.invalidateSelf();
    }

    @Override
    public void setTintMode(PorterDuff.Mode tintMode) {
        if (mBgShader == null) {
            this.mBgTintMode = tintMode;
            this.mBgTintFilter = this.createTintFilter(this.mBgTint, this.mSolidTintMode);
        }

        if (mDrawSolid && mSolidShader == null) {
            this.mSolidTintMode = tintMode;
            this.mSolidTintFilter = this.createTintFilter(this.mBgTint, this.mSolidTintMode);
        }

        this.invalidateSelf();
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean bgColorChanged = false;
        if (mBgShader == null) {
            int newBgColor = this.mBgColorsList.getColorForState(stateSet, this.mBgColorsList.getDefaultColor());
            bgColorChanged = newBgColor != this.mBgPaint.getColor();
            if (bgColorChanged) {
                this.mBgPaint.setColor(newBgColor);
            }
        }

        boolean solidColorChanged = false;
        if (mDrawSolid && mSolidShader == null) {
            int newSolidColor = this.mSolidColorsList.getColorForState(stateSet, this.mSolidColorsList.getDefaultColor());
            solidColorChanged = newSolidColor != this.mSolidPaint.getColor();
            if (solidColorChanged) {
                this.mSolidPaint.setColor(newSolidColor);
            }
        }


        if (this.mBgShader == null && this.mBgTint != null && this.mBgTintMode != null) {
            this.mBgTintFilter = this.createTintFilter(this.mBgTint, this.mBgTintMode);
            this.invalidateSelf();
            return true;
        } else if (this.mSolidShader == null && this.mSolidTint != null && this.mSolidTintMode != null) {
            this.mSolidTintFilter = this.createTintFilter(this.mSolidTint, this.mSolidTintMode);
            return true;
        } else {
            return bgColorChanged || solidColorChanged;
        }
    }

    @Override
    public boolean isStateful() {
        if (mDrawSolid) {
            return this.mBgTint != null && this.mBgTint.isStateful()
                    || this.mBgColorsList != null && this.mBgColorsList.isStateful()
                    || this.mSolidTint != null && this.mSolidTint.isStateful()
                    || this.mSolidColorsList != null && this.mSolidColorsList.isStateful()
                    || super.isStateful();
        } else {
            return this.mBgTint != null && this.mBgTint.isStateful()
                    || this.mBgColorsList != null && this.mBgColorsList.isStateful()
                    || super.isStateful();
        }
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
