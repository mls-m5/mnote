package com.mls.mnote;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

class NoteButton extends Button{
	int noteNum;
	double noteValue;
	
	public NoteButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		noteNum = -1;
		noteValue = 1. / 8;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		canvas.save();
		canvas.translate(getWidth() / 2, getHeight() / 2);
		float scale = (float)getHeight() / NoteView.noteHeight / 2;
		canvas.scale(scale, scale);

		if (noteNum != -1){
			NoteView.drawNote(canvas, 0, 0, noteNum, noteValue, true, false);
		}
		else{
			NoteView.drawNote(canvas, 0, 0, 1, noteValue, true, false);
		}
		canvas.restore();
	}

}