from cfAutoCollect import cfAutoCollect

userExample = cfAutoCollect("CF27EFXRWU3WKIAAUUQ", "5bd9ba5e0bf69112dde1a7f0a77121c3b78b144c", "TEST")
userExample.client_auth()
userExample.create_virtual_account("TEST", "Tester","9999999999", "test@gmail.com")
userExample.recent_payments("TEST")
userExample.list_all_va()
#dummy data for webhook execution

data = {
     "event" :    "AMOUNT_COLLECTED",
     "amount" : "15",
     "vAccountId" : "PREE",
     "vAccountNumber"  : "CASHFREELGSHPREE",
     "email" : "preetha.datta@gmail.com",
     "phone"  : "9910115208",
    "referenceId"   : "165196",
    "utr" : "78956755343816",
    "creditRefNo" : "9240092284977434",
    "remitterAccount" : "1234567890",
    "remitterName" : "Cashfree",
    "paymentTime": "2018-06-25 18:02:37",
    "signature"   :  "8jT8J7nER2jExS55K0Gp3EgT2QP7kVNw6lXC0doanf8="
}
#receive data through our webhook calls.
userExample.is_valid_signature(data)
