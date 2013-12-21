package za.co.storycheck.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import za.co.storycheck.R;

public class DrawablePie extends View {

	private float imageWidth = 384;
	private float imageHeight = 384;
	private float imageTopPad = 16;
	private float imageLeftPad = 16;
	private float imagePieRadius = 177;
	private float imagePieWidth = imagePieRadius * 2;
	private RectF pieRect = new RectF(imageLeftPad, imageTopPad, imageLeftPad
			+ imagePieWidth, imageTopPad + imagePieWidth);
	private Rect scaledPieRect;
	private Bitmap pieBm;
	private Drawable overlay;
	private Paint backgroundPaint = new Paint();
	private int colors[] = new int[11];
	private Drawable dots[] = new Drawable[colors.length];
	private float graphicsRatio;
	private Point center;
	private float values[] = new float[] { 30, 25, 20, 15, 10, 30 };
	private int colorIndexes[] = new int[] { 0, 1, 2, 3, 4, 10 };

	public DrawablePie(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public DrawablePie(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DrawablePie(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		overlay = context.getResources().getDrawable(R.drawable.pie_overlay);
		overlay.setDither(true);
		colors[0] = Color.argb(255, 145, 196, 0);
		colors[1] = Color.argb(255, 233, 125, 0);
		colors[2] = Color.argb(255, 185, 185, 185);
		colors[3] = Color.argb(255, 250, 126, 255);
		colors[4] = Color.argb(255, 35, 118, 203);
		colors[5] = Color.argb(255, 216, 255, 0);
		colors[6] = Color.argb(255, 110, 52, 21);
		colors[7] = Color.argb(255, 0, 185, 83);
		colors[8] = Color.argb(255, 255, 108, 0);
		colors[9] = Color.argb(255, 0, 240, 255);
		colors[10] = Color.argb(255, 170, 176, 193);
		for (int x = 0; x < colors.length; x++) {
			dots[x] = createDot(colors[x]);
		}
		regeneratePieBm();
	}

	private Drawable createDot(int color) {
		int width = (int) (15 * getResources().getDisplayMetrics().scaledDensity);
		Bitmap bm = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bm);
		RectF pieRect = new RectF(0, 0, width, width);
		Path path = new Path();
		path.addArc(pieRect, 0, 360);
		Paint p = new Paint();
		p.setColor(color);
		// p.setDither(true);
		p.setAntiAlias(true);
		p.setStyle(Style.FILL);
		canvas.drawPath(path, p);
		return new BitmapDrawable(bm);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(graphicsRatio, graphicsRatio);
		if (pieBm != null) {
			canvas.save();
			canvas.rotate(0, imagePieRadius + imageLeftPad,
					imagePieRadius + imageTopPad);
			canvas.drawBitmap(pieBm, imageLeftPad, imageTopPad, backgroundPaint);
			canvas.restore();
		}
		overlay.draw(canvas);
		canvas.restore();
	}

	private void drawPie(Canvas canvas) {
		float total = 0f;
		for (float i : values) {
			total += i;
		}
		float[] relVal = new float[values.length];
		for (int i = 0; i < relVal.length; i++) {
			relVal[i] = values[i] / total;
		}
		float pos = 0;
		for (int i = 0; i < relVal.length; i++) {
			Path path = new Path();
			path.moveTo(imagePieRadius, imagePieRadius);
			float angle = 360 * relVal[i];
			RectF pieRect = new RectF(0, 0, imagePieWidth, imagePieWidth);
			path.addArc(pieRect, pos, angle);
			path.lineTo(imagePieRadius, imagePieRadius);
			Paint p = new Paint();
			p.setColor(colors[colorIndexes[i]]);
			// p.setDither(true);
			p.setAntiAlias(true);
			p.setStyle(Style.FILL);
			canvas.drawPath(path, p);
			// p.setColor(darkerColor(colors[i]));
			// p.setStrokeWidth(3);
			// p.setStyle(Style.STROKE);
			// canvas.drawPath(path, p);
			pos += angle;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		recalc(0, 0, w, h);
	}

	private void regeneratePieBm() {
		// free the old bitmap
		if (pieBm != null) {
			pieBm.recycle();
		}

		pieBm = Bitmap.createBitmap((int) imagePieWidth, (int) imagePieWidth,
				Bitmap.Config.ARGB_8888);
		Canvas backgroundCanvas = new Canvas(pieBm);
		drawPie(backgroundCanvas);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		recalc(left, top, right, bottom);
	}

	private void recalc(int left, int top, int right, int bottom) {
		float widthRatio = (right - left) / imageWidth;
		float heightRatio = (bottom - top) / imageHeight;
		graphicsRatio = Math.max(widthRatio, heightRatio);
		scaledPieRect = new Rect((int) (pieRect.left * graphicsRatio)-2,
				(int) (pieRect.top * graphicsRatio)-2,
				(int) (pieRect.right * graphicsRatio)+2,
				(int) (pieRect.bottom * graphicsRatio)+2);
		Rect thisRect = new Rect(0, 0, (int) imageWidth, (int) imageHeight);
		overlay.setBounds(thisRect);
		center = new Point((int) (imagePieRadius* graphicsRatio + imageLeftPad* graphicsRatio),
				(int) (imagePieRadius* graphicsRatio + imageTopPad* graphicsRatio));

	}

	public void setPieVlaues(int passedValues[]) {
		values = new float[Math.min(passedValues.length, 6)];
		colorIndexes = new int[values.length];
		for (int x = 0; x < passedValues.length; x++) {

            int index = Math.min(x, 5);
            values[index] += passedValues[x];
			colorIndexes[index] = index;
		}
		if (colorIndexes.length == 6) {
			colorIndexes[5] = 10;
		}
		regeneratePieBm();
		invalidate(scaledPieRect);
	}

	public Drawable getDot(int colorIndex) {
		return dots[colorIndex];
	}
}
