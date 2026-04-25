package com.mycompany.hms.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public final class Icons {

    private Icons() {}

    public static FlatSVGIcon of(String name, int size) {
        return new FlatSVGIcon("icons/" + name + ".svg", size, size);
    }

    public static FlatSVGIcon of(String name) { return of(name, 18); }
}
