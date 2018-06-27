package cashfreeUser_java_integration;


public class execute{

    public static void main(String[] args)throws Exception {

        cfAutoCollect userExample = new cfAutoCollect("dummyclientId", "dummyClientSecret", "TEST");

        System.out.println(userExample.client_auth());
        userExample.create_virtual_account("TEST","Tester", "9999999999", "tester@gmail.com");
        userExample.recent_payments("TEST");
        userExample.list_all_va();

    }


}


