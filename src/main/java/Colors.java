import java.awt.*;

final class Colors {

    static final Color ordinary      = new Color(255,255,255);
    static final Color fixed         = new Color(0,0,0);
    static final Color selectedNode  = new Color(255,0,0);
    static final Color selectedKnot  = new Color(255,200,200);

    static final Color[] uniques = new Color[] {
            new Color(0,255,255),
            new Color(20,0,240),
            new Color(40,64,220),
            new Color(60,128,200),
            new Color(80,192,180),
            new Color(100,255,160),
            new Color(120,192,140),
            new Color(140,128,120),
            new Color(160,64,100),
            new Color(180,0,80),
            new Color(200,64,60),
            new Color(220,128,40),
            new Color(240,192,20),
            new Color(255,255,0),
    };

    static Color unique(int number) {
        return uniques[number % uniques.length];
    }
}
