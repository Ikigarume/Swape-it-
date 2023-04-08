package com.example.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyDividerItem extends RecyclerView.ItemDecoration {

    //Step 1 : Variables
    private static final int[] ATR = new int[] {
            android.R.attr.listDivider
    };
    public static final int Horizontal_list = LinearLayoutManager.HORIZONTAL;
    public static final int Vertical_list = LinearLayoutManager.VERTICAL;
    private Drawable myDivider ;
    private int myOrientation ;
    private Context context ;
    private int margin ;

    //Step 2 : Constructor
    public MyDividerItem(int orientation, Context context, int margin){
        this.context = context ;
        this.margin = margin ;
        final TypedArray a = context.obtainStyledAttributes(ATR);
        myDivider = a.getDrawable(0);
        a.recycle();
        //myDivider.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        myDivider = new ShapeDrawable(new RectShape());
        ((ShapeDrawable) myDivider).getPaint().setColor(Color.GRAY);
        ((ShapeDrawable) myDivider).getPaint().setStyle(Paint.Style.STROKE);
        ((ShapeDrawable) myDivider).getPaint().setStrokeWidth(3);
        ((ShapeDrawable) myDivider).getPaint().setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        //Step3 : create a methode to set the orientation
        setOrientation(orientation);
    }

    private void setOrientation(int orientation) {
        if(myOrientation != Horizontal_list && myOrientation != Vertical_list){
            throw new IllegalArgumentException("invalid");
        }
        myOrientation = orientation ;
    }

    //steop 4: let's override some methods :
    @Override
    public void onDrawOver(@NonNull Canvas c,@NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state){
        if(myOrientation == Vertical_list){
            drawVerticalRecycler(c,parent);
        }
        else {
            drawHorizontalRecycler(c,parent);
        }
    }



    private void drawVerticalRecycler(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i=0 ; i<childCount ; i++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin ;
            final int bottom = top + myDivider.getIntrinsicHeight() ;
            myDivider.setBounds(left + dpToPx(margin), top, right - dpToPx(margin), bottom);
            myDivider.draw(c);

        }

    }

    private void drawHorizontalRecycler(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i=0 ; i<childCount ; i++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin ;
            final int right = left + myDivider.getIntrinsicHeight() ;
            myDivider.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin));
            myDivider.draw(c);

        }

    }



    //Step 5 : this methode will convert eh DP to PX
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics()));
    }

    //step6 : This methode ll get the item margin
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(myOrientation == Vertical_list){
            outRect.set(0,0,0,myDivider.getIntrinsicHeight());
        } else {
            outRect.set(0,0,myDivider.getIntrinsicWidth(),0);
        }
    }



}
