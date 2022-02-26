
package com.lickhunter.web.models.sentiments;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "s",
    "n",
    "p",
    "p_btc",
    "v",
    "vt",
    "pc",
    "pch",
    "mc",
    "gs",
    "ss",
    "as",
    "bl",
    "br",
    "sp",
    "na",
    "md",
    "t",
    "r",
    "yt",
    "sv",
    "u",
    "c",
    "sd",
    "d",
    "cr",
    "acr",
    "tc",
    "categories"
})
@Generated("jsonschema2pojo")
public class Btc {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("s")
    private String s;
    @JsonProperty("n")
    private String n;
    @JsonProperty("p")
    private Double p;
    @JsonProperty("p_btc")
    private Long pBtc;
    @JsonProperty("v")
    private Double v;
    @JsonProperty("vt")
    private Double vt;
    @JsonProperty("pc")
    private Double pc;
    @JsonProperty("pch")
    private Double pch;
    @JsonProperty("mc")
    private Long mc;
    @JsonProperty("gs")
    private Double gs;
    @JsonProperty("ss")
    private Long ss;
    @JsonProperty("as")
    private Double as;
    @JsonProperty("bl")
    private Long bl;
    @JsonProperty("br")
    private Long br;
    @JsonProperty("sp")
    private Long sp;
    @JsonProperty("na")
    private Long na;
    @JsonProperty("md")
    private Long md;
    @JsonProperty("t")
    private Long t;
    @JsonProperty("r")
    private Long r;
    @JsonProperty("yt")
    private Long yt;
    @JsonProperty("sv")
    private Long sv;
    @JsonProperty("u")
    private Long u;
    @JsonProperty("c")
    private Long c;
    @JsonProperty("sd")
    private Double sd;
    @JsonProperty("d")
    private Double d;
    @JsonProperty("cr")
    private Double cr;
    @JsonProperty("acr")
    private Long acr;
    @JsonProperty("tc")
    private Long tc;
    @JsonProperty("categories")
    private String categories;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("s")
    public String getS() {
        return s;
    }

    @JsonProperty("s")
    public void setS(String s) {
        this.s = s;
    }

    @JsonProperty("n")
    public String getN() {
        return n;
    }

    @JsonProperty("n")
    public void setN(String n) {
        this.n = n;
    }

    @JsonProperty("p")
    public Double getP() {
        return p;
    }

    @JsonProperty("p")
    public void setP(Double p) {
        this.p = p;
    }

    @JsonProperty("p_btc")
    public Long getpBtc() {
        return pBtc;
    }

    @JsonProperty("p_btc")
    public void setpBtc(Long pBtc) {
        this.pBtc = pBtc;
    }

    @JsonProperty("v")
    public Double getV() {
        return v;
    }

    @JsonProperty("v")
    public void setV(Double v) {
        this.v = v;
    }

    @JsonProperty("vt")
    public Double getVt() {
        return vt;
    }

    @JsonProperty("vt")
    public void setVt(Double vt) {
        this.vt = vt;
    }

    @JsonProperty("pc")
    public Double getPc() {
        return pc;
    }

    @JsonProperty("pc")
    public void setPc(Double pc) {
        this.pc = pc;
    }

    @JsonProperty("pch")
    public Double getPch() {
        return pch;
    }

    @JsonProperty("pch")
    public void setPch(Double pch) {
        this.pch = pch;
    }

    @JsonProperty("mc")
    public Long getMc() {
        return mc;
    }

    @JsonProperty("mc")
    public void setMc(Long mc) {
        this.mc = mc;
    }

    @JsonProperty("gs")
    public Double getGs() {
        return gs;
    }

    @JsonProperty("gs")
    public void setGs(Double gs) {
        this.gs = gs;
    }

    @JsonProperty("ss")
    public Long getSs() {
        return ss;
    }

    @JsonProperty("ss")
    public void setSs(Long ss) {
        this.ss = ss;
    }

    @JsonProperty("as")
    public Double getAs() {
        return as;
    }

    @JsonProperty("as")
    public void setAs(Double as) {
        this.as = as;
    }

    @JsonProperty("bl")
    public Long getBl() {
        return bl;
    }

    @JsonProperty("bl")
    public void setBl(Long bl) {
        this.bl = bl;
    }

    @JsonProperty("br")
    public Long getBr() {
        return br;
    }

    @JsonProperty("br")
    public void setBr(Long br) {
        this.br = br;
    }

    @JsonProperty("sp")
    public Long getSp() {
        return sp;
    }

    @JsonProperty("sp")
    public void setSp(Long sp) {
        this.sp = sp;
    }

    @JsonProperty("na")
    public Long getNa() {
        return na;
    }

    @JsonProperty("na")
    public void setNa(Long na) {
        this.na = na;
    }

    @JsonProperty("md")
    public Long getMd() {
        return md;
    }

    @JsonProperty("md")
    public void setMd(Long md) {
        this.md = md;
    }

    @JsonProperty("t")
    public Long getT() {
        return t;
    }

    @JsonProperty("t")
    public void setT(Long t) {
        this.t = t;
    }

    @JsonProperty("r")
    public Long getR() {
        return r;
    }

    @JsonProperty("r")
    public void setR(Long r) {
        this.r = r;
    }

    @JsonProperty("yt")
    public Long getYt() {
        return yt;
    }

    @JsonProperty("yt")
    public void setYt(Long yt) {
        this.yt = yt;
    }

    @JsonProperty("sv")
    public Long getSv() {
        return sv;
    }

    @JsonProperty("sv")
    public void setSv(Long sv) {
        this.sv = sv;
    }

    @JsonProperty("u")
    public Long getU() {
        return u;
    }

    @JsonProperty("u")
    public void setU(Long u) {
        this.u = u;
    }

    @JsonProperty("c")
    public Long getC() {
        return c;
    }

    @JsonProperty("c")
    public void setC(Long c) {
        this.c = c;
    }

    @JsonProperty("sd")
    public Double getSd() {
        return sd;
    }

    @JsonProperty("sd")
    public void setSd(Double sd) {
        this.sd = sd;
    }

    @JsonProperty("d")
    public Double getD() {
        return d;
    }

    @JsonProperty("d")
    public void setD(Double d) {
        this.d = d;
    }

    @JsonProperty("cr")
    public Double getCr() {
        return cr;
    }

    @JsonProperty("cr")
    public void setCr(Double cr) {
        this.cr = cr;
    }

    @JsonProperty("acr")
    public Long getAcr() {
        return acr;
    }

    @JsonProperty("acr")
    public void setAcr(Long acr) {
        this.acr = acr;
    }

    @JsonProperty("tc")
    public Long getTc() {
        return tc;
    }

    @JsonProperty("tc")
    public void setTc(Long tc) {
        this.tc = tc;
    }

    @JsonProperty("categories")
    public String getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(String categories) {
        this.categories = categories;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
