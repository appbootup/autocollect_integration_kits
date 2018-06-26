package cashfreeUser_java_integration;


public class execute{

    public static void main(String[] args)throws Exception {

        cfAutoCollect userExample = new cfAutoCollect("CF27EFXRWU3WKIAAUUQ", "5bd9ba5e0bf69112dde1a7f0a77121c3b78b144c", "TEST");

        System.out.println(userExample.client_auth());
        userExample.create_virtual_account("TEST","Tester", "9999999999", "tester@gmail.com");
        userExample.recent_payments("TEST");
        userExample.list_all_va();

    }


}


