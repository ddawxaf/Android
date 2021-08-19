package com.hgapp.a0086.data;

import java.util.List;

public class LeagueDatailNewData {
    private String gid;
    private String league;
    private String M_Type;
    private String M_Time;
    private String M_Date;
    private String showretime;
    private String team_h;
    private String team_c;
    private String score_h;
    private String score_c;
    private String all;
    private String order_method;
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrder_method() {
        return order_method;
    }

    public void setOrder_method(String order_method) {
        this.order_method = order_method;
    }

    public String getScore_h() {
        return score_h;
    }

    public void setScore_h(String score_h) {
        this.score_h = score_h;
    }

    public String getScore_c() {
        return score_c;
    }

    public void setScore_c(String score_c) {
        this.score_c = score_c;
    }

    private List<LeagueDatailNewData.DataBean> data;

    private List<LeagueDetailListDataResults.DataBean> gameData;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getM_Type() {
        return M_Type;
    }

    public void setM_Type(String m_Type) {
        M_Type = m_Type;
    }

    public String getM_Time() {
        return M_Time;
    }

    public void setM_Time(String m_Time) {
        M_Time = m_Time;
    }

    public String getM_Date() {
        return M_Date;
    }

    public void setM_Date(String m_Date) {
        M_Date = m_Date;
    }

    public String getShowretime() {
        return showretime;
    }

    public void setShowretime(String showretime) {
        this.showretime = showretime;
    }

    public String getTeam_h() {
        return team_h;
    }

    public void setTeam_h(String team_h) {
        this.team_h = team_h;
    }

    public String getTeam_c() {
        return team_c;
    }

    public void setTeam_c(String team_c) {
        this.team_c = team_c;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public List<LeagueDatailNewData.DataBean> getData() {
        return data;
    }

    public void setData(List<LeagueDatailNewData.DataBean> data) {
        this.data = data;
    }

    public List<LeagueDetailListDataResults.DataBean> getGameData() {
        return gameData;
    }

    public void setGameData(List<LeagueDetailListDataResults.DataBean> gameData) {
        this.gameData = gameData;
    }

    public static class DataBean {

        private String gid;
        private String cate;
        private String type;
        private String active;
        private String line_type;
        private String odd_f_type;
        private String gold;
        private String ioradio_r_h;
        private String rtype;
        private String wtype;
        private String order_method;
        private String buyOrderTitle;
        private String btype_h;
        private String btype_c;
        private String pwtype;
        private String league;
        private String M_Type;
        private String M_Time;
        private String M_Date;
        private String showretime;
        private String gnum_h;
        private String gnum_c;
        private String team_h;
        private String team_c;
        private String strong;
        private String ratio;
        private String textUp;
        private String textDown;
        private String textUpStr;
        private String textDownStr;
        private String textM;
        private String textMStr;

        public String getBuyOrderTitle() {
            return buyOrderTitle;
        }

        public void setBuyOrderTitle(String buyOrderTitle) {
            this.buyOrderTitle = buyOrderTitle;
        }

        public String getOrder_method() {
            return order_method;
        }

        public void setOrder_method(String order_method) {
            this.order_method = order_method;
        }

        public String getBtype_h() {
            return btype_h;
        }

        public void setBtype_h(String btype_h) {
            this.btype_h = btype_h;
        }

        public String getBtype_c() {
            return btype_c;
        }

        public void setBtype_c(String btype_c) {
            this.btype_c = btype_c;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getLine_type() {
            return line_type;
        }

        public void setLine_type(String line_type) {
            this.line_type = line_type;
        }

        public String getOdd_f_type() {
            return odd_f_type;
        }

        public void setOdd_f_type(String odd_f_type) {
            this.odd_f_type = odd_f_type;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getIoradio_r_h() {
            return ioradio_r_h;
        }

        public void setIoradio_r_h(String ioradio_r_h) {
            this.ioradio_r_h = ioradio_r_h;
        }

        public String getRtype() {
            return rtype;
        }

        public void setRtype(String rtype) {
            this.rtype = rtype;
        }

        public String getWtype() {
            return wtype;
        }

        public void setWtype(String wtype) {
            this.wtype = wtype;
        }

        public String getPwtype() {
            return pwtype;
        }

        public void setPwtype(String pwtype) {
            this.pwtype = pwtype;
        }

        public String getLeague() {
            return league;
        }

        public void setLeague(String league) {
            this.league = league;
        }

        public String getM_Type() {
            return M_Type;
        }

        public void setM_Type(String m_Type) {
            M_Type = m_Type;
        }

        public String getM_Time() {
            return M_Time;
        }

        public void setM_Time(String m_Time) {
            M_Time = m_Time;
        }

        public String getM_Date() {
            return M_Date;
        }

        public void setM_Date(String m_Date) {
            M_Date = m_Date;
        }

        public String getShowretime() {
            return showretime;
        }

        public void setShowretime(String showretime) {
            this.showretime = showretime;
        }

        public String getGnum_h() {
            return gnum_h;
        }

        public void setGnum_h(String gnum_h) {
            this.gnum_h = gnum_h;
        }

        public String getGnum_c() {
            return gnum_c;
        }

        public void setGnum_c(String gnum_c) {
            this.gnum_c = gnum_c;
        }

        public String getTeam_h() {
            return team_h;
        }

        public void setTeam_h(String team_h) {
            this.team_h = team_h;
        }

        public String getTeam_c() {
            return team_c;
        }

        public void setTeam_c(String team_c) {
            this.team_c = team_c;
        }

        public String getStrong() {
            return strong;
        }

        public void setStrong(String strong) {
            this.strong = strong;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getTextUp() {
            return textUp;
        }

        public void setTextUp(String textUp) {
            this.textUp = textUp;
        }

        public String getTextDown() {
            return textDown;
        }

        public void setTextDown(String textDown) {
            this.textDown = textDown;
        }

        public String getTextUpStr() {
            return textUpStr;
        }

        public void setTextUpStr(String textUpStr) {
            this.textUpStr = textUpStr;
        }

        public String getTextDownStr() {
            return textDownStr;
        }

        public void setTextDownStr(String textDownStr) {
            this.textDownStr = textDownStr;
        }

        public String getTextM() {
            return textM;
        }

        public void setTextM(String textM) {
            this.textM = textM;
        }

        public String getTextMStr() {
            return textMStr;
        }

        public void setTextMStr(String textMStr) {
            this.textMStr = textMStr;
        }
    }
}
