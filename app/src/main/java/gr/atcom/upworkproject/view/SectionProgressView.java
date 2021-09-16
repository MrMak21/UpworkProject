package gr.atcom.upworkproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.jetbrains.annotations.NotNull;

import gr.atcom.upworkproject.R;


public class SectionProgressView extends ConstraintLayout {

    private float step;


    private int stepsCount;
    private int currentStep;
    private int backgroundColor;
    private Context context;

    public SectionProgressView(@NonNull @NotNull Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public SectionProgressView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public SectionProgressView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    private void init(Context context, AttributeSet attributeSet) {
        inflate(context, R.layout.custom_view_layout, this);

        if (attributeSet != null) {
            TypedArray typedArray = context.getTheme()
                    .obtainStyledAttributes(attributeSet, R.styleable.SectionProgressView, 0, 0);
            try {
                backgroundColor = typedArray.getColor(R.styleable.SectionProgressView_background_color, 0);
                stepsCount = typedArray.getInt(R.styleable.SectionProgressView_steps_count, 0);
                currentStep = typedArray.getInt(R.styleable.SectionProgressView_current_step, 0);

                setViews();

            } finally {
                typedArray.recycle();
            }
            step = currentStep;
        }


    }

    private void setViews() {
        ConstraintLayout mainContainer = findViewById(R.id.mainContainer);
        LinearLayout container = findViewById(R.id.container);
        mainContainer.setBackgroundColor(backgroundColor);

        for (int i = 0; i < stepsCount; i++) {


            ImageView imageView = new ImageView(context);
            if (i < currentStep - 1) {
                imageView.setImageResource(R.drawable.circle_white_shape);
            } else if (i == currentStep - 1) {
                imageView.setImageResource(R.drawable.circle_white_selected_shape);
            } else {
                imageView.setImageResource(R.drawable.circle_grey_shape);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (i != 0) {
//                params.setMarginStart(-1);
            }
            params.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(params);
            container.addView(imageView);

            if (i < stepsCount - 1) {
                View view = new View(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new LayoutParams(100, 5));
                lp.gravity = Gravity.CENTER_VERTICAL;
//                lp.setMarginStart(-1);
                view.setLayoutParams(lp);
                if (i >= currentStep - 1) {
                    view.setBackgroundColor(getResources().getColor(R.color.grey_tundora));
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                container.addView(view);
            }

        }
    }

}
