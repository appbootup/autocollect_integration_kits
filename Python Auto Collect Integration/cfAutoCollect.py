# -*- coding: utf-8 -*-

import json
import requests
import collections
import hashlib
import hmac
import base64

class cfAutoCollect:
    clientId = None
    clientSecret = None
    stage = None
    expiry = None
    baseUrl = None 
    token = None 

    def __init__(self, clientId, clientSecret, stage):
        cfAutoCollect.clientId = clientId
        cfAutoCollect.clientSecret = clientSecret
        cfAutoCollect.stage = stage

    def client_auth(self):

        if cfAutoCollect.stage == "TEST" : 

            cfAutoCollect.baseUrl = "https://cac-gamma.cashfree.com"
        
        elif cfAutoCollect.stage == "PROD" : 

            cfAutoCollect.baseUrl = "https://cac-api.cashfree.com"

        else :

            return "invalid stage entered"
        
        headers = {
        'X-Client-Id' : cfAutoCollect.clientId,
        'X-Client-Secret' : cfAutoCollect.clientSecret 
        }

        try :

            linkAuthorize = cfAutoCollect.baseUrl  + "/cac/v1/authorize"


            jsonData = requests.post(url = linkAuthorize, headers = headers)
            jsonData = json.loads(jsonData.text)
            
            #adding check in case API does not return token and expiry
            if jsonData["status"] == "ERROR" : 
                return jsonData["message"]

            cfAutoCollect.token = jsonData["data"]["token"]
            cfAutoCollect.expiry = jsonData["data"]["expiry"]
            return cfAutoCollect.token

        except Exception as e:
            return e 
            return "Empty JSON response"


    def create_virtual_account(self,vAccountId, name,phone, email):

        if (vAccountId == None or name == None or phone == None or email == None):

            return "mandatory parameters missing"

        else:
            userParam = {   
                "vAccountId" : vAccountId,
                "name" : name,
                "phone" : phone,
                "email" : email ,
            }
            headers = {
            'Authorization' : "Bearer " + str(cfAutoCollect.token)            
            } 
            linkCreateVA = cfAutoCollect.baseUrl + "/cac/v1/createVA"
            jsonData = requests.post(url = linkCreateVA, headers = headers, data = json.dumps(userParam))
            jsonData = json.loads(jsonData.text)

            return jsonData

    
    def recent_payments(self,vAccountId):

        if (vAccountId == None ):

            return "mandatory paramters missing"

        else : 

            headers = {
            'Authorization' : "Bearer " + str(cfAutoCollect.token)            
            }
            linkRecentPayments = cfAutoCollect.baseUrl + "/cac/v1/payments/" + vAccountId 
            jsonData = requests.get(url = linkRecentPayments, headers = headers)
            jsonData = json.loads(jsonData.text)

            return jsonData
    
    def list_all_va(self):

        headers = {
            'Authorization' : "Bearer " + str(cfAutoCollect.token)            
            }

        linkListAllVa = cfAutoCollect.baseUrl + "/cac/v1/allVA"
        jsonData = requests.get(url = linkListAllVa, headers = headers)
        jsonData = json.loads(jsonData.text)

        return jsonData


    def is_valid_signature(self, data):

        #hardcoding data for now 
        sortedData = []
        
        sortedData = collections.OrderedDict(sorted(data.items()))
        signature = sortedData["signature"]
        del sortedData["signature"]
        signatureData = ""
        for key in sorted(sortedData):
            signatureData += sortedData[key]
            message = signatureData.encode('utf-8')
            
        
        secret = cfAutoCollect.clientSecret
        secret = secret.encode('utf-8')

        computedsignature = base64.b64encode(hmac.new(secret,message,digestmod=hashlib.sha256).digest()).decode('utf-8')
        
        if (signature == computedsignature):
            return True
        else:
            return False


