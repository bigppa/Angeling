package org.sopt.angeling.Model;

/**
 * Created by DongHyun on 2016-01-13.
 */
public class Voluntary {
    public long progrmRegistNo; //프로그램등록번호
    public String progrmSj; //봉사제목
    public int progrmSttusSe; //모집상태
    public String progrmBgnde; //봉사시작일자
    public String progrmEndde; //봉사종료일자
    public int actBeginTm; //봉사시작시간
    public int actEndTm; //봉사종료시간
    public String noticeBgnde; //모집시작일
    public String noticeEndde; //모집종료일
    public int rcritNmpr; //모집인원
    public String  actWkdy;//활동요일
    public int appTotal; //신청인원수
    public String srvcClCode; //봉사분야
    public String adultPosblAt; //성인가능여부
    public String yngbgsPosblAt; //청소년가능여부
    public String familyPosblAt; //가족가능여부
    public String pbsvntPosblAt; //공무원가능여부
    public String grpPosblAt; //단체가능여부
    public String mnnstNm; //모집기관
    public String nanmmbyNm; //등록기관
    public String actPlace; //봉사장소
    public String nanmmbyNmAdmn; //담당자명
    public String astelno; //전화번호
    public String fxnum; //팩스번호
    public String postAdres; //담당자주소
    public String email; //담당자 이메일
    public String progrmCn; //상세내용

    public double x; //경도
    public double y; //위도

    public Voluntary(long progrmRegistNo, String progrmSj, int progrmSttusSe, String progrmBgnde,
                     String progrmEndde, int actBeginTm, int actEndTm, String noticeBgnde, String noticeEndde,
                     int rcritNmpr, String actWkdy, int appTotal, String srvcClCode, String adultPosblAt, String yngbgsPosblAt,
                     String familyPosblAt, String pbsvntPosblAt, String grpPosblAt, String mnnstNm, String nanmmbyNm, String actPlace,
                     String nanmmbyNmAdmn, String astelno, double x, double y) {
        this.progrmRegistNo = progrmRegistNo;
        this.progrmSj = progrmSj;
        this.progrmSttusSe = progrmSttusSe;
        this.progrmBgnde = progrmBgnde;
        this.progrmEndde = progrmEndde;
        this.actBeginTm = actBeginTm;
        this.actEndTm = actEndTm;
        this.noticeBgnde = noticeBgnde;
        this.noticeEndde = noticeEndde;
        this.rcritNmpr = rcritNmpr;
        this.actWkdy = actWkdy;
        this.appTotal = appTotal;
        this.srvcClCode = srvcClCode;
        this.adultPosblAt = adultPosblAt;
        this.yngbgsPosblAt = yngbgsPosblAt;
        this.familyPosblAt = familyPosblAt;
        this.pbsvntPosblAt = pbsvntPosblAt;
        this.grpPosblAt = grpPosblAt;
        this.mnnstNm = mnnstNm;
        this.nanmmbyNm = nanmmbyNm;
        this.actPlace = actPlace;
        this.nanmmbyNmAdmn = nanmmbyNmAdmn;
        this.astelno = astelno;
        this.x = x;
        this.y = y;
    }
}
