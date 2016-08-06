package healthportfolios.techno.healthportfolios;


import android.app.Application;

public final class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "ProductSans-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "ProductSans-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "ProductSans-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "ProductSans-Regular.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/Roboto-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

    }
}