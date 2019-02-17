package org.sopt.angeling.Model;

/**
 * Created by DongHyun on 2016-01-13.
 */
public class Thumbnail {
    public long progrmRegistNo;
    public String progrmSj;
    public String postAdres;
    public String astelno;

    public double distance=0;
    public double x; //위도
    public double y; //경도

    public Thumbnail(long registno, String program, String address, String telephone, double x, double y) {
        this.progrmRegistNo = registno;
        this.progrmSj = program;
        this.postAdres = address;
        this.astelno = telephone;
        this.x = x;
        this.y = y;
    }
}