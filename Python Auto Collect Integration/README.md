#  Cashfree Python AutoCollect Integration 

Python bindings for interacting with the Cashfree API for AutoCollect. This is useful for merchants who are looking to automatically reconcile their incoming bank transfers. 

# Using 

As you can see there are two files. "Execute.py" is a guide to calling the API. <br />
NOTE : Ensure that "execute.py" and "cfAutoCollect.py" are in the same folder.

# Setting Up

You will need to authenticate client by calling the client_auth function as follows : 

```python
from cfAutoCollect import cfAutoCollect

userExample = cfAutoCollect("DummyClientID", "DummyClientSecret", "TEST/PROD")
userExample.client_auth()
```

# Functionality

You can perform the following functions : 

**Create Virtual Account**
```
userExample.create_virtual_account("TEST", "Tester","9999999999", "test@gmail.com")
```

**View Recent Payments according to Virtual Account ID**
```
userExample.recent_payments("TEST")
```
**List all Virtual Accounts **

```
userExample.list_all_va()
```
## Found a bug?

Report it at [https://github.com/cashfree/autocollect_integration_kits/issues](https://github.com/cashfree/autocollect_integration_kits/issues)

# Support

For further queries, reach us at techsupport@gocashfree.com .

********************************************************************************** 





