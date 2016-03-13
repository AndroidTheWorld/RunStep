package ling.runstep.event;

/**
 * Created by Jalyn on 2016/2/20.
 */
public class SendToEvent {

    public static class sendUserName{
        public String loginNameString;
        public sendUserName(String loginNameString){
            this.loginNameString = loginNameString;
        }

    }
    public static class sendDataToLogin{
        public String userName;
        public String userPw;

        public sendDataToLogin(String userName, String userPw) {
            this.userName = userName;
            this.userPw = userPw;
        }
    }
}
