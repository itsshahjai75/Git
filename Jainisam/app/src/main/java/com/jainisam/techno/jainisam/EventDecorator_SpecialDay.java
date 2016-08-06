package com.jainisam.techno.jainisam;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator_SpecialDay implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private final Drawable drawable;

    public EventDecorator_SpecialDay(int color, Collection<CalendarDay> dates,Activity context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        drawable = context.getResources().getDrawable(R.drawable.my_selector);


    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(R.drawable.ic_my_selector);
        view.setBackgroundDrawable(drawable);

    }
}
