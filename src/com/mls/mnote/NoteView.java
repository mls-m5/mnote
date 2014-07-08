package com.mls.mnote;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

public class NoteView extends View{

	private static Paint paint;
	private static Paint paintFlag;

	final static int widthUnit = 20;
	final static int heightUnit = 20;
	final static int noteHeight = heightUnit * 5;

	static private Path[] paths;
	static Path flagPath;
	
	static double noteValue = 1./4.;
	List<Note>noteList = new LinkedList<Note>();
	
	static NoteView globalNoteView;
	
	static double scale = .5;
	
	public static final int NOTESTYLE_M = 0;
	public static final int NOTESTYLE_CLASSIC = 1;
	static boolean showClaves = true;
	static int NoteStyle = NOTESTYLE_CLASSIC;
	
	public NoteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		tryInitStaticStuff();
		globalNoteView = this;
			
		noteList.add(new Note(1, .25));
		noteList.add(new Note(4, .25));
		noteList.add(new Note(2, 1./8));
		noteList.add(new Note(7, .25));
		
		checkForPreferenceChange(context);
		
		setMinimumHeight((int) ((noteHeight + heightUnit * 2) * scale));
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if (globalNoteView == this){
			globalNoteView = null;
		}
		super.onDetachedFromWindow();
	}
	
	private static void tryInitStaticStuff() {
		if (paths != null) return;
		paths = new Path[7];
		synchronized (paths) {
			paint = new Paint();
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);
			paint.setStrokeCap(Paint.Cap.ROUND);


			paintFlag = new Paint(paint);
			paintFlag.setStyle(Paint.Style.FILL_AND_STROKE);

			if (paths != null);
			paths[0] = new Path();
			paths[0].moveTo(0, 0);

			paths[1] = new Path(){
				{
					moveTo(0, 0);
					arcTo(new RectF(0, -heightUnit, widthUnit, 0), 180 , 180);
					lineTo(widthUnit, 0);
				}
			};

			paths[2] = new Path(paths[1]){
				{
					arcTo(new RectF(widthUnit, -heightUnit, widthUnit * 2, 0), 180 , 180);
					lineTo(widthUnit * 2, 0);
				}
			};

			paths[3] = new Path(){
				{
					moveTo(0, 0);
					lineTo(-widthUnit, -heightUnit);
					lineTo(widthUnit, -heightUnit);
				}
			};

			paths[4] = new Path(){
				{
					moveTo(0, 0);
					lineTo(widthUnit, 0);
					arcTo(new RectF(widthUnit, -heightUnit, widthUnit * 2, 0), 180, 180);
					lineTo(widthUnit * 2, 0);
				}
			};

			paths[5] = new Path();
			paths[5].moveTo(0, 0);
			paths[5].arcTo(new RectF(0, -heightUnit / 3, widthUnit, heightUnit / 3), 180, 359);

			paths[6] = new Path();
			paths[6].moveTo(0, 0);
			paths[6].lineTo(widthUnit, -heightUnit / 2);


			flagPath = new Path();
			flagPath.moveTo(0, 0);
			flagPath.lineTo(widthUnit * 2, heightUnit);
			flagPath.lineTo(0, heightUnit / 2);
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.scale((float)scale, (float) scale);
		double drawX = widthUnit;
		double drawY = noteHeight + heightUnit;
		
		if (showClaves){
			for (int i = 0; i < 5; ++i){
				float y = heightUnit * i + noteHeight;
				canvas.drawLine(0, y, getWidth(), y, paint);
			}
		}

		for (Note n: noteList){
			double offsetY = 0;
			boolean flipY = false;
			if (showClaves){
				offsetY = -n.note * heightUnit / 2;
				if (offsetY > 1){
					flipY = true;
				}

			}
			drawNote(canvas, drawX, drawY + offsetY, n.note, n.value, false, flipY);
			double width = n.value * widthUnit * 16;
			if (width < widthUnit * 2.2){
				width = widthUnit * 2.2;
			}
			drawX += width;
			if (drawX > getWidth() / scale){
				drawX = widthUnit;
				drawY += noteHeight + heightUnit;
			}
			
		}
		canvas.restore();
		
		
		super.draw(canvas);
	}
	

	static RectF noteRect = new RectF(-(float)(heightUnit * 1.1), 0, 0, (float)heightUnit);
	
	public static void drawNote(Canvas canvas, double x, double y, int noteNum, double noteLength, boolean centerMiddle, boolean flip) {
		tryInitStaticStuff();
		canvas.save();
		canvas.translate((float)x, (float)y);
		if (flip){
			canvas.rotate(180);
		}
		
		if (centerMiddle){
			canvas.translate(0, noteHeight / 2);
		}
		
		noteNum = noteNum % 7;
		if (NoteStyle == NOTESTYLE_M){
			canvas.drawPath(paths[noteNum], paint);
		}
		else if (NoteStyle == NOTESTYLE_CLASSIC){
			canvas.drawArc(noteRect, 0, 360, true, paint); //TODO: Add skewing to make it look better
		}
		
		int partial = (int) (1 / noteLength);
		int renderNum = (int) (Math.log(partial) / Math.log(2));

		if (partial == 2){
			canvas.drawLine((float)-widthUnit / 2, -(float)(heightUnit * 1.5), (float)(widthUnit / 2), -(float)(heightUnit * 1.5), paint);
		}

		if (partial > 1){
			canvas.drawLine(0, -noteHeight, 0, 0, paint);
			
			canvas.save();
			canvas.translate(0, -noteHeight);
			for (int i = 0; i < renderNum - 2; ++i){
				canvas.drawPath(flagPath, paintFlag);
				canvas.translate(0, heightUnit);
			}
			canvas.restore();
		}
		else{
			canvas.drawLine(0, -heightUnit, 0, 0, paint);
		}
		

		canvas.restore();
	}

	public static void setNoteValue(double noteValue) {
		NoteView.noteValue = noteValue;
	}
	
	public static void createNote(int note){
		if (globalNoteView != null){
			globalNoteView.noteList.add(new Note(note, noteValue));
			globalNoteView.postInvalidate();
		}
	}

	public static void save() {
		if (globalNoteView != null){
			FileManager.save("apa.bepa.note", globalNoteView.getDataString());
		}
	}
	
	public void load(String name){
		String input = FileManager.load("apa.bepa.note");
		if (input != null){
			parseString(input);
		}
	}
	
	private void parseString(String load) {
		
	}

	public String getDataString(){
		return "a1 b4";
	}

	public static void checkForPreferenceChange(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		boolean isClassicStyle = sharedPreferences.getBoolean("pref_classic_mode", true);

		if (isClassicStyle){
			NoteStyle = NOTESTYLE_CLASSIC;
			showClaves = true;
		}
		else{
			NoteStyle = NOTESTYLE_M;
			showClaves = false;
		}
		
		globalNoteView.postInvalidate();

	}
}
