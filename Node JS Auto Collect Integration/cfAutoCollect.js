class cfAutoCollect{
    
    
        constructor(clientId, clientSecret, stage)
        {
              this.clientId = clientId;
              this.jsonresponse;
              this.clientSecret = clientSecret;
              this.token;
              this.expiry;
              this.baseUrl;
              this.stage = stage;
        }

        client_auth(){
            
            
            if (this.stage == "TEST"){
                this.baseUrl = "https://cac-gamma.cashfree.com";
            }
            else if (this.stage == "PROD"){
                this.baseUrl = "https://cac-api.cashfree.com";
            }
            var linkAuthorize = this.baseUrl +  "/cac/v1/authorize";

            var request = require('sync-request')
            
            this.jsonresponse = request("POST",linkAuthorize, {           
                headers:
                {
                'content-type':'application/json',
                'X-Client-Id' : this.clientId,
                'X-Client-Secret' : this.clientSecret
                }
               });
               var data = this.jsonresponse.body.toString('utf-8');
               data = JSON.parse(data);
               
               if (data["status"] == "ERROR"){
                   return data["message"];
                   
               }
               this.expiry = data["data"]["expiry"];
               this.token = data["data"]["token"];
       
           return this.token;
            
        }
        
        create_virtual_account(vAccountId, name,phone, email){

            if ((vAccountId == null) || (name == null) || (email == null) || ( phone == null) )
            {
                return "Mandatory paramters missing";
            }
            else {
                var userParam = {   
                    "vAccountId" : vAccountId,
                    "name" : name,
                    "phone" : phone,
                    "email" : email ,
                    
                }

                var linkCreateVA = this.baseUrl + "/cac/v1/createVA";
                var headers = {
                    'Authorization' : "Bearer " + this.token         
                    } 
                var request = require('sync-request');
                this.jsonresponse = request("POST",linkCreateVA, {
                    json : userParam,
                    headers: headers,
                });
                var data = this.jsonresponse.body.toString('utf-8');
                data = JSON.parse(data);
    
                return data; 
    
            }
        }

        recent_payments(vAccountId){

            if (vAccountId == null){
                return "Mandatory paramteters missing";

            }
            else{
                var headers = {
                    'Authorization' : "Bearer " + this.token            
                    }
                var linkRecentPayments = this.baseUrl + "/cac/v1/payments/" + vAccountId;

                var request = require('sync-request')
                this.jsonresponse = request("GET",linkRecentPayments, {
                   headers: headers,
                });
                var data = this.jsonresponse.body.toString('utf-8');
                data = JSON.parse(data);
    
                return data;  
            }
        }

        list_all_va(){

            var headers = {
                'Authorization' : "Bearer " + this.token            
                }
            var linkListAllVa = this.baseUrl + "/cac/v1/allVA";

            var request = require('sync-request');
            this.jsonresponse = request("GET",linkListAllVa, {
                headers: headers,
            });
            var data = this.jsonresponse.body.toString('utf-8');

            return data; 
            

        }

        is_valid_signature(data){
            
            var orderedData = {};
            Object.keys(data).sort().forEach(function(key) {
              orderedData[key] = data[key];
            });
            var signature = orderedData["signature"];
            delete orderedData["signature"];
            var message = "";

            for (var key in orderedData) {
                if (orderedData.hasOwnProperty(key)) {
                    message = message + orderedData[key];
                }
            }
            var secret = this.clientSecret;
            var crypto = require('crypto');
            var computedSignature = crypto.createHmac('sha256', secret).update(message).digest('base64'); 

            if (computedSignature == signature)
            {
                return true
            }
            else{
                return false
            }         
        }    
    
}
module.exports = cfAutoCollect;


