package org.sopt.angeling.Controller;

import org.sopt.angeling.Model.Thumbnail;

/**
 * Created by DongHyun on 2016-01-14.
 */
public interface MarkerListener extends Distance_Interface {
    void pickMarker(Thumbnail thumbnail);
}
