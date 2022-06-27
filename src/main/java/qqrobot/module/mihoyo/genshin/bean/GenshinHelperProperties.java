package qqrobot.module.mihoyo.genshin.bean;

import java.io.Serializable;
import java.util.List;

public class GenshinHelperProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mode;
    private String sckey;
    private String corpid;
    private String corpsecret;
    private String agentid;
    private String signMode;
    private List<Account> account;
    private String cron;


    public static class Account {
        private String cookie;
        private String stuid;
        private String stoken;
        private String toUser;

        //<editor-fold defaultstate="collapsed" desc="delombok">
        @SuppressWarnings("all")
        public Account() {
        }

        @SuppressWarnings("all")
        public String getCookie() {
            return this.cookie;
        }

        @SuppressWarnings("all")
        public String getStuid() {
            return this.stuid;
        }

        @SuppressWarnings("all")
        public String getStoken() {
            return this.stoken;
        }

        @SuppressWarnings("all")
        public String getToUser() {
            return this.toUser;
        }

        @SuppressWarnings("all")
        public void setCookie(final String cookie) {
            this.cookie = cookie;
        }

        @SuppressWarnings("all")
        public void setStuid(final String stuid) {
            this.stuid = stuid;
        }

        @SuppressWarnings("all")
        public void setStoken(final String stoken) {
            this.stoken = stoken;
        }

        @SuppressWarnings("all")
        public void setToUser(final String toUser) {
            this.toUser = toUser;
        }

        @Override
        @SuppressWarnings("all")
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof GenshinHelperProperties.Account)) return false;
            final GenshinHelperProperties.Account other = (GenshinHelperProperties.Account) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$cookie = this.getCookie();
            final Object other$cookie = other.getCookie();
            if (this$cookie == null ? other$cookie != null : !this$cookie.equals(other$cookie)) return false;
            final Object this$stuid = this.getStuid();
            final Object other$stuid = other.getStuid();
            if (this$stuid == null ? other$stuid != null : !this$stuid.equals(other$stuid)) return false;
            final Object this$stoken = this.getStoken();
            final Object other$stoken = other.getStoken();
            if (this$stoken == null ? other$stoken != null : !this$stoken.equals(other$stoken)) return false;
            final Object this$toUser = this.getToUser();
            final Object other$toUser = other.getToUser();
            if (this$toUser == null ? other$toUser != null : !this$toUser.equals(other$toUser)) return false;
            return true;
        }

        @SuppressWarnings("all")
        protected boolean canEqual(final Object other) {
            return other instanceof GenshinHelperProperties.Account;
        }

        @Override
        @SuppressWarnings("all")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $cookie = this.getCookie();
            result = result * PRIME + ($cookie == null ? 43 : $cookie.hashCode());
            final Object $stuid = this.getStuid();
            result = result * PRIME + ($stuid == null ? 43 : $stuid.hashCode());
            final Object $stoken = this.getStoken();
            result = result * PRIME + ($stoken == null ? 43 : $stoken.hashCode());
            final Object $toUser = this.getToUser();
            result = result * PRIME + ($toUser == null ? 43 : $toUser.hashCode());
            return result;
        }

        @Override
        @SuppressWarnings("all")
        public String toString() {
            return "GenshinHelperProperties.Account(cookie=" + this.getCookie() + ", stuid=" + this.getStuid() + ", stoken=" + this.getStoken() + ", toUser=" + this.getToUser() + ")";
        }
        //</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="delombok">
    @SuppressWarnings("all")
    public GenshinHelperProperties() {
    }

    @SuppressWarnings("all")
    public String getMode() {
        return this.mode;
    }

    @SuppressWarnings("all")
    public String getSckey() {
        return this.sckey;
    }

    @SuppressWarnings("all")
    public String getCorpid() {
        return this.corpid;
    }

    @SuppressWarnings("all")
    public String getCorpsecret() {
        return this.corpsecret;
    }

    @SuppressWarnings("all")
    public String getAgentid() {
        return this.agentid;
    }

    @SuppressWarnings("all")
    public String getSignMode() {
        return this.signMode;
    }

    @SuppressWarnings("all")
    public List<Account> getAccount() {
        return this.account;
    }

    @SuppressWarnings("all")
    public String getCron() {
        return this.cron;
    }

    @SuppressWarnings("all")
    public void setMode(final String mode) {
        this.mode = mode;
    }

    @SuppressWarnings("all")
    public void setSckey(final String sckey) {
        this.sckey = sckey;
    }

    @SuppressWarnings("all")
    public void setCorpid(final String corpid) {
        this.corpid = corpid;
    }

    @SuppressWarnings("all")
    public void setCorpsecret(final String corpsecret) {
        this.corpsecret = corpsecret;
    }

    @SuppressWarnings("all")
    public void setAgentid(final String agentid) {
        this.agentid = agentid;
    }

    @SuppressWarnings("all")
    public void setSignMode(final String signMode) {
        this.signMode = signMode;
    }

    @SuppressWarnings("all")
    public void setAccount(final List<Account> account) {
        this.account = account;
    }

    @SuppressWarnings("all")
    public void setCron(final String cron) {
        this.cron = cron;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GenshinHelperProperties)) return false;
        final GenshinHelperProperties other = (GenshinHelperProperties) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$mode = this.getMode();
        final Object other$mode = other.getMode();
        if (this$mode == null ? other$mode != null : !this$mode.equals(other$mode)) return false;
        final Object this$sckey = this.getSckey();
        final Object other$sckey = other.getSckey();
        if (this$sckey == null ? other$sckey != null : !this$sckey.equals(other$sckey)) return false;
        final Object this$corpid = this.getCorpid();
        final Object other$corpid = other.getCorpid();
        if (this$corpid == null ? other$corpid != null : !this$corpid.equals(other$corpid)) return false;
        final Object this$corpsecret = this.getCorpsecret();
        final Object other$corpsecret = other.getCorpsecret();
        if (this$corpsecret == null ? other$corpsecret != null : !this$corpsecret.equals(other$corpsecret)) return false;
        final Object this$agentid = this.getAgentid();
        final Object other$agentid = other.getAgentid();
        if (this$agentid == null ? other$agentid != null : !this$agentid.equals(other$agentid)) return false;
        final Object this$signMode = this.getSignMode();
        final Object other$signMode = other.getSignMode();
        if (this$signMode == null ? other$signMode != null : !this$signMode.equals(other$signMode)) return false;
        final Object this$account = this.getAccount();
        final Object other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) return false;
        final Object this$cron = this.getCron();
        final Object other$cron = other.getCron();
        if (this$cron == null ? other$cron != null : !this$cron.equals(other$cron)) return false;
        return true;
    }

    @SuppressWarnings("all")
    protected boolean canEqual(final Object other) {
        return other instanceof GenshinHelperProperties;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $mode = this.getMode();
        result = result * PRIME + ($mode == null ? 43 : $mode.hashCode());
        final Object $sckey = this.getSckey();
        result = result * PRIME + ($sckey == null ? 43 : $sckey.hashCode());
        final Object $corpid = this.getCorpid();
        result = result * PRIME + ($corpid == null ? 43 : $corpid.hashCode());
        final Object $corpsecret = this.getCorpsecret();
        result = result * PRIME + ($corpsecret == null ? 43 : $corpsecret.hashCode());
        final Object $agentid = this.getAgentid();
        result = result * PRIME + ($agentid == null ? 43 : $agentid.hashCode());
        final Object $signMode = this.getSignMode();
        result = result * PRIME + ($signMode == null ? 43 : $signMode.hashCode());
        final Object $account = this.getAccount();
        result = result * PRIME + ($account == null ? 43 : $account.hashCode());
        final Object $cron = this.getCron();
        result = result * PRIME + ($cron == null ? 43 : $cron.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "GenshinHelperProperties(mode=" + this.getMode() + ", sckey=" + this.getSckey() + ", corpid=" + this.getCorpid() + ", corpsecret=" + this.getCorpsecret() + ", agentid=" + this.getAgentid() + ", signMode=" + this.getSignMode() + ", account=" + this.getAccount() + ", cron=" + this.getCron() + ")";
    }
    //</editor-fold>
}
