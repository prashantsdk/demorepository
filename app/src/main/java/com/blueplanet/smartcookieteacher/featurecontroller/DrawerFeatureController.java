package com.blueplanet.smartcookieteacher.featurecontroller;

/**
 * Created by 1311 on 11-03-2016.
 */
public class DrawerFeatureController {

    private static DrawerFeatureController _drawerFeatureController = null;

    private boolean _isFragmentOpenedFromDrawer = false;

    /**
     * function to get single instance of this class
     *
     * @return _loginFeatureController
     */
    public static DrawerFeatureController getInstance() {
        if (_drawerFeatureController == null) {
            _drawerFeatureController = new DrawerFeatureController();
        }
        return _drawerFeatureController;
    }

    /**
     * make constructor private
     */
    private DrawerFeatureController() {

    }

    public boolean isFragmentOpenedFromDrawer() {
        return _isFragmentOpenedFromDrawer;
    }

    public void setIsFragmentOpenedFromDrawer(boolean isFragmentOpenedFromDrawer) {
        _isFragmentOpenedFromDrawer = isFragmentOpenedFromDrawer;
    }
}
